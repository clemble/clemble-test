package com.stresstest.random.construction.external;

abstract class DefaultAbstractBuilderBasedClass {

    abstract Boolean getData();

    static class DefaultBuilderBasedClassBuilder {
        boolean data;

        DefaultAbstractBuilderBasedClass build() {
            return new DefaultAbstractBuilderBasedClass() {
                @Override
                Boolean getData() {
                    return data;
                }
            };
        }
    }

    static DefaultBuilderBasedClassBuilder create() {
        return new DefaultBuilderBasedClassBuilder();
    }
    
    static DefaultBuilderBasedClassBuilder create(DefaultAbstractBuilderBasedClass basedClass) {
        DefaultBuilderBasedClassBuilder builder = new DefaultBuilderBasedClassBuilder();
        builder.data = basedClass.getData();
        return builder;
    }

}
