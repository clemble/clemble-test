package com.stresstest.random.construction.external;

class DefaultInterfaceImpl implements DefaultInterface<Boolean> {

    final private boolean data;

    public DefaultInterfaceImpl(boolean dataValue) {
        this.data = dataValue;
    }

    @Override
    public Boolean getData() {
        return data;
    }

}
