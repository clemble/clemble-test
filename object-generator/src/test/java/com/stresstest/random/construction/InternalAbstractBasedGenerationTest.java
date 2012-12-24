package com.stresstest.random.construction;

import org.junit.Assert;
import org.junit.Test;

import com.stresstest.random.ObjectGenerator;

public class InternalAbstractBasedGenerationTest {

    public abstract class PublicAbstractInterface<T> {
        abstract public T getData();
    }

    public class PublicAbstractClassImpl extends PublicAbstractInterface<Boolean> {
        final public boolean data;
        public PublicAbstractClassImpl(boolean data) {
            this.data = data;
        }
        public PublicAbstractClassImpl(PublicAbstractClassImpl data) {
            this.data = data.data;
        }
        @Override
        public Boolean getData() {
            return data;
        }
    }

    @Test
    public void testPublicInitiation() {
        PublicAbstractInterface<?> result = ObjectGenerator.generate(PublicAbstractInterface.class);
        Assert.assertTrue(result instanceof PublicAbstractClassImpl);
    }

    private abstract class PrivateAbstractClass<T> {
        abstract public T getData();
    }

    private class PrivateAbstractCassImpl extends PrivateAbstractClass<Boolean> {
        final public boolean data;
        private PrivateAbstractCassImpl(boolean data) {
            this.data = data;
        }
        private PrivateAbstractCassImpl(PrivateAbstractCassImpl data) {
            this.data = data.data;
        }
        @Override
        public Boolean getData() {
            return data;
        }
    }

    @Test
    public void testPrivateInitiation() {
        PrivateAbstractClass<?> result = ObjectGenerator.generate(PrivateAbstractClass.class);
        Assert.assertTrue(result instanceof PrivateAbstractCassImpl);
    }

    abstract class DefaultAbstractClass<T> {
        abstract public T getData();
    }
    class DefaultAbstractClassImplementation extends DefaultAbstractClass<Boolean> {
        final private boolean data;
        DefaultAbstractClassImplementation(boolean dataValue) {
            this.data = dataValue;
        }
        DefaultAbstractClassImplementation(DefaultAbstractClassImplementation dataValue) {
            this.data = dataValue.data;
        }
        @Override
        public Boolean getData() {
            return data;
        }
    }
    
    @Test
    public void testDefaultInitiation() {
        DefaultAbstractClass<?> result = ObjectGenerator.generate(DefaultAbstractClass.class);
        Assert.assertTrue(result instanceof DefaultAbstractClassImplementation);
    }
    
    protected abstract class ProtectedAbstractClass<T> {
        abstract public T getData();
    }
    protected class ProtectedAbstractClassImpl extends ProtectedAbstractClass<Boolean> {
        final private boolean data;
        protected ProtectedAbstractClassImpl(boolean dataValue){
            this.data = dataValue;
        }
        protected ProtectedAbstractClassImpl(ProtectedAbstractClassImpl dataValue){
            this.data = dataValue.data;
        }
        public Boolean getData() {
            return data;
        }
    }

    @Test
    public void testProtectedInitiation() {
        ProtectedAbstractClass<?> result = ObjectGenerator.generate(ProtectedAbstractClass.class);
        Assert.assertTrue(result instanceof ProtectedAbstractClassImpl);
    }

}
