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
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

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
     * Generic source of randomness in all value generators (shared for performance reasons).
     */
    final public static Random RANDOM_UTILS = new Random();

    /**
     * Generates random T value.
     * 
     * @return random T value
     */
    abstract public T generate();

    /**
     * {@link Boolean} random value generator.
     */
    final public static ValueGenerator<Boolean> BOOLEAN_VALUE_GENERATOR = new ValueGenerator<Boolean>() {
        @Override
        public Boolean generate() {
            return RANDOM_UTILS.nextBoolean();
        }
    };

    /**
     * {@link Byte} random value generator.
     */
    final public static ValueGenerator<Byte> BYTE_VALUE_GENERATOR = new ValueGenerator<Byte>() {
        @Override
        public Byte generate() {
            return (byte) RANDOM_UTILS.nextInt();
        }
    };

    /**
     * {@link Character} random value generator.
     */
    final public static ValueGenerator<Character> CHAR_VALUE_GENERATOR = new ValueGenerator<Character>() {
        @Override
        public Character generate() {
            return (char) RANDOM_UTILS.nextInt((int) Character.MAX_VALUE);
        }
    };

    /**
     * {@link Integer} random value generator.
     */
    final public static ValueGenerator<Integer> INTEGER_VALUE_GENERATOR = new ValueGenerator<Integer>() {
        @Override
        public Integer generate() {
            return RANDOM_UTILS.nextInt();
        }
    };

    /**
     * {@link Short} random value generator.
     */
    final public static ValueGenerator<Short> SHORT_VALUE_GENERATOR = new ValueGenerator<Short>() {
        @Override
        public Short generate() {
            return (short) RANDOM_UTILS.nextInt();
        }
    };

    /**
     * {@link Long} random value generator.
     */
    final public static ValueGenerator<Long> LONG_VALUE_GENERATOR = new ValueGenerator<Long>() {
        @Override
        public Long generate() {
            return RANDOM_UTILS.nextLong();
        }
    };

    /**
     * {@link Float} random value generator.
     */
    final public static ValueGenerator<Float> FLOAT_VALUE_GENERATOR = new ValueGenerator<Float>() {
        @Override
        public Float generate() {
            return RANDOM_UTILS.nextFloat();
        }
    };

    /**
     * {@link Double} random value generator.
     */
    final public static ValueGenerator<Double> DOUBLE_VALUE_GENERATOR = new ValueGenerator<Double>() {
        @Override
        public Double generate() {
            return RANDOM_UTILS.nextDouble();
        }
    };

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
     * {@link String} generates random String of 10 characters long.
     */
    final public static ValueGenerator<String> STRING_VALUE_GENERATOR = new ValueGenerator<String>() {
        final private static int DEFAULT_GENERATOR_SIZE = 10;

        @Override
        public String generate() {
            return RandomStringUtils.random(DEFAULT_GENERATOR_SIZE);
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
        return new ValueGenerator<T>() {
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
        final List<T> randomValues = new ArrayList<T>();
        for (T value : iterable)
            randomValues.add(value);
        return new ValueGenerator<T>() {
            @Override
            public T generate() {
                return randomValues.get(RANDOM_UTILS.nextInt(randomValues.size()));
            }
        };
    }

    /**
     * Generates random {@link String} generator, that produces random {@link String} of defined length.
     * 
     * @param length
     *            size of the {@link String} to generate.
     * @return random String.
     */
    final public static ValueGenerator<String> randomString(final int length) {
        if (length <= 0)
            throw new IllegalArgumentException("Length must be possitive");
        return new ValueGenerator<String>() {
            @Override
            public String generate() {
                return RandomStringUtils.random(length);
            }
        };
    }

    /**
     * Generates random alphabetic {@link String} generator, that produces random alphabetic {@link String} of defined length.
     * 
     * @param length
     *            size of the {@link String} to generate.
     * @return random alphabetic {@link String}.
     */
    final public static ValueGenerator<String> randomAlphabeticString(final int length) {
        if (length <= 0)
            throw new IllegalArgumentException("Length must be possitive");
        return new ValueGenerator<String>() {
            @Override
            public String generate() {
                return RandomStringUtils.randomAlphabetic(length);
            }
        };
    }

    /**
     * Generates random alphanumeric {@link String} generator, that produces random alphanumeric {@link String} of defined length.
     * 
     * @param length
     *            size of the {@link String} to generate.
     * @return random alphanumeric {@link String}.
     */
    final public static ValueGenerator<String> randomAlphanumericString(final int length) {
        if (length <= 0)
            throw new IllegalArgumentException("Length must be possitive");
        return new ValueGenerator<String>() {
            @Override
            public String generate() {
                return RandomStringUtils.randomAlphanumeric(length);
            }
        };
    }

    /**
     * Generates random ASCII {@link String} generator, that produces random ASCII {@link String} of defined length.
     * 
     * @param length
     *            size of the {@link String} to generate.
     * @return random ASCII {@link String}.
     */
    final public static ValueGenerator<String> randomAsciiString(final int length) {
        if (length <= 0)
            throw new IllegalArgumentException("Length must be possitive");
        return new ValueGenerator<String>() {
            @Override
            public String generate() {
                return RandomStringUtils.randomAscii(length);
            }
        };
    }

    /**
     * Collection of standard value generators, which must be used by default
     */
    final public static Map<Class<?>, ValueGenerator<?>> DEFAULT_GENERATORS;
    static {
        Map<Class<?>, ValueGenerator<?>> standardValueGenerators = new HashMap<Class<?>, ValueGenerator<?>>();
        standardValueGenerators.put(String.class, STRING_VALUE_GENERATOR);

        standardValueGenerators.put(Boolean.class, BOOLEAN_VALUE_GENERATOR);
        standardValueGenerators.put(boolean.class, BOOLEAN_VALUE_GENERATOR);

        standardValueGenerators.put(Byte.class, BYTE_VALUE_GENERATOR);
        standardValueGenerators.put(byte.class, BYTE_VALUE_GENERATOR);
        standardValueGenerators.put(Character.class, CHAR_VALUE_GENERATOR);
        standardValueGenerators.put(char.class, CHAR_VALUE_GENERATOR);
        standardValueGenerators.put(Short.class, SHORT_VALUE_GENERATOR);
        standardValueGenerators.put(short.class, SHORT_VALUE_GENERATOR);
        standardValueGenerators.put(Integer.class, INTEGER_VALUE_GENERATOR);
        standardValueGenerators.put(int.class, INTEGER_VALUE_GENERATOR);
        standardValueGenerators.put(Long.class, LONG_VALUE_GENERATOR);
        standardValueGenerators.put(long.class, LONG_VALUE_GENERATOR);

        standardValueGenerators.put(Float.class, FLOAT_VALUE_GENERATOR);
        standardValueGenerators.put(float.class, FLOAT_VALUE_GENERATOR);
        standardValueGenerators.put(Double.class, DOUBLE_VALUE_GENERATOR);
        standardValueGenerators.put(double.class, DOUBLE_VALUE_GENERATOR);

        standardValueGenerators.put(Collection.class, COLLECTION_VALUE_GENERATOR);
        standardValueGenerators.put(List.class, LIST_VALUE_GENERATOR);
        standardValueGenerators.put(Set.class, SET_VALUE_GENERATOR);
        standardValueGenerators.put(Queue.class, QUEUE_VALUE_GENERATOR);
        standardValueGenerators.put(Deque.class, DEQUE_VALUE_GENERATOR);

        standardValueGenerators.put(Map.class, MAP_VALUE_GENERATOR);

        DEFAULT_GENERATORS = ImmutableMap.copyOf(standardValueGenerators);
    }

}
