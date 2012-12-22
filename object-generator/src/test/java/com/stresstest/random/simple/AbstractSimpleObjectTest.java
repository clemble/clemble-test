package com.stresstest.random.simple;

import junit.framework.Assert;

import org.junit.Test;

import com.stresstest.random.ObjectGenerator;

abstract public class AbstractSimpleObjectTest<T> {
    
    final private static int DEFAULT_NUMBER_OF_TESTS = 100000;
    
    final private Class<T> classToGenerate;
    final private int numTests;
    
    public AbstractSimpleObjectTest(final Class<T> classToGenerate) {
        this(classToGenerate, DEFAULT_NUMBER_OF_TESTS);
    }
    
    public AbstractSimpleObjectTest(final Class<T> classToGenerate, final int numTests) {
        this.classToGenerate = classToGenerate;
        this.numTests = numTests;
    }

    @Test
    public void testRandomValueGenerated() {
        for (int i = 0; i < numTests; i++) {
            T oneObject = ObjectGenerator.generate(classToGenerate);
            T anotherObject = ObjectGenerator.generate(classToGenerate);
            Assert.assertNotSame(oneObject, anotherObject);
        }
    }

}
