package com.stresstest.random;

public interface ValueGeneratorFactory {

    public <T> ValueGenerator<T> getValueGenerator(Class<T> klass);

}
