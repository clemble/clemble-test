package com.stresstest.random.construction.external;

abstract public class PublicAbstractFactoryMethodClass {
    
    abstract public boolean getData();

    public static PublicAbstractFactoryMethodClass create(final boolean value) {
        return new PublicAbstractFactoryMethodClass() {
            @Override
            public boolean getData() {
                return value;
            }
        };
    }
}
