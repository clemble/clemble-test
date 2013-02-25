package com.stresstest.random.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("all")
public class A {

    private int intValue;
    
    private Collection<String> stringCollections = new ArrayList<String>();

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public Collection<String> getStringCollections() {
        return stringCollections;
    }

    public void setStringCollections(Collection<String> stringCollections) {
        throw new IllegalAccessError();
    }
    
    public void addStringCollection(String value){
        stringCollections.add(value);
    }
}
