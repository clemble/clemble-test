package com.stresstest.jbehave.context;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.instrument.classloading.LoadTimeWeaver;

public class StoryContextClassFileTransformer implements LoadTimeWeaverAware, ClassFileTransformer {

	@Override
	public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {
		loadTimeWeaver.addTransformer(this);
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		return classfileBuffer;
	}

}
