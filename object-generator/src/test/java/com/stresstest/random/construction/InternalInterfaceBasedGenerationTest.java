package com.stresstest.random.construction;

import org.junit.Assert;
import org.junit.Test;

import com.clemble.test.random.ObjectGenerator;

public class InternalInterfaceBasedGenerationTest {

    public interface PublicInterface<T> {
        public T getData();
    }

    public class PublicImplementation implements PublicInterface<Boolean> {
        final public boolean data;

        public PublicImplementation(boolean data) {
            this.data = data;
        }

        public PublicImplementation(PublicImplementation data) {
            this.data = data.data;
        }

        @Override
        public Boolean getData() {
            return data;
        }

    }

    @Test
    public void testPublicInitiation() {
        PublicInterface<?> result = ObjectGenerator.generate(PublicInterface.class);
        Assert.assertTrue(result instanceof PublicImplementation);
    }

    private interface PrivateInterface<T> {
        public T getData();
    }

    private class PrivateImplementation implements PrivateInterface<Boolean> {
        final public boolean data;

        private PrivateImplementation(boolean data) {
            this.data = data;
        }

        private PrivateImplementation(PrivateImplementation data) {
            this.data = data.data;
        }

        @Override
        public Boolean getData() {
            return data;
        }

    }

    @Test
    public void testPrivateInitiation() {
        PrivateInterface<?> result = ObjectGenerator.generate(PrivateInterface.class);
        Assert.assertTrue(result instanceof PrivateImplementation);
    }

    interface DefaultInterface<T> {
        public T getData();
    }

    class DefaultImplementation implements DefaultInterface<Boolean> {
        final private boolean data;

        DefaultImplementation(boolean dataValue) {
            this.data = dataValue;
        }

        DefaultImplementation(DefaultImplementation dataValue) {
            this.data = dataValue.data;
        }

        @Override
        public Boolean getData() {
            return data;
        }

    }

    @Test
    public void testDefaultInitiation() {
        DefaultInterface<?> result = ObjectGenerator.generate(DefaultInterface.class);
        Assert.assertTrue(result instanceof DefaultImplementation);
    }

    protected interface ProtectedInterface<T> {
        public T getData();
    }

    protected class ProtectedImplementation implements ProtectedInterface<Boolean> {
        final private boolean data;

        protected ProtectedImplementation(boolean dataValue) {
            this.data = dataValue;
        }

        protected ProtectedImplementation(ProtectedImplementation dataValue) {
            this.data = dataValue.data;
        }

        public Boolean getData() {
            return data;
        }
    }

    @Test
    public void testProtectedInitiation() {
        ProtectedInterface<?> result = ObjectGenerator.generate(ProtectedInterface.class);
        Assert.assertTrue(result instanceof ProtectedImplementation);
    }
}
