package com.stresstest.random.factory;

import junit.framework.Assert;

import org.junit.Test;

import com.stresstest.random.ValueGenerator;

public class ValueGeneratorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringValueGeneratorException() {
        ValueGenerator.randomString(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphabeticStringValueGeneratorException() {
        ValueGenerator.randomAlphabeticString(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAsciiStringValueGeneratorException() {
        ValueGenerator.randomAsciiString(-2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphanumericStringValueGeneratorException() {
        ValueGenerator.randomAlphanumericString(-3);
    }
    
    
    
    @Test
    public void testRandomStringValueGenerator() {
        String generatedValue = ValueGenerator.randomString(20).generate();
        Assert.assertEquals(generatedValue.length(), 20);
    }
    
    @Test
    public void testRandomAlphabeticStringValueGenerator() {
        String generatedValue = ValueGenerator.randomAlphabeticString(30).generate();
        Assert.assertEquals(generatedValue.length(), 30);
    }
    
    @Test
    public void testRandomAsciiStringValueGenerator() {
        String generatedValue = ValueGenerator.randomAsciiString(40).generate();
        Assert.assertEquals(generatedValue.length(), 40);
    }
    
    @Test
    public void testRandomAlphanumericStringValueGenerator() {
        String generatedValue = ValueGenerator.randomAlphanumericString(50).generate();
        Assert.assertEquals(generatedValue.length(), 50);
    }
}
