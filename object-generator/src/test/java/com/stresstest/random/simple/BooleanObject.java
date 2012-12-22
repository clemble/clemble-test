package com.stresstest.random.simple;

public class BooleanObject {

    final private boolean booleanValue;
    private boolean anotherBooleanValue;
    private boolean publicBooleanValue;

    public BooleanObject(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public boolean isAnotherBooleanValue() {
        return anotherBooleanValue;
    }

    public boolean isPublicBooleanValue() {
        return publicBooleanValue;
    }

}
