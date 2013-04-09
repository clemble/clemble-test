package com.stresstest.random;

import java.util.Iterator;

import com.stresstest.random.constructor.ClassPropertySetterManager;
import com.stresstest.random.generator.CachedValueGeneratorFactory;
import com.stresstest.random.generator.RandomValueGeneratorFactory;

public class ObjectGenerator {
	
	final private static ClassPropertySetterManager SETTER_MANAGER = new ClassPropertySetterManager();
	
    final private static ValueGeneratorFactory STANDARD_VALUE_GENERATOR = new RandomValueGeneratorFactory(SETTER_MANAGER);

    private static ValueGeneratorFactory valueGeneratorFactory = STANDARD_VALUE_GENERATOR;

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
    	return new Iterable<T>() {

			@Override
			public Iterator<T> iterator() {
				return null;
			}
		};
    }
    
    public static void enableCaching() {
    	valueGeneratorFactory = new CachedValueGeneratorFactory(STANDARD_VALUE_GENERATOR);
    }
    
    public static void disableCaching() {
    	valueGeneratorFactory = STANDARD_VALUE_GENERATOR;
    }
}
