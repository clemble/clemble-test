package com.stresstest.random.construction.external.impl;

import com.stresstest.random.construction.external.PublicAbstractClass;

public class PublicAbstractClassImpl extends PublicAbstractClass<Boolean> {
    final private boolean data;

    private PublicAbstractClassImpl(boolean dataValue) {
        this.data = dataValue;
    }

    @Override
    public Boolean getData() {
        return data;
    }

}
