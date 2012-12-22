package com.stresstest.random.construction.external;

abstract class DefaultFactoryMethodClass {

    abstract public Boolean getData();

    static DefaultFactoryMethodClass create(final boolean value) {
        return new DefaultFactoryMethodClass() {
            @Override
            public Boolean getData() {
                return value;
            }
        };

    }
}
