package com.stresstest.random.construction;

import org.junit.Assert;
import org.junit.Test;

import com.stresstest.random.ObjectGenerator;

public class BuilderBasedGenerationTest {

    public abstract static class PublicInterface {
        abstract public Boolean getData();
        public static class PublicInterfaceBuilder{
            private boolean data;
            public void setData(boolean newDataValue){
                this.data = newDataValue;
            }
            public PublicInterface build(){
                return new PublicInterface(){
                    public Boolean getData(){
                        return data;
                    }
                };
            }
        }
        public static PublicInterfaceBuilder create() {
            return new PublicInterfaceBuilder();
        }
    }

    @Test
    public void testPublicInitiation() {
        PublicInterface result = ObjectGenerator.generate(PublicInterface.class);
        Assert.assertNotNull(result);
    }

    @SuppressWarnings("unused")
    private abstract static class PrivateInterface {
        abstract public Boolean getData();
        private static class PrivateInterfaceBuilder{
            private boolean data;
            private void setData(boolean newDataValue){
                this.data = newDataValue;
            }
            public PrivateInterface build(){
                return new PrivateInterface(){
                    public Boolean getData(){
                        return data;
                    }
                };
            }
        }
        private static PrivateInterfaceBuilder create() {
            return new PrivateInterfaceBuilder();
        }
    }

    @Test
    public void testPrivateInitiation() {
        PrivateInterface result = ObjectGenerator.generate(PrivateInterface.class);
        Assert.assertNotNull(result);
    }

    abstract static class DefaultInterface {
        abstract public Boolean getData();
        static class DefaultInterfaceBuilder{
            private boolean data;
            void setData(boolean newDataValue){
                this.data = newDataValue;
            }
            DefaultInterface build(){
                return new DefaultInterface(){
                    public Boolean getData(){
                        return data;
                    }
                };
            }
        }
        static DefaultInterfaceBuilder create() {
            return new DefaultInterfaceBuilder();
        }
    }

    @Test
    public void testDefaultInitiation() {
        DefaultInterface result = ObjectGenerator.generate(DefaultInterface.class);
        Assert.assertNotNull(result);
    }

    protected abstract static class ProtectedInterface {
        abstract public Boolean getData();
        protected static class ProtectedInterfaceBuilder{
            private boolean data;
            protected void setData(boolean newDataValue){
                this.data = newDataValue;
            }
            protected ProtectedInterface build(){
                return new ProtectedInterface(){
                    public Boolean getData(){
                        return data;
                    }
                };
            }
        }
        protected static ProtectedInterfaceBuilder create() {
            return new ProtectedInterfaceBuilder();
        }
    }

    @Test
    public void testProtectedInitiation() {
        ProtectedInterface result = ObjectGenerator.generate(ProtectedInterface.class);
        Assert.assertNotNull(result);
    }
    
    protected abstract static class UnconstractableInterface {
        abstract public Boolean getData();
        protected static class UnconstractableInterfaceBuilder{
            private boolean data;
            protected void setData(boolean newDataValue){
                this.data = newDataValue;
            }
            protected ProtectedInterface build(){
                return new ProtectedInterface(){
                    public Boolean getData(){
                        return data;
                    }
                };
            }
        }
        protected static UnconstractableInterfaceBuilder create() {
            return new UnconstractableInterfaceBuilder();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testUnconstructableInitiation() {
        ObjectGenerator.generate(UnconstractableInterface.class);
    }
}
