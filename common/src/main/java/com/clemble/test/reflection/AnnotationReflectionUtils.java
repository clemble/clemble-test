package com.clemble.test.reflection;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AnnotationReflectionUtils {

    /** URL prefix for loading from the file system: "file:" */
    public static final String FILE_URL_PREFIX = "file:";

    /** URL protocol for an entry from a jar file: "jar" */
    public static final String URL_PROTOCOL_JAR = "jar";

    /** URL protocol for an entry from a zip file: "zip" */
    public static final String URL_PROTOCOL_ZIP = "zip";

    /** URL protocol for an entry from a JBoss jar file: "vfszip" */
    public static final String URL_PROTOCOL_VFSZIP = "vfszip";

    /** URL protocol for a JBoss VFS resource: "vfs" */
    public static final String URL_PROTOCOL_VFS = "vfs";

    /** URL protocol for an entry from a WebSphere jar file: "wsjar" */
    public static final String URL_PROTOCOL_WSJAR = "wsjar";

    /** URL protocol for an entry from an OC4J jar file: "code-source" */
    public static final String URL_PROTOCOL_CODE_SOURCE = "code-source";

    /** Separator between JAR URL and file path within the JAR */
    public static final String JAR_URL_SEPARATOR = "!/";

    // Taken from http://stackoverflow.com/questions/1456930/how-do-i-read-all-classes-from-a-java-package-in-the-classpath

    public static <T extends Annotation> List<Class<?>> findCandidates(String basePackage, Class<T> searchedAnnotation) {
        ArrayList<Class<?>> candidates = new ArrayList<Class<?>>();
        Enumeration<URL> urls;
        String basePath = basePackage.replaceAll("\\.", File.separator);
        try {
            urls = Thread.currentThread().getContextClassLoader().getResources(basePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (isJarURL(url)) {
                try {
                    candidates.addAll(doFindPathMatchingJarResources(url, basePath, searchedAnnotation));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                File directory = new File(url.getFile());
                if (directory.exists() && directory.isDirectory()) {
                    for (File file : new File(url.getFile()).listFiles())
                        fetchCandidates(basePackage, file, searchedAnnotation, candidates);
                }
            }
        }
        return candidates;
    }

    private static <T extends Annotation> void fetchCandidates(String basePackage, File candidate, Class<T> searchedAnnotation, List<Class<?>> candidates) {
        if (candidate.isDirectory()) {
            for (File file : candidate.listFiles())
                fetchCandidates(basePackage + "." + candidate.getName(), file, searchedAnnotation, candidates);
        } else {
            String fileName = candidate.getName();
            if (fileName.endsWith(".class")) {
                String className = fileName.substring(0, fileName.length() - 6);
                Class<?> foundClass = checkCandidate(basePackage + "." + className, searchedAnnotation);

                if (foundClass != null)
                    candidates.add(foundClass);
            }
        }
    }

    public static boolean isJarURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_ZIP.equals(protocol) || URL_PROTOCOL_WSJAR.equals(protocol) || (URL_PROTOCOL_CODE_SOURCE
                .equals(protocol) && url.getPath().contains(JAR_URL_SEPARATOR)));
    }

    public static <T extends Annotation> Class<?> checkCandidate(String className, Class<T> searchedAnnotation) {
        try {
            Class<?> candidateClass = Class.forName(className);
            Target target = searchedAnnotation.getAnnotation(Target.class);
            for(ElementType elementType: target.value()) {
                switch(elementType) {
                case TYPE:
                    if (candidateClass.getAnnotation(searchedAnnotation) != null)
                        return candidateClass;
                    break;
                case CONSTRUCTOR:
                    for(Constructor<?> constructor: candidateClass.getConstructors())
                        if(constructor.getAnnotation(searchedAnnotation) != null)
                            return candidateClass;
                    break;
                case METHOD:
                    for(Method method: candidateClass.getMethods())
                        if(method.getAnnotation(searchedAnnotation) != null)
                            return candidateClass;
                    break;
                case FIELD:
                    for(Field field: candidateClass.getFields())
                        if(field.getAnnotation(searchedAnnotation) != null)
                            return candidateClass;
                    break;
                default:
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
        } catch (NoClassDefFoundError e) {
        }
        return null;
    }

    /**
     * Find all resources in jar files that match the given location pattern
     * via the Ant-style PathMatcher.
     * 
     * @param rootDirResource the root directory as Resource
     * @param subPattern the sub pattern to match (below the root directory)
     * @return the Set of matching Resource instances
     * @throws IOException in case of I/O errors
     * @see java.net.JarURLConnection
     * @see org.springframework.util.PathMatcher
     */
    protected static <T extends Annotation> Set<Class<?>> doFindPathMatchingJarResources(URL sourceUrl, String basePackage, Class<T> searchedAnnotation)
            throws IOException {

        URLConnection con = sourceUrl.openConnection();
        JarFile jarFile;
        String jarFileUrl;
        String rootEntryPath;
        boolean newJarFile = false;

        if (con instanceof JarURLConnection) {
            // Should usually be the case for traditional JAR files.
            JarURLConnection jarCon = (JarURLConnection) con;
            jarFile = jarCon.getJarFile();
            jarFileUrl = jarCon.getJarFileURL().toExternalForm();
            JarEntry jarEntry = jarCon.getJarEntry();
            rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
        } else {
            // No JarURLConnection -> need to resort to URL file parsing.
            // We'll assume URLs of the format "jar:path!/entry", with the protocol
            // being arbitrary as long as following the entry format.
            // We'll also handle paths with and without leading "file:" prefix.
            String urlFile = sourceUrl.getFile();
            int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
            if (separatorIndex != -1) {
                jarFileUrl = urlFile.substring(0, separatorIndex);
                rootEntryPath = urlFile.substring(separatorIndex + JAR_URL_SEPARATOR.length());
                jarFile = getJarFile(jarFileUrl);
            } else {
                jarFile = new JarFile(urlFile);
                jarFileUrl = urlFile;
                rootEntryPath = "";
            }
            newJarFile = true;
        }

        try {
            if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
                // Root entry path must end with slash to allow for proper matching.
                // The Sun JRE does not return a slash here, but BEA JRockit does.
                rootEntryPath = rootEntryPath + "/";
            }
            Set<Class<?>> result = new LinkedHashSet<Class<?>>(8);
            for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
                JarEntry entry = entries.nextElement();
                String entryPath = entry.getName();
                if (entryPath.startsWith(rootEntryPath) && entryPath.endsWith(".class")) {
                    int entryLength = entryPath.length();
                    String className = entryPath.replaceAll(File.separator, ".").substring(0, entryLength - 6);
                    Class<?> foundClass = checkCandidate(className, searchedAnnotation);
                    if (foundClass != null)
                        result.add(foundClass);
                }
            }
            return result;
        } finally {
            // Close jar file, but only if freshly obtained -
            // not from JarURLConnection, which might cache the file reference.
            if (newJarFile) {
                jarFile.close();
            }
        }
    }

    /**
     * Resolve the given jar file URL into a JarFile object.
     */
    protected static JarFile getJarFile(String jarFileUrl) throws IOException {
        if (jarFileUrl.startsWith(FILE_URL_PREFIX)) {
            try {
                return new JarFile(new URI(jarFileUrl.replaceAll(" ", "%20")).getSchemeSpecificPart());
            } catch (URISyntaxException ex) {
                // Fallback for URLs that are not valid URIs (should hardly ever happen).
                return new JarFile(jarFileUrl.substring(FILE_URL_PREFIX.length()));
            }
        } else {
            return new JarFile(jarFileUrl);
        }
    }

}
