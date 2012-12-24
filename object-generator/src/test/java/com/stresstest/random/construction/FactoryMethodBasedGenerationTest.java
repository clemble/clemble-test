package com.stresstest.random.construction;

import org.junit.Assert;
import org.junit.Test;

import com.stresstest.random.ObjectGenerator;

public class FactoryMethodBasedGenerationTest {

    public abstract static class PublicAbstractFactoryMethodBasedClass {
        abstract public Boolean getData();

        public static PublicAbstractFactoryMethodBasedClass create(final boolean value) {
            return new PublicAbstractFactoryMethodBasedClass() {
                @Override
                public Boolean getData() {
                    return value;
                }
            };
        }
        
        public static PublicAbstractFactoryMethodBasedClass create(final PublicAbstractFactoryMethodBasedClass value) {
            return new PublicAbstractFactoryMethodBasedClass() {
                @Override
                public Boolean getData() {
                    return value.getData();
                }
            };
        }
    }

    @Test
    public void testPublicInitiation() {
        PublicAbstractFactoryMethodBasedClass result = ObjectGenerator.generate(PublicAbstractFactoryMethodBasedClass.class);
        Assert.assertNotNull(result);
    }

    private abstract static class PrivateAbstractFactoryMethodBasedClass {
        abstract public Boolean getData();

        @SuppressWarnings("unused")
        private static PrivateAbstractFactoryMethodBasedClass create(final boolean value) {
            return new PrivateAbstractFactoryMethodBasedClass() {
                @Override
                public Boolean getData() {
                    return value;
                }
            };
        }
        
        private static PrivateAbstractFactoryMethodBasedClass create(final PrivateAbstractFactoryMethodBasedClass value) {
            return new PrivateAbstractFactoryMethodBasedClass() {
                @Override
                public Boolean getData() {
                    return value.getData();
                }
            };
        }
    }

    @Test
    public void testPrivateInitiation() {
        PrivateAbstractFactoryMethodBasedClass result = ObjectGenerator.generate(PrivateAbstractFactoryMethodBasedClass.class);
        Assert.assertNotNull(result);
    }

    abstract static class DefaultAbstractFactoryMethodBasedClass<T> {
        abstract public T getData();

        static DefaultAbstractFactoryMethodBasedClass<Boolean> create(final boolean value) {
            return new DefaultAbstractFactoryMethodBasedClass<Boolean>() {
                @Override
                public Boolean getData() {
                    return value;
                }
            };
        }
        
        static DefaultAbstractFactoryMethodBasedClass<Boolean> create(final DefaultAbstractFactoryMethodBasedClass<Boolean> value) {
            return new DefaultAbstractFactoryMethodBasedClass<Boolean>() {
                @Override
                public Boolean getData() {
                    return value.getData();
                }
            };
        }
    }

    @Test
    public void testDefaultInitiation() {
        DefaultAbstractFactoryMethodBasedClass<?> result = ObjectGenerator.generate(DefaultAbstractFactoryMethodBasedClass.class);
        Assert.assertNotNull(result);
    }

    protected abstract static class ProtectedAbstractFactoryMethodBasedClass {
        abstract public Boolean getData();
        protected static ProtectedAbstractFactoryMethodBasedClass create(final boolean value) {
            return new ProtectedAbstractFactoryMethodBasedClass() {
                @Override
                public Boolean getData() {
                    return value;
                }
            };
        }
        protected static ProtectedAbstractFactoryMethodBasedClass create(final ProtectedAbstractFactoryMethodBasedClass value) {
            return new ProtectedAbstractFactoryMethodBasedClass() {
                @Override
                public Boolean getData() {
                    return value.getData();
                }
            };
        }
    }

    @Test
    public void testProtectedInitiation() {
        ProtectedAbstractFactoryMethodBasedClass result = ObjectGenerator.generate(ProtectedAbstractFactoryMethodBasedClass.class);
        Assert.assertNotNull(result);
    }

    public abstract static class UnconstructableAbstractFactoryMethodBasedClass {
        abstract public Boolean getData();
        public static UnconstructableAbstractFactoryMethodBasedClass create(final UnconstructableAbstractFactoryMethodBasedClass value) {
            return value;
        }
    }

    @Test(expected = RuntimeException.class)
    public void testUnconstractableClass() {
        UnconstructableAbstractFactoryMethodBasedClass result = ObjectGenerator.generate(UnconstructableAbstractFactoryMethodBasedClass.class);
    }
}
