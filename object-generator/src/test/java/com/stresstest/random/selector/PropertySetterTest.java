package com.stresstest.random.selector;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stresstest.random.ObjectGenerator;
import com.stresstest.random.ObjectValueGenerator;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.constructor.ClassPropertySetter;
import com.stresstest.random.constructor.ClassConstructorSimple;
import com.stresstest.random.generator.RandomValueGenerator;

@SuppressWarnings("all")
public class PropertySetterTest {
    @Before
    public void setUp() {
        ObjectGenerator.disableCaching();
    }

    @After
    public void clean() {
        ObjectGenerator.enableCaching();
    }

    @Test
    public void testRandomGeneration() {
        ClassPropertySetter.register(A.class, "intValue", RandomValueGenerator.INTEGER_VALUE_GENERATOR);
        ClassPropertySetter.register(C.class, "intValue", RandomValueGenerator.INTEGER_VALUE_GENERATOR);
        ClassPropertySetter.register(D.class, "intValue", RandomValueGenerator.INTEGER_VALUE_GENERATOR);

        D randomValue = ObjectGenerator.generate(D.class);
        Assert.assertNotSame(randomValue.getIntValue(), ((C) randomValue).getIntValue());
        Assert.assertNotSame(((C) randomValue).getIntValue(), ((B) randomValue).getIntValue());
        Assert.assertNotSame(((B) randomValue).getIntValue(), ((A) randomValue).getIntValue());

        Assert.assertNotSame(randomValue.getDoubleValue(), ((C) randomValue).getDoubleValue());
        Assert.assertNotSame(((C) randomValue).getDoubleValue(), ((B) randomValue).getDoubleValue());

        Assert.assertNotSame(randomValue.getLongValue(), ((C) randomValue).getLongValue());
        ObjectValueGenerator<D> classValueGenerator = (ObjectValueGenerator<D>) ObjectGenerator.getValueGenerator(D.class);
        Assert.assertTrue(classValueGenerator.getObjectConstructor() instanceof ClassConstructorSimple);
        ClassPropertySetter<D> propertySetter = classValueGenerator.getPropertySetter();
    }

    @Test
    public void testFixedValueGeneration() {
        ClassPropertySetter.register(A.class, "intValue", ValueGenerator.constantValueGenerator(10));
        ClassPropertySetter.register(C.class, "intValue", ValueGenerator.constantValueGenerator(20));
        ClassPropertySetter.register(D.class, "intValue", ValueGenerator.constantValueGenerator(30));

        ClassPropertySetter.register(B.class, "doubleValue", ValueGenerator.constantValueGenerator(40.0));
        ClassPropertySetter.register(D.class, "doubleValue", ValueGenerator.constantValueGenerator(50.0));

        A randomAValue = ObjectGenerator.generate(A.class);
        B randomBValue = ObjectGenerator.generate(B.class);
        C randomCValue = ObjectGenerator.generate(C.class);
        D randomDValue = ObjectGenerator.generate(D.class);

        Assert.assertEquals("A integer did not match", randomAValue.getIntValue(), 10);
        Assert.assertEquals("B integer did not match", randomBValue.getIntValue(), 10);
        Assert.assertEquals("B double did not match", randomBValue.getDoubleValue(), 40.0);
        Assert.assertEquals("C integer did not match", randomCValue.getIntValue(), 20);
        Assert.assertEquals("C double did not match", randomCValue.getDoubleValue(), 40.0);
        Assert.assertEquals("D integer did not match", randomDValue.getIntValue(), 30);
        Assert.assertEquals("D double did not match", randomDValue.getDoubleValue(), 50.0);

        ClassPropertySetter.register(A.class, "intValue", ValueGenerator.constantValueGenerator(60));
        ClassPropertySetter.register(C.class, "intValue", ValueGenerator.constantValueGenerator(70));
        ClassPropertySetter.register(D.class, "intValue", ValueGenerator.constantValueGenerator(80));

        ClassPropertySetter.register(B.class, "doubleValue", ValueGenerator.constantValueGenerator(90.0));
        ClassPropertySetter.register(D.class, "doubleValue", ValueGenerator.constantValueGenerator(100.0));

        randomAValue = ObjectGenerator.generate(A.class);
        randomBValue = ObjectGenerator.generate(B.class);
        randomCValue = ObjectGenerator.generate(C.class);
        randomDValue = ObjectGenerator.generate(D.class);

        Assert.assertEquals("A integer did not match", randomAValue.getIntValue(), 60);
        Assert.assertEquals("B integer did not match", randomBValue.getIntValue(), 60);
        Assert.assertEquals("B double did not match", randomBValue.getDoubleValue(), 90.0);
        Assert.assertEquals("C integer did not match", randomCValue.getIntValue(), 70);
        Assert.assertEquals("C double did not match", randomCValue.getDoubleValue(), 90.0);
        Assert.assertEquals("D integer did not match", randomDValue.getIntValue(), 80);
        Assert.assertEquals("D double did not match", randomDValue.getDoubleValue(), 100.0);

    }

}
