package com.stresstest.random.construction.external;


class DefaultBuilderBasedClass {
    final private boolean data;

    protected DefaultBuilderBasedClass(boolean dataValue) {
        this.data = dataValue;
    }

    boolean getData() {
        return data;
    }

    static class DefaultBuilderBasedClassBuilder {
        protected boolean value;
        public DefaultBuilderBasedClass build(){
            return new DefaultBuilderBasedClass(value);
        }
    }
    
    private static DefaultBuilderBasedClass create(boolean value) {
        return new DefaultBuilderBasedClass(value);
    }
    
    public static DefaultBuilderBasedClassBuilder newBuilder() {
        return new DefaultBuilderBasedClassBuilder();
    }
    
    public static DefaultBuilderBasedClassBuilder newBuilder(boolean value) {
        DefaultBuilderBasedClassBuilder builder = new DefaultBuilderBasedClassBuilder();
        builder.value = value;
        return builder;
    }
}

