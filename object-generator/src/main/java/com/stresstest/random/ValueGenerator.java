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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

public abstract class ValueGenerator<T> {

    final public static RandomGenerator RANDOM_UTILS = new JDKRandomGenerator();

    abstract public T generate();

    final public static ValueGenerator<Boolean> BOOLEAN_VALUE_GENERATOR = new ValueGenerator<Boolean>() {
        @Override
        public Boolean generate() {
            return RANDOM_UTILS.nextBoolean();
        }
    };

    final public static ValueGenerator<Byte> BYTE_VALUE_GENERATOR = new ValueGenerator<Byte>() {
        @Override
        public Byte generate() {
            return (byte) RANDOM_UTILS.nextInt();
        }
    };

    final public static ValueGenerator<Character> CHAR_VALUE_GENERATOR = new ValueGenerator<Character>() {
        @Override
        public Character generate() {
            return (char) RANDOM_UTILS.nextInt((int) Character.MAX_VALUE);
        }
    };

    final public static ValueGenerator<Integer> INTEGER_VALUE_GENERATOR = new ValueGenerator<Integer>() {
        @Override
        public Integer generate() {
            return RANDOM_UTILS.nextInt();
        }
    };

    final public static ValueGenerator<Short> SHORT_VALUE_GENERATOR = new ValueGenerator<Short>() {
        @Override
        public Short generate() {
            return (short) RANDOM_UTILS.nextInt();
        }
    };

    final public static ValueGenerator<Long> LONG_VALUE_GENERATOR = new ValueGenerator<Long>() {
        @Override
        public Long generate() {
            return RANDOM_UTILS.nextLong();
        }
    };

    final public static ValueGenerator<Float> FLOAT_VALUE_GENERATOR = new ValueGenerator<Float>() {
        @Override
        public Float generate() {
            return RANDOM_UTILS.nextFloat();
        }
    };

    final public static ValueGenerator<Double> DOUBLE_VALUE_GENERATOR = new ValueGenerator<Double>() {
        @Override
        public Double generate() {
            return RANDOM_UTILS.nextDouble();
        }
    };

    @SuppressWarnings("rawtypes")
    final public static ValueGenerator<Collection<?>> COLLECTION_VALUE_GENERATOR = new ValueGenerator<Collection<?>>() {
        @Override
        public Collection<?> generate() {
            return new ArrayList();
        }
    };

    @SuppressWarnings("rawtypes")
    final public static ValueGenerator<List<?>> LIST_VALUE_GENERATOR = new ValueGenerator<List<?>>() {
        @Override
        public List<?> generate() {
            return new ArrayList();
        }
    };
    
    @SuppressWarnings("rawtypes")
    final public static ValueGenerator<Queue<?>> QUEUE_VALUE_GENERATOR = new ValueGenerator<Queue<?>>() {
        @Override
        public Queue<?> generate() {
            return new ArrayDeque();
        }
    };

    @SuppressWarnings("rawtypes")
    final public static ValueGenerator<Deque<?>> DEQUE_VALUE_GENERATOR = new ValueGenerator<Deque<?>>() {
        @Override
        public Deque<?> generate() {
            return new ArrayDeque();
        }
    };

    @SuppressWarnings("rawtypes")
    final public static ValueGenerator<Set<?>> SET_VALUE_GENERATOR = new ValueGenerator<Set<?>>() {
        @Override
        public Set<?> generate() {
            return new HashSet();
        }
    };

    @SuppressWarnings("rawtypes")
    final public static ValueGenerator<Map<?, ?>> MAP_VALUE_GENERATOR = new ValueGenerator<Map<?, ?>>() {
        @Override
        public Map<?, ?> generate() {
            return new HashMap();
        }
    };

    final public static ValueGenerator<String> STRING_VALUE_GENERATOR = new ValueGenerator<String>() {
        final private static int DEFAULT_GENERATOR_SIZE = 10;

        @Override
        public String generate() {
            return RandomStringUtils.randomAlphabetic(DEFAULT_GENERATOR_SIZE);
        }
    };

    final public static <T> ValueGenerator<T> constantValueGenerator(final T constant) {
        return new ValueGenerator<T>() {
            @Override
            public T generate() {
                return constant;
            }
        };
    }

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

}
