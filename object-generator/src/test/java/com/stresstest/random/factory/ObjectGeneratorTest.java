package com.stresstest.random.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import junit.framework.Assert;

import org.junit.Test;

import com.clemble.test.random.ObjectGenerator;

@SuppressWarnings("all")
public class ObjectGeneratorTest {

    private class A {
        public boolean value;
    }

    @Test
    public void testCaching() {
        ObjectGenerator.enableCaching();
        Assert.assertEquals(ObjectGenerator.getValueGenerator(A.class), ObjectGenerator.getValueGenerator(A.class));
    }

    @Test
    public void testNoCaching() {
        ObjectGenerator.disableCaching();
        Assert.assertNotSame(ObjectGenerator.getValueGenerator(A.class), ObjectGenerator.getValueGenerator(A.class));
    }

    @Test(expected = InvocationTargetException.class)
    public void testClassSafe() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
        Constructor[] constructors = ObjectGenerator.class.getDeclaredConstructors();
        constructors[0].setAccessible(true);
        constructors[0].newInstance();
    }
}
