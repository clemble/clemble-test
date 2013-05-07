package com.stresstest.random;

import com.stresstest.random.constructor.ClassPropertySetterRegistry;
import com.stresstest.random.generator.CachedValueGeneratorFactory;
import com.stresstest.random.generator.RandomValueGeneratorFactory;

public class ObjectGenerator {
	
	final private static ClassPropertySetterRegistry SETTER_MANAGER = new ClassPropertySetterRegistry();
	
    final private static ValueGeneratorFactory DEFAULT_VALUE_GENERATOR = new RandomValueGeneratorFactory(SETTER_MANAGER);

    private static ValueGeneratorFactory valueGeneratorFactory = DEFAULT_VALUE_GENERATOR;

    private ObjectGenerator(){
        throw new IllegalAccessError();
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
