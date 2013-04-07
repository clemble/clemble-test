package com.stresstest.random;

import com.stresstest.random.constructor.PropertySetterManager;
import com.stresstest.random.generator.CachedValueGeneratorFactory;
import com.stresstest.random.generator.RandomValueGeneratorFactory;
import com.stresstest.random.generator.ValueGeneratorFactory;

public class ObjectGenerator {
	
	final private static PropertySetterManager SETTER_MANAGER = new PropertySetterManager();
	
    final private static ValueGeneratorFactory STANDARD_VALUE_GENERATOR = new RandomValueGeneratorFactory(SETTER_MANAGER);

    private static ValueGeneratorFactory valueGeneratorFactory = STANDARD_VALUE_GENERATOR;

    private ObjectGenerator(){
        throw new IllegalAccessError();
    }
    
    public static void enableCaching() {
        valueGeneratorFactory = new CachedValueGeneratorFactory(STANDARD_VALUE_GENERATOR);
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
    
    public static <T, V> void register(final Class<T> searchClass, final String name, final ValueGenerator<V> valueGenerator){
    	SETTER_MANAGER.register(searchClass, name, valueGenerator);
    }
}
