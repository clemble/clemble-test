package com.stresstest.random.construction.external;

import org.junit.Assert;

import org.junit.Test;

import com.clemble.test.random.ObjectGenerator;
import com.clemble.test.random.ValueGenerator;
import com.clemble.test.random.ValueGeneratorFactory;
import com.clemble.test.random.constructor.ClassConstructorBuilder;
import com.clemble.test.random.constructor.ClassValueGenerator;
import com.clemble.test.random.generator.RandomValueGeneratorFactory;

public class DefaultBasedGenerationTest {

    final private ValueGeneratorFactory valueGeneratorFactory = new RandomValueGeneratorFactory();

    @Test
    public void testDefaultInterfaceCreation() {
        DefaultInterface<?> defaultInterface = ObjectGenerator.generate(DefaultInterface.class);
        Assert.assertTrue(defaultInterface instanceof DefaultInterfaceImpl);
    }

    @Test
    public void testDefaultAbstractClassCreation() {
        DefaultAbstractClass<?> defaultAbstractClass = ObjectGenerator.generate(DefaultAbstractClass.class);
        Assert.assertTrue(defaultAbstractClass instanceof DefaultAbstractClassImpl);
    }

    @Test
    public void testDefaultFactoryMethodClassCreation() {
        DefaultFactoryMethodClass defaultFactoryMethodClass = ObjectGenerator.generate(DefaultFactoryMethodClass.class);
        Assert.assertNotNull(defaultFactoryMethodClass);
    }

    @Test
    public void testDefaultBuilderBasedClassCreation() {
        DefaultAbstractBuilderBasedClass defaultBuilderBasedClass = ObjectGenerator.generate(DefaultAbstractBuilderBasedClass.class);
        Assert.assertNotNull(defaultBuilderBasedClass);
    }

    @Test
    public void testDefaultFactoryConstructorUsed() {
        ValueGenerator<DefaultBuilderBasedClass> factoryGenerator = valueGeneratorFactory.getValueGenerator(DefaultBuilderBasedClass.class);
        ClassValueGenerator<DefaultBuilderBasedClass> classValueGenerator = (ClassValueGenerator<DefaultBuilderBasedClass>) factoryGenerator;
        Assert.assertTrue(classValueGenerator.getObjectConstructor() instanceof ClassConstructorBuilder);
    }

}
