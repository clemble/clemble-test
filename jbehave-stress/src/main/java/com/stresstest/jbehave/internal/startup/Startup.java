/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package com.stresstest.jbehave.internal.startup;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * This is the "agent class" that initializes the JMockit "Java agent". It is not intended for use in client code.
 * It must be public, however, so the JVM can call the {@code premain} method, which as the name implies is called <em>before</em> the {@code main} method.
 * 
 * @see #premain(String, Instrumentation)
 */
public final class Startup {

    private static Instrumentation instrumentation;

    private Startup() {
    }

    /**
     * This method must only be called by the JVM, to provide the instrumentation object.
     * In order for this to occur, the JVM must be started with "-javaagent:jmockit.jar" as a command line parameter
     * (assuming the jar file is in the current directory).
     * <p/>
     * It is also possible to load other <em>instrumentation tools</em> at this time, by having set the "jmockit-tools" and/or "jmockit-mocks" system properties
     * in the JVM command line. There are two types of instrumentation tools:
     * <ol>
     * <li>A {@link ClassFileTransformer class file transformer}, which will be instantiated and added to the JVM instrumentation service. Such a class must
     * have a no-args constructor.</li>
     * <li>An <em>external mock</em>, which should be a {@code MockUp} subclass with a no-args constructor.
     * </ol>
     * 
     * @param agentArgs not used
     * @param inst the instrumentation service provided by the JVM
     */
    public static void premain(String agentArgs, Instrumentation inst) throws IOException {
        initialize(inst);
    }

    private static void initialize(Instrumentation inst) throws IOException {
        if (instrumentation == null) {
            instrumentation = inst;
            // inst.addTransformer(CachedClassfiles.INSTANCE);
        }
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws IOException {
        initialize(inst);
    }

    public static boolean initializeIfNeeded() {
        if (instrumentation == null) {
            return new AgentInitialization().initializeAccordingToJDKVersion();
        }

        return false;
    }

}
