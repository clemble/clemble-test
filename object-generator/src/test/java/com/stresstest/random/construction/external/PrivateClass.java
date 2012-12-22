package com.stresstest.random.construction.external;

public class PrivateClass {

    final private boolean data;

    private PrivateClass(final boolean dataValue) {
        this.data = dataValue;
    }

    public boolean getData() {
        return data;
    }
}
