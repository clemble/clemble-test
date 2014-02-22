package com.clemble.test.random;

import com.clemble.test.random.constructor.ClassPropertySetterRegistry;
import com.clemble.test.random.generator.CachedValueGeneratorFactory;
import com.clemble.test.random.generator.RandomValueGeneratorFactory;

import java.util.ArrayList;
import java.util.List;

public class ObjectGenerator {

    final private static ClassPropertySetterRegistry SETTER_MANAGER = new ClassPropertySetterRegistry();

    final private static ValueGeneratorFactory DEFAULT_VALUE_GENERATOR = new RandomValueGeneratorFactory(SETTER_MANAGER);

    private static ValueGeneratorFactory valueGeneratorFactory = DEFAULT_VALUE_GENERATOR;

    private ObjectGenerator() {
        throw new IllegalAccessError();
    }

    public static <T> T generate(Class<T> classToGenerate) {
        return getValueGenerator(classToGenerate).generate();
    }

    public static <T> List<T> generateList(Class<T> classToGenerate) {
        return generateList(classToGenerate, 2);
    }

    public static <T> List<T> generateList(Class<T> classToGenerate, int num) {
        ValueGenerator<T> generator = getValueGenerator(classToGenerate);
        List<T> results = new ArrayList<T>();
        for(int i = 0; i < num; i++)
            results.add(generator.generate());
        return results;
    }

    public static <T> ValueGenerator<T> getValueGenerator(Class<T> classToGenerate) {
        return valueGeneratorFactory.getValueGenerator(classToGenerate);
    }

    public static <T, V> void register(final Class<T> searchClass, final String name, final ValueGenerator<V> valueGenerator) {
        SETTER_MANAGER.register(searchClass, name, valueGenerator);
    }

    public static <T> void register(final Class<T> klass, final ValueGenerator<T> valueGenerator) {
        valueGeneratorFactory.put(klass, valueGenerator);
    }

    public static <T> Iterable<T> getPossibleValues(final Class<T> targetClass) {
        return new PossibleValuesIterable<T>(getValueGenerator(targetClass));
    }

    public static void enableCaching() {
        valueGeneratorFactory = new CachedValueGeneratorFactory(DEFAULT_VALUE_GENERATOR);
    }

    public static void disableCaching() {
        valueGeneratorFactory = DEFAULT_VALUE_GENERATOR;
    }
}
