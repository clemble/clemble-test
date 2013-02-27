package com.stresstest.jbehave.support.internal;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import com.stresstest.jbehave.context.StoryContextAware;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class StoryContextClassFileTransformer implements ClassFileTransformer {

	public static ClassFileTransformer INSTANCE = new StoryContextClassFileTransformer();

	final private String[] exludedPackages = new String[] { "javassist", "org/junit", "org/spring", "java", "net", "sun" };

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		ClassPool pool = ClassPool.getDefault();
		CtClass cl = null;
		
		for(String packageName: exludedPackages)
			if(className.startsWith(packageName))
				return classfileBuffer;

		try {
			cl = pool.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));

			if (!cl.isInterface()) {

				CtField field = CtField.make("public String _storyContext;", cl);
				cl.addField(field);

				cl.addInterface(ClassPool.getDefault().get(StoryContextAware.class.getName()));

				CtMethod getMethod = CtNewMethod.make("public String getStoryContextObject() { return _storyContext; }", cl);
				CtMethod setMethod = CtNewMethod.make(
						"public void setStoryContextObject(String name) { return _storyContext = name; }", cl);

				cl.addMethod(getMethod);
				cl.addMethod(setMethod);

				classfileBuffer = cl.toBytecode();
			}
		} catch (Exception e) {
			System.err.println("Could not instrument " + className + ", exception : " + e.getMessage());
		} finally {
			if (cl != null) {
				cl.detach();
			}
		}
		return classfileBuffer;
	}

}
