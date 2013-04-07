package com.stresstest.random.factory;

import junit.framework.Assert;

import org.junit.Test;

import com.stresstest.random.generator.RandomValueGenerator;

public class RandomValueGeneratorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringValueGeneratorException() {
        RandomValueGenerator.randomString(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphabeticStringValueGeneratorException() {
        RandomValueGenerator.randomAlphabeticString(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAsciiStringValueGeneratorException() {
        RandomValueGenerator.randomAsciiString(-2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphanumericStringValueGeneratorException() {
        RandomValueGenerator.randomAlphanumericString(-3);
    }
    
    @Test
    public void testRandomStringValueGenerator() {
        String generatedValue = RandomValueGenerator.randomString(20).generate();
        Assert.assertEquals(generatedValue.length(), 20);
    }
    
    @Test
    public void testRandomAlphabeticStringValueGenerator() {
        String generatedValue = RandomValueGenerator.randomAlphabeticString(30).generate();
        Assert.assertEquals(generatedValue.length(), 30);
    }
    
    @Test
    public void testRandomAsciiStringValueGenerator() {
        String generatedValue = RandomValueGenerator.randomAsciiString(40).generate();
        Assert.assertEquals(generatedValue.length(), 40);
    }
    
    @Test
    public void testRandomAlphanumericStringValueGenerator() {
        String generatedValue = RandomValueGenerator.randomAlphanumericString(50).generate();
        Assert.assertEquals(generatedValue.length(), 50);
    }
}
