package com.stresstest.random.simple;

import com.stresstest.random.ObjectGenerator;

public class BooleanObjectTest extends AbstractSimpleObjectTest<BooleanObject> {

    public BooleanObjectTest() {
        super(BooleanObject.class);
        ObjectGenerator.enableCaching();
    }

}
