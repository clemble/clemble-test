package com.stresstest.random.construction;

import org.junit.Assert;

import org.junit.Test;

import com.clemble.test.random.ObjectGenerator;
import com.clemble.test.random.ValueGenerator;
import com.clemble.test.random.constructor.ClassConstructorBuilder;
import com.clemble.test.random.constructor.ClassValueGenerator;
import com.stresstest.random.construction.external.PrivateBuilderBasedClass;
import com.stresstest.random.construction.external.PrivateClass;
import com.stresstest.random.construction.external.PublicAbstractBuilderBasedClass;
import com.stresstest.random.construction.external.PublicAbstractClass;
import com.stresstest.random.construction.external.PublicAbstractFactoryMethodClass;
import com.stresstest.random.construction.external.PublicAbstractInterface;
import com.stresstest.random.construction.external.Uncreatable;
import com.stresstest.random.construction.external.UncreatableInterface;
import com.stresstest.random.construction.external.UncreatableObject;
import com.stresstest.random.construction.external.impl.PublicAbstractClassImpl;
import com.stresstest.random.construction.external.impl.PublicInterfaceImpl;

public class ExternalPublicBasedGenerationTest {

    @Test
    public void testPublicInterfaceCreation() {
        PublicAbstractInterface<?> publicInterface = ObjectGenerator.generate(PublicAbstractInterface.class);
        Assert.assertTrue(publicInterface instanceof PublicInterfaceImpl);
    }

    @Test
    public void testPublicAbstractClassCreation() {
        PublicAbstractClass<?> publicInterface = ObjectGenerator.generate(PublicAbstractClass.class);
        Assert.assertTrue(publicInterface instanceof PublicAbstractClassImpl);
    }

    @Test
    public void testPublicFactoryClassCreation() {
        PublicAbstractFactoryMethodClass factoryMethodClass = ObjectGenerator.generate(PublicAbstractFactoryMethodClass.class);
        Assert.assertNotNull(factoryMethodClass);
    }
    
    @Test
    public void testPublicBuilderClassCreation() {
        PublicAbstractBuilderBasedClass publicBuilderBasedClass = ObjectGenerator.generate(PublicAbstractBuilderBasedClass.class);
        Assert.assertNotNull(publicBuilderBasedClass);
    }
    
    @Test
    public void testPrivateClassCreation() {
        PrivateClass privateClass = ObjectGenerator.generate(PrivateClass.class);
        Assert.assertNotNull(privateClass);
    }

    @Test
    public void testPrivateBuilderBasedClassCreation() {
        ValueGenerator<PrivateBuilderBasedClass> valueGenerator = ObjectGenerator.getValueGenerator(PrivateBuilderBasedClass.class);
        ClassValueGenerator<PrivateBuilderBasedClass> classValueGenerator = (ClassValueGenerator<PrivateBuilderBasedClass>) valueGenerator;
        Assert.assertTrue(classValueGenerator.getObjectConstructor() instanceof ClassConstructorBuilder);
        Assert.assertNotNull(valueGenerator.generate());
    }

    
    @Test(expected = RuntimeException.class)
    public void testUncreataleObject() {
        ObjectGenerator.generate(UncreatableObject.class);
    }

    @Test(expected = RuntimeException.class)
    public void testUncreatableInterface() {
        ObjectGenerator.generate(UncreatableInterface.class);
    }
    
    @Test(expected = RuntimeException.class)
    public void testUncreatable() {
        ObjectGenerator.generate(Uncreatable.class);
    }

}
