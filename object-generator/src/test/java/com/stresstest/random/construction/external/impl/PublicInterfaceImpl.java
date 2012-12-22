package com.stresstest.random.construction.external.impl;

import com.stresstest.random.construction.external.PublicAbstractInterface;

public class PublicInterfaceImpl implements PublicAbstractInterface<Boolean> {
    final private boolean data;

    private PublicInterfaceImpl(boolean dataValue) {
        this.data = dataValue;
    }

    @Override
    public Boolean getData() {
        return data;
    }

}
