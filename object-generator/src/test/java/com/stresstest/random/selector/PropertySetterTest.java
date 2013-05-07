package com.stresstest.random.selector;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stresstest.random.ObjectGenerator;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.constructor.ClassPropertySetter;
import com.stresstest.random.constructor.ClassConstructorSimple;
import com.stresstest.random.constructor.ClassPropertySetterRegistry;
import com.stresstest.random.constructor.ClassValueGenerator;
import com.stresstest.random.generator.RandomValueGenerator;
import com.stresstest.random.generator.SequentialValueGenerator;

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
    	ObjectGenerator.register(A.class, "intValue", RandomValueGenerator.INTEGER_VALUE_GENERATOR);
    	ObjectGenerator.register(C.class, "intValue", RandomValueGenerator.INTEGER_VALUE_GENERATOR);
    	ObjectGenerator.register(D.class, "intValue", RandomValueGenerator.INTEGER_VALUE_GENERATOR);

        D randomValue = ObjectGenerator.generate(D.class);
        Assert.assertNotSame(randomValue.getIntValue(), ((C) randomValue).getIntValue());
        Assert.assertNotSame(((C) randomValue).getIntValue(), ((B) randomValue).getIntValue());
        Assert.assertNotSame(((B) randomValue).getIntValue(), ((A) randomValue).getIntValue());

        Assert.assertNotSame(randomValue.getDoubleValue(), ((C) randomValue).getDoubleValue());
        Assert.assertNotSame(((C) randomValue).getDoubleValue(), ((B) randomValue).getDoubleValue());

        Assert.assertNotSame(randomValue.getLongValue(), ((C) randomValue).getLongValue());
        ClassValueGenerator<D> classValueGenerator = (ClassValueGenerator<D>) ObjectGenerator.getValueGenerator(D.class);
        Assert.assertTrue(classValueGenerator.getObjectConstructor() instanceof ClassConstructorSimple);
        ClassPropertySetter<D> propertySetter = classValueGenerator.getPropertySetter();
    }

    @Test
    public void testFixedValueGeneration() {
    	ObjectGenerator.register(A.class, "intValue", SequentialValueGenerator.constantValueGenerator(10));
    	ObjectGenerator.register(C.class, "intValue", SequentialValueGenerator.constantValueGenerator(20));
    	ObjectGenerator.register(D.class, "intValue", SequentialValueGenerator.constantValueGenerator(30));

    	ObjectGenerator.register(B.class, "doubleValue", SequentialValueGenerator.constantValueGenerator(40.0));
    	ObjectGenerator.register(D.class, "doubleValue", SequentialValueGenerator.constantValueGenerator(50.0));

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

        ObjectGenerator.register(A.class, "intValue", SequentialValueGenerator.constantValueGenerator(60));
        ObjectGenerator.register(C.class, "intValue", SequentialValueGenerator.constantValueGenerator(70));
        ObjectGenerator.register(D.class, "intValue", SequentialValueGenerator.constantValueGenerator(80));

        ObjectGenerator.register(B.class, "doubleValue", SequentialValueGenerator.constantValueGenerator(90.0));
        ObjectGenerator.register(D.class, "doubleValue", SequentialValueGenerator.constantValueGenerator(100.0));

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
