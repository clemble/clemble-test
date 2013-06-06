/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package com.stresstest.jbehave.support.internal.startup;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

import sun.tools.attach.BsdVirtualMachine;
import sun.tools.attach.LinuxVirtualMachine;
import sun.tools.attach.SolarisVirtualMachine;
import sun.tools.attach.WindowsVirtualMachine;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;

final class JDK6AgentLoader {
    private static final AttachProvider ATTACH_PROVIDER = new AttachProvider() {
        @Override
        public String name() {
            return null;
        }

        @Override
        public String type() {
            return null;
        }

        @Override
        public VirtualMachine attachVirtualMachine(String id) {
            return null;
        }

        @Override
        public List<VirtualMachineDescriptor> listVirtualMachines() {
            return null;
        }
    };

    private final String filePath;
    private final String pid;

    JDK6AgentLoader(String filePath) {
        this.filePath = filePath;
        this.pid = discoverProcessIdForRunningVM();
    }

    private String discoverProcessIdForRunningVM() {
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        return nameOfRunningVM.substring(0, nameOfRunningVM.indexOf('@'));
    }

    boolean loadAgent() {
        VirtualMachine vm;

        if (AttachProvider.providers().isEmpty()) {
            vm = getVirtualMachineImplementationFromEmbeddedOnes();
        } else {
            vm = attachToThisVM();
        }

        if (vm != null) {
            loadAgentAndDetachFromThisVM(vm);
            return true;
        }

        return false;
    }

    private VirtualMachine getVirtualMachineImplementationFromEmbeddedOnes() {
        try {
            if (File.separatorChar == '\\') {
                return new WindowsVirtualMachine(ATTACH_PROVIDER, pid);
            }

            String osName = System.getProperty("os.name");
            if (osName.toUpperCase().startsWith("LINUX")) {
                return new LinuxVirtualMachine(ATTACH_PROVIDER, pid);
            } else if (osName.toUpperCase().startsWith("MAC OS X")) {
                return new BsdVirtualMachine(ATTACH_PROVIDER, pid);
            } else if (osName.toUpperCase().startsWith("SOLARIS")) {
                return new SolarisVirtualMachine(ATTACH_PROVIDER, pid);
            }
        } catch (AttachNotSupportedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsatisfiedLinkError e) {
            throw new IllegalStateException("Native library for Attach API not available in this JRE", e);
        }

        return null;
    }

    private VirtualMachine attachToThisVM() {
        try {
            return VirtualMachine.attach(pid);
        } catch (AttachNotSupportedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAgentAndDetachFromThisVM(VirtualMachine vm) {
        try {
            vm.loadAgentPath(filePath);
            vm.detach();
        } catch (AgentLoadException e) {
            throw new RuntimeException(e);
        } catch (AgentInitializationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
