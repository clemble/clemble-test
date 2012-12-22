package com.stresstest.random;

public class ObjectGenerator {

    final private static ValueGeneratorFactory STANDARD_VALUE_GENERATOR = new SimpleValueGeneratorFactory();

    final private static ValueGeneratorFactory CACHED_VALUE_GENERATOR = new CachedValueGeneratorFactory(STANDARD_VALUE_GENERATOR);

    private static ValueGeneratorFactory valueGeneratorFactory = STANDARD_VALUE_GENERATOR;

    public static void enableCaching() {
        valueGeneratorFactory = CACHED_VALUE_GENERATOR;
    }

    public static void disableCaching() {
        valueGeneratorFactory = STANDARD_VALUE_GENERATOR;
    }

    public static <T> T generate(Class<T> classToGenerate) {
        return getValueGenerator(classToGenerate).generate();
    }

    public static <T> ValueGenerator<T> getValueGenerator(Class<T> classToGenerate) {
        return valueGeneratorFactory.getValueGenerator(classToGenerate);
    }
}
