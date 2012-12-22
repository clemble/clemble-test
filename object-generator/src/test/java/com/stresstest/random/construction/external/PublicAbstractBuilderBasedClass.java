package com.stresstest.random.construction.external;


abstract public class PublicAbstractBuilderBasedClass {
    abstract Boolean getData();

    public static class PublicBuilderBasedClassBuilder {
        boolean data;
        PublicAbstractBuilderBasedClass build() {
            return new PublicAbstractBuilderBasedClass() {
                @Override
                Boolean getData() {
                    return data;
                }
            };
        }
    }

    public static PublicBuilderBasedClassBuilder create() {
        return new PublicBuilderBasedClassBuilder();
    }
}
