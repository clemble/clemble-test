package com.stresstest.random.construction.external;

public class UncreatableObject {

    private UncreatableObject() {
        throw new IllegalAccessError();
    }

}
