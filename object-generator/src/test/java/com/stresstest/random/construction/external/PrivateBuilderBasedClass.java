package com.stresstest.random.construction.external;

public class PrivateBuilderBasedClass {
    final private boolean data;

    private PrivateBuilderBasedClass(boolean dataValue) {
        this.data = dataValue;
    }

    boolean getData() {
        return data;
    }

    static class PrivateBuilderBasedClassBuilder {
        protected boolean value;

        public PrivateBuilderBasedClass build() {
            return new PrivateBuilderBasedClass(value);
        }
    }

    @SuppressWarnings("unused")
    private static PrivateBuilderBasedClassBuilder newBuilder() {
        return new PrivateBuilderBasedClassBuilder();
    }

    @SuppressWarnings("unused")
    private static PrivateBuilderBasedClassBuilder newBuilder(PrivateBuilderBasedClass builderBasedClass) {
        PrivateBuilderBasedClassBuilder builder = new PrivateBuilderBasedClassBuilder();
        builder.value = builderBasedClass.data;
        return builder;
    }

}
