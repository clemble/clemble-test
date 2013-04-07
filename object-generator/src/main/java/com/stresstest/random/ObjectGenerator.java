package com.stresstest.random;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.stresstest.random.generator.CachedValueGeneratorFactory;
import com.stresstest.random.generator.RandomValueGeneratorFactory;
import com.stresstest.random.generator.ValueGeneratorFactory;

public class ObjectGenerator {
	

	/**
	 * {@link Collection} random value generator, as a result empty {@link ArrayList} returned.
	 */
	@SuppressWarnings("rawtypes")
	final public static ValueGenerator<Collection<?>> COLLECTION_VALUE_GENERATOR = new ValueGenerator<Collection<?>>() {
		@Override
		public Collection<?> generate() {
			return new ArrayList();
		}
	};

	/**
	 * {@link List} random value generator, as a result generates empty {@link ArrayList} returned.
	 */
	@SuppressWarnings("rawtypes")
	final public static ValueGenerator<List<?>> LIST_VALUE_GENERATOR = new ValueGenerator<List<?>>() {
		@Override
		public List<?> generate() {
			return new ArrayList();
		}
	};

	/**
	 * {@link Queue} random value generator as a result empty {@link ArrayDeque} returned.
	 */
	@SuppressWarnings("rawtypes")
	final public static ValueGenerator<Queue<?>> QUEUE_VALUE_GENERATOR = new ValueGenerator<Queue<?>>() {
		@Override
		public Queue<?> generate() {
			return new ArrayDeque();
		}
	};

	/**
	 * {@link Deque} random value generator, as a result emptu {@link ArrayDeque} returned.
	 */
	@SuppressWarnings("rawtypes")
	final public static ValueGenerator<Deque<?>> DEQUE_VALUE_GENERATOR = new ValueGenerator<Deque<?>>() {
		@Override
		public Deque<?> generate() {
			return new ArrayDeque();
		}
	};

	/**
	 * {@link Set} random value generator, as a result empty {@link HashSet} returned.
	 */
	@SuppressWarnings("rawtypes")
	final public static ValueGenerator<Set<?>> SET_VALUE_GENERATOR = new ValueGenerator<Set<?>>() {
		@Override
		public Set<?> generate() {
			return new HashSet();
		}
	};

	/**
	 * {@link Map} random value generator, as a result empty {@link HashMap} returned.
	 */
	@SuppressWarnings("rawtypes")
	final public static ValueGenerator<Map<?, ?>> MAP_VALUE_GENERATOR = new ValueGenerator<Map<?, ?>>() {
		@Override
		public Map<?, ?> generate() {
			return new HashMap();
		}
	};

	final private static Map<Class<?>, ValueGenerator<?>> EMPTY_COLLECTION_GENERATORS;
	static {
		HashMap<Class<?>, ValueGenerator<?>> standardValueGenerators = new HashMap<Class<?>, ValueGenerator<?>>();

		standardValueGenerators.put(Collection.class, COLLECTION_VALUE_GENERATOR);
		standardValueGenerators.put(List.class, LIST_VALUE_GENERATOR);
		standardValueGenerators.put(Set.class, SET_VALUE_GENERATOR);
		standardValueGenerators.put(Queue.class, QUEUE_VALUE_GENERATOR);
		standardValueGenerators.put(Deque.class, DEQUE_VALUE_GENERATOR);

		standardValueGenerators.put(Map.class, MAP_VALUE_GENERATOR);

		EMPTY_COLLECTION_GENERATORS = ImmutableMap.copyOf(standardValueGenerators);
	}

    final private static ValueGeneratorFactory STANDARD_VALUE_GENERATOR = new RandomValueGeneratorFactory();

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
    	if(EMPTY_COLLECTION_GENERATORS.containsKey(classToGenerate))
    		return (ValueGenerator<T>) EMPTY_COLLECTION_GENERATORS.get(classToGenerate);
        return valueGeneratorFactory.getValueGenerator(classToGenerate);
    }
}
