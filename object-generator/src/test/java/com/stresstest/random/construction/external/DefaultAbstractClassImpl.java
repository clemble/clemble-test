package com.stresstest.random.construction.external;

class DefaultAbstractClassImpl extends DefaultAbstractClass<Boolean>{

    final private boolean data;
    
    DefaultAbstractClassImpl(boolean dataValue) {
        this.data = dataValue;
    }
    
    @Override
    Boolean getData() {
        return data;
    }

}
