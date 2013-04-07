package com.stresstest.random.generator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.constructor.PropertySetterManager;


abstract public class AbstractValueGeneratorFactory implements ValueGeneratorFactory {
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
	
	final protected <T> ValueGenerator<T> getDefaultValueGenerator(Class<T> targetClass) {
		return (ValueGenerator<T>) EMPTY_COLLECTION_GENERATORS.get(targetClass);
	}
	
	final private PropertySetterManager propertySetterManager;

	public AbstractValueGeneratorFactory() {
		this.propertySetterManager = new PropertySetterManager();
	}
	
	public AbstractValueGeneratorFactory(PropertySetterManager setterManager) {
		this.propertySetterManager = setterManager != null ? setterManager : new PropertySetterManager();
	}
	
	@Override
	final public Collection<ValueGenerator<?>> getValueGenerators(Class<?>[] parameters) {
		// Step 1. Sanity check
		if (parameters == null || parameters.length == 0)
			return Collections.emptyList();
		// Step 2. Sequential check
		Collection<ValueGenerator<?>> resultGenerators = new ArrayList<ValueGenerator<?>>();
		for (Class<?> parameter : parameters) {
			if (parameter != null)
				resultGenerators.add(getValueGenerator(parameter));
		}
		return resultGenerators;
	}
	
	@Override
	final public PropertySetterManager getPropertySetterManager() {
		return propertySetterManager;
	}

}
