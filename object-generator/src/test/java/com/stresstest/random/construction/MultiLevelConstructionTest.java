package com.stresstest.random.construction;

import org.junit.Test;

import org.junit.Assert;

import com.clemble.test.random.ObjectGenerator;

@SuppressWarnings("unused")
public class MultiLevelConstructionTest {

    interface A {
        boolean getData();
    }

    interface B extends A {
    }

    private static class C implements B {
        final private boolean data;

        public C(final boolean value) {
            this.data = value;
        }

        @Override
        public boolean getData() {
            return data;
        }
    }

    interface D extends B {
    }

    @Test
    public void testMultiLevelCreation() {
        A generatedValue = ObjectGenerator.generate(A.class);
        Assert.assertTrue(generatedValue instanceof C);
    }

    @Test(expected = RuntimeException.class)
    public void testUncreatable() {
        D generatedValue = ObjectGenerator.generate(D.class);
    }
}
