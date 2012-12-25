package com.stresstest.random.construction;

import junit.framework.Assert;

import org.junit.Test;

import com.stresstest.random.ValueGenerator;
import com.stresstest.random.ValueGeneratorFactory;
import com.stresstest.random.impl.ClassConstructor.ConstructorBasedConstructor;
import com.stresstest.random.impl.ClassConstructor.FactoryMethodBasedConstructor;
import com.stresstest.random.impl.ClassValueGenerator;

public class ConstructionFactoryStructureTest {

    final private ValueGeneratorFactory valueGeneratorFactory = new ValueGeneratorFactory();

    private static class PrivateFactoryMethodClass {
        final private boolean data;

        private PrivateFactoryMethodClass(boolean dataValue) {
            this.data = dataValue;
        }

        private boolean getData() {
            return data;
        }

        private static class PrivateFactoryMethodClassBuilder {
            private boolean value;

            private PrivateFactoryMethodClass build() {
                return new PrivateFactoryMethodClass(value);
            }
        }

        private static PrivateFactoryMethodClass create(boolean value) {
            return new PrivateFactoryMethodClass(value);
        }

        private static PrivateFactoryMethodClass create(final PrivateFactoryMethodClass value) {
            return value;
        }

        private static PrivateFactoryMethodClassBuilder newBuilder() {
            return new PrivateFactoryMethodClassBuilder();
        }
    }

    @Test
    public void testPrivateFactoryConstructorUsed() {
        ValueGenerator<PrivateFactoryMethodClass> factoryGenerator = valueGeneratorFactory.getValueGenerator(PrivateFactoryMethodClass.class);
        ClassValueGenerator<PrivateFactoryMethodClass> classValueGenerator = (ClassValueGenerator<PrivateFactoryMethodClass>) factoryGenerator;
        Assert.assertTrue(classValueGenerator.getObjectConstructor() instanceof FactoryMethodBasedConstructor);
    }

    protected static class ProtectedFactoryMethodClass {
        final private boolean data;

        protected ProtectedFactoryMethodClass(boolean dataValue) {
            this.data = dataValue;
        }

        protected boolean getData() {
            return data;
        }

        protected static class ProtectedFactoryMethodClassBuilder {
            protected boolean value;

            protected ProtectedFactoryMethodClass build() {
                return new ProtectedFactoryMethodClass(value);
            }
        }

        protected static ProtectedFactoryMethodClass create(boolean value) {
            return new ProtectedFactoryMethodClass(value);
        }

        protected static ProtectedFactoryMethodClass create(final ProtectedFactoryMethodClass value) {
            return value;
        }

        protected static ProtectedFactoryMethodClassBuilder newBuilder() {
            return new ProtectedFactoryMethodClassBuilder();
        }
    }

    @Test
    public void testProtectedFactoryConstructorUsed() {
        ValueGenerator<ProtectedFactoryMethodClass> factoryGenerator = valueGeneratorFactory.getValueGenerator(ProtectedFactoryMethodClass.class);
        ClassValueGenerator<ProtectedFactoryMethodClass> classValueGenerator = (ClassValueGenerator<ProtectedFactoryMethodClass>) factoryGenerator;
        Assert.assertTrue(classValueGenerator.getObjectConstructor() instanceof FactoryMethodBasedConstructor);
    }

    static class DefaultFactoryMethodClass {
        final private boolean data;

        protected DefaultFactoryMethodClass(boolean dataValue) {
            this.data = dataValue;
        }

        boolean getData() {
            return data;
        }

        static class DefaultFactoryMethodClassBuilder {
            protected boolean value;

            protected DefaultFactoryMethodClass build() {
                return new DefaultFactoryMethodClass(value);
            }
        }

        static DefaultFactoryMethodClass create(boolean value) {
            return new DefaultFactoryMethodClass(value);
        }
        
        static DefaultFactoryMethodClass create(final DefaultFactoryMethodClass value) {
            return value;
        }

        static DefaultFactoryMethodClassBuilder newBuilder() {
            return new DefaultFactoryMethodClassBuilder();
        }
    }

    @Test
    public void testDefaultFactoryConstructorUsed() {
        ValueGenerator<DefaultFactoryMethodClass> factoryGenerator = valueGeneratorFactory.getValueGenerator(DefaultFactoryMethodClass.class);
        ClassValueGenerator<DefaultFactoryMethodClass> classValueGenerator = (ClassValueGenerator<DefaultFactoryMethodClass>) factoryGenerator;
        Assert.assertTrue(classValueGenerator.getObjectConstructor() instanceof FactoryMethodBasedConstructor);
    }

    public static class PublicFactoryMethodClass {
        final private boolean data;

        private PublicFactoryMethodClass(boolean dataValue) {
            this.data = dataValue;
        }

        public boolean getData() {
            return data;
        }

        public static class PublicFactoryMethodClassBuilder {
            private boolean value;

            public PublicFactoryMethodClass build() {
                return new PublicFactoryMethodClass(value);
            }
        }

        public static PublicFactoryMethodClass create(boolean value) {
            return new PublicFactoryMethodClass(value);
        }
        
        public static PublicFactoryMethodClass create(PublicFactoryMethodClass value) {
            return value;
        }

        public static PublicFactoryMethodClassBuilder newBuilder() {
            return new PublicFactoryMethodClassBuilder();
        }
    }

    @Test
    public void testPublicFactoryConstructorUsed() {
        ValueGenerator<PublicFactoryMethodClass> factoryGenerator = valueGeneratorFactory.getValueGenerator(PublicFactoryMethodClass.class);
        ClassValueGenerator<PublicFactoryMethodClass> classValueGenerator = (ClassValueGenerator<PublicFactoryMethodClass>) factoryGenerator;
        Assert.assertTrue(classValueGenerator.getObjectConstructor() instanceof FactoryMethodBasedConstructor);
    }

    private static class TwoParametersClass {

        final private int numberValue;
        final private boolean booleanValue;

        private TwoParametersClass() {
            this(10);
        }

        private TwoParametersClass(final int number) {
            this(number, false);
        }

        private TwoParametersClass(final int number, final boolean booleanValue) {
            this.numberValue = number;
            this.booleanValue = booleanValue;
        }
    }

    @Test
    public void testMostParametersUsed() {
        ValueGenerator<TwoParametersClass> factoryGenerator = valueGeneratorFactory.getValueGenerator(TwoParametersClass.class);
        ClassValueGenerator<TwoParametersClass> classValueGenerator = (ClassValueGenerator<TwoParametersClass>) factoryGenerator;
        Assert.assertTrue(classValueGenerator.getObjectConstructor() instanceof ConstructorBasedConstructor);
        Assert.assertNotNull(factoryGenerator.generate());
        ConstructorBasedConstructor<?> constructor = (ConstructorBasedConstructor<?>) classValueGenerator.getObjectConstructor();
        Assert.assertEquals(constructor.getConstructor().getParameterTypes().length, 2);
    }
}
