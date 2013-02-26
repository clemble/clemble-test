package com.stresstest.jbehave.internal.startup;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.regex.Pattern;

public class JarSearchUtils {

    public static String discoverPathToJarFile(String nameRegExp) {
        Pattern pattern = Pattern.compile(nameRegExp);
        // Step 1. Checking classpath
        String jarFilePath = searchInClasspath(pattern);
        if(jarFilePath != null)
            return jarFilePath;
        // Step 2. Checking this Class location
        // This can fail for a remote URL, so it is used as a fallback only:
        jarFilePath = searchInContainingJar(pattern);
        if (jarFilePath != null)
            return jarFilePath;

        throw new IllegalStateException("No jar file with name " + nameRegExp + "found in the classpath");
    }

    private static String searchInClasspath(Pattern namePattern) {
        String[] classPath = System.getProperty("java.class.path").split(File.pathSeparator);
        for (String cpEntry : classPath) {
            if (namePattern.matcher(cpEntry).matches()) {
                return cpEntry;
            }
        }

        return null;
    }

    private static String searchInContainingJar(Pattern pattern) {
        CodeSource codeSource = AgentInitialization.class.getProtectionDomain().getCodeSource();
        if (codeSource == null) {
            return null;
        }

        URL location = codeSource.getLocation();
        String locationPath = location.getPath();
        File jarFile;

        if (locationPath.endsWith("/main/classes/")) {
            jarFile = findLocalJarOrZipFileFromLocationOfCurrentClassFile(locationPath, pattern);
        } else {
            jarFile = findJarFileContainingCurrentClass(location);
        }

        return jarFile.isFile() ? jarFile.getPath() : null;
    }

    private static File findLocalJarOrZipFileFromLocationOfCurrentClassFile(String locationPath, Pattern pattern) {
        File localJarDir = new File(locationPath.replace("main/classes/", ""));

        String[] fileNames = localJarDir.list();
        for(String fileName: fileNames) {
            if(pattern.matcher(fileName).matches())
                return new File(localJarDir, fileName);
            
        }

        return new File(locationPath.replace("classes/", "META-INF.zip"));
    }

    private static File findJarFileContainingCurrentClass(URL location) {
        try {
            // URI is needed to deal with spaces and non-ASCII characters.
            URI jarFileURI = location.toURI();
            return new File(jarFileURI);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
