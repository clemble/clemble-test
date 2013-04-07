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

/**
 * Value generator abstraction generates random T value.
 * 
 * @author Anton Oparin
 * 
 * @param <T>
 *            class parameter type to generate.
 */
public abstract class ValueGenerator<T> {

	/**
	 * Generates random T value.
	 * 
	 * @return random T value
	 */
	abstract public T generate();
	
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
    
    /**
     * Generates constant value generator, which brings no randomness in generation.
     * 
     * @param constant
     *            final variable to generate.
     * @return ValueGenerator that returns constant value for each call.
     */
    final public static <T> ValueGenerator<T> constantValueGenerator(final T constant) {
        return new RandomValueGenerator<T>() {
            @Override
            public T generate() {
                return constant;
            }
        };
    }
    
    /**
     * Generates random selection from list of elements
     * 
     * @param iterable
     *            {@link Iterable} of possible values.
     * @return ValueGenerator that returns one of the elements of original {@link Iterable}.
     */
    final public static <T> ValueGenerator<T> randomValueGenerator(final Iterable<T> iterable) {
        return RandomValueGenerator.valueGenerator(iterable);
    }

    /**
     * Collection of standard value generators, which must be used by default
     */
    final public static Map<Class<?>, ValueGenerator<?>> DEFAULT_GENERATORS;
    static {
        Map<Class<?>, ValueGenerator<?>> standardValueGenerators = new HashMap<Class<?>, ValueGenerator<?>>();
        standardValueGenerators.put(String.class, RandomValueGenerator.STRING_VALUE_GENERATOR);

        standardValueGenerators.put(Boolean.class, RandomValueGenerator.BOOLEAN_VALUE_GENERATOR);
        standardValueGenerators.put(boolean.class, RandomValueGenerator.BOOLEAN_VALUE_GENERATOR);

        standardValueGenerators.put(Byte.class, RandomValueGenerator.BYTE_VALUE_GENERATOR);
        standardValueGenerators.put(byte.class, RandomValueGenerator.BYTE_VALUE_GENERATOR);
        standardValueGenerators.put(Character.class, RandomValueGenerator.CHAR_VALUE_GENERATOR);
        standardValueGenerators.put(char.class, RandomValueGenerator.CHAR_VALUE_GENERATOR);
        standardValueGenerators.put(Short.class, RandomValueGenerator.SHORT_VALUE_GENERATOR);
        standardValueGenerators.put(short.class, RandomValueGenerator.SHORT_VALUE_GENERATOR);
        standardValueGenerators.put(Integer.class, RandomValueGenerator.INTEGER_VALUE_GENERATOR);
        standardValueGenerators.put(int.class, RandomValueGenerator.INTEGER_VALUE_GENERATOR);
        standardValueGenerators.put(Long.class, RandomValueGenerator.LONG_VALUE_GENERATOR);
        standardValueGenerators.put(long.class, RandomValueGenerator.LONG_VALUE_GENERATOR);

        standardValueGenerators.put(Float.class, RandomValueGenerator.FLOAT_VALUE_GENERATOR);
        standardValueGenerators.put(float.class, RandomValueGenerator.FLOAT_VALUE_GENERATOR);
        standardValueGenerators.put(Double.class, RandomValueGenerator.DOUBLE_VALUE_GENERATOR);
        standardValueGenerators.put(double.class, RandomValueGenerator.DOUBLE_VALUE_GENERATOR);

        standardValueGenerators.put(Collection.class, COLLECTION_VALUE_GENERATOR);
        standardValueGenerators.put(List.class, LIST_VALUE_GENERATOR);
        standardValueGenerators.put(Set.class, SET_VALUE_GENERATOR);
        standardValueGenerators.put(Queue.class, QUEUE_VALUE_GENERATOR);
        standardValueGenerators.put(Deque.class, DEQUE_VALUE_GENERATOR);

        standardValueGenerators.put(Map.class, MAP_VALUE_GENERATOR);

        DEFAULT_GENERATORS = ImmutableMap.copyOf(standardValueGenerators);
    }

}
