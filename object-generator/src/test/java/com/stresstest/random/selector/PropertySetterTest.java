package com.stresstest.random.selector;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.stresstest.random.ObjectGenerator;
import com.stresstest.random.PropertySetter;
import com.stresstest.random.impl.ClassConstructor.ConstructorBasedConstructor;
import com.stresstest.random.impl.ClassPropertySetter;
import com.stresstest.random.impl.ClassValueGenerator;
import com.stresstest.random.impl.ConstantValueGenerator;
import com.stresstest.random.impl.IntegerValueGenerator;

public class PropertySetterTest {

    @Test
    public void testRandomGeneration() {
        PropertySetter.register(A.class, "intValue", new IntegerValueGenerator());
        PropertySetter.register(C.class, "intValue", new IntegerValueGenerator());
        PropertySetter.register(D.class, "intValue", new IntegerValueGenerator());

        D randomValue = ObjectGenerator.generate(D.class);
        Assert.assertNotSame(randomValue.getIntValue(), ((C) randomValue).getIntValue());
        Assert.assertNotSame(((C) randomValue).getIntValue(), ((B) randomValue).getIntValue());
        Assert.assertNotSame(((B) randomValue).getIntValue(), ((A) randomValue).getIntValue());

        Assert.assertNotSame(randomValue.getDoubleValue(), ((C) randomValue).getDoubleValue());
        Assert.assertNotSame(((C) randomValue).getDoubleValue(), ((B) randomValue).getDoubleValue());

        Assert.assertNotSame(randomValue.getLongValue(), ((C) randomValue).getLongValue());
        ClassValueGenerator<D> classValueGenerator = (ClassValueGenerator<D>) ObjectGenerator.getValueGenerator(D.class);
        Assert.assertTrue(classValueGenerator.getObjectConstructor() instanceof ConstructorBasedConstructor);
        ClassPropertySetter<D> propertySetter = classValueGenerator.getPropertySetter();
    }

    @Test
    public void testFixedValueGeneration() {
        PropertySetter.register(A.class, "intValue", new ConstantValueGenerator(10));
        PropertySetter.register(C.class, "intValue", new ConstantValueGenerator(20));
        PropertySetter.register(D.class, "intValue", new ConstantValueGenerator(30));

        A randomAValue = ObjectGenerator.generate(A.class);
        B randomBValue = ObjectGenerator.generate(B.class);
        C randomCValue = ObjectGenerator.generate(C.class);
        D randomDValue = ObjectGenerator.generate(D.class);

        Assert.assertEquals("A did not match", randomAValue.getIntValue(), 10);
        Assert.assertEquals("B did not match", randomBValue.getIntValue(), 10);
        Assert.assertEquals("C did not match", randomCValue.getIntValue(), 20);
//        Assert.assertEquals("D did not match", randomDValue.getIntValue(), 30);
    }

}
