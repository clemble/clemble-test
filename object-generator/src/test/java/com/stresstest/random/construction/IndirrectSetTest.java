package com.stresstest.random.construction;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stresstest.random.ObjectGenerator;
import com.stresstest.random.generator.SequentialValueGenerator;

public class IndirrectSetTest {

    public static class A {

        private int value;

        public A() {
        }

        public int getValue() {
            return value;
        }

        public void setData(int value) {
            this.value = value;
        }
    }

    @Before
    public void setUp() {
        ObjectGenerator.disableCaching();
    }

    @After
    public void clean() {
        ObjectGenerator.enableCaching();
    }

    @Test
    public void testIndirrectSet() {
        A generatedObject = ObjectGenerator.generate(A.class);
        Assert.assertTrue(generatedObject.getValue() != 0);
    }

    @Test
    public void testWithPredefinedSetter() {
        ObjectGenerator.disableCaching();
        ObjectGenerator.register(A.class, "data", SequentialValueGenerator.constantValueGenerator(10));
        A generatedObject = ObjectGenerator.generate(A.class);
        Assert.assertTrue(generatedObject.getValue() == 10);
    }
}
