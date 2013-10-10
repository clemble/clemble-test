package com.stresstest.random.simple;

import com.clemble.test.random.ObjectGenerator;

public class BooleanObjectTest extends AbstractSimpleObjectTest<BooleanObject> {

    public BooleanObjectTest() {
        super(BooleanObject.class);
        ObjectGenerator.enableCaching();
    }

}
