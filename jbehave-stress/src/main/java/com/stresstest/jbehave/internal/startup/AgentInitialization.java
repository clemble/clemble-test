/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package com.stresstest.jbehave.internal.startup;

final class AgentInitialization {

    static final Double javaSpecVersion = Double.valueOf(System.getProperty("java.specification.version"));

    boolean initializeAccordingToJDKVersion() {
        String jarFilePath = JarSearchUtils.discoverPathToJarFile(".*jmockit[-.\\d]*.jar");

        if (javaSpecVersion >= 1.6) {
            return new JDK6AgentLoader(jarFilePath).loadAgent();
        } else {
            throw new IllegalStateException("Unable to initialize, check that your Java 5 VM has been started with the -javaagent:" + jarFilePath + " command line option.");
        }
    }

}
