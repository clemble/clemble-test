package com.stresstest.jbehave.support.internal.startup;

import java.io.File;
import java.util.regex.Pattern;

public class JarSearchUtils {

    public static String discoverPathToJarFile(String nameRegExp) {
        Pattern pattern = Pattern.compile(nameRegExp);
        // Step 1. Checking classpath
        String jarFilePath = searchInClasspath(pattern);
        if (jarFilePath != null)
            return jarFilePath;

        throw new IllegalStateException("No jar file with name " + nameRegExp + " found in the classpath");
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

}
