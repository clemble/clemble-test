package com.clemble.test.random.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ValueGenerator;
import com.google.common.collect.ImmutableMap;

public abstract class RandomValueGenerator<T> extends AbstractValueGenerator<T> {

    /**
     * Generic source of randomness in all value generators (shared for performance reasons).
     */
    final public static Random RANDOM_UTILS = new Random();

    /**
     * {@link Boolean} random value generator.
     */
    final public static ValueGenerator<Boolean> BOOLEAN_VALUE_GENERATOR = new RandomValueGenerator<Boolean>() {
        @Override
        public Boolean generate() {
            return RANDOM_UTILS.nextBoolean();
        }
    };

    /**
     * {@link Byte} array random value generator.
     */
    final public static ValueGenerator<boolean[]> BOOLEAN_ARRAY_VALUE_GENERATOR = new RandomValueGenerator<boolean[]>() {
        @Override
        public boolean[] generate() {
            boolean[] resultArray = new boolean[1 + RANDOM_UTILS.nextInt(10)];
            for (int i = 0; i < resultArray.length; i++)
                resultArray[i] = BOOLEAN_VALUE_GENERATOR.generate();
            return resultArray;
        }
    };

    /**
     * {@link Byte} random value generator.
     */
    final public static ValueGenerator<Byte> BYTE_VALUE_GENERATOR = new RandomValueGenerator<Byte>() {
        @Override
        public Byte generate() {
            return (byte) RANDOM_UTILS.nextInt();
        }
    };

    /**
     * {@link Byte} array random value generator.
     */
    final public static ValueGenerator<byte[]> BYTE_ARRAY_VALUE_GENERATOR = new RandomValueGenerator<byte[]>() {
        @Override
        public byte[] generate() {
            byte[] resultArray = new byte[1 + RANDOM_UTILS.nextInt(10)];
            for (int i = 0; i < resultArray.length; i++)
                resultArray[i] = BYTE_VALUE_GENERATOR.generate();
            return resultArray;
        }
    };

    /**
     * {@link Character} random value generator.
     */
    final public static ValueGenerator<Character> CHAR_VALUE_GENERATOR = new RandomValueGenerator<Character>() {
        @Override
        public Character generate() {
            return (char) RANDOM_UTILS.nextInt((int) Character.MAX_VALUE);
        }
    };

    /**
     * {@link Character} array random value generator.
     */
    final public static ValueGenerator<char[]> CHAR_ARRAY_VALUE_GENERATOR = new RandomValueGenerator<char[]>() {
        @Override
        public char[] generate() {
            char[] resultArray = new char[1 + RANDOM_UTILS.nextInt(10)];
            for (int i = 0; i < resultArray.length; i++)
                resultArray[i] = CHAR_VALUE_GENERATOR.generate();
            return resultArray;
        }
    };

    /**
     * {@link Integer} random value generator.
     */
    final public static ValueGenerator<Integer> INTEGER_VALUE_GENERATOR = new RandomValueGenerator<Integer>() {
        @Override
        public Integer generate() {
            return RANDOM_UTILS.nextInt();
        }
    };

    /**
     * {@link Integer} array random value generator.
     */
    final public static ValueGenerator<int[]> INTEGER_ARRAY_VALUE_GENERATOR = new RandomValueGenerator<int[]>() {
        @Override
        public int[] generate() {
            int[] resultArray = new int[1 + RANDOM_UTILS.nextInt(10)];
            for (int i = 0; i < resultArray.length; i++)
                resultArray[i] = INTEGER_VALUE_GENERATOR.generate();
            return resultArray;
        }
    };

    /**
     * {@link Short} random value generator.
     */
    final public static ValueGenerator<Short> SHORT_VALUE_GENERATOR = new RandomValueGenerator<Short>() {
        @Override
        public Short generate() {
            return (short) RANDOM_UTILS.nextInt();
        }
    };

    /**
     * {@link Short} array random value generator.
     */
    final public static ValueGenerator<short[]> SHORT_ARRAY_VALUE_GENERATOR = new RandomValueGenerator<short[]>() {
        @Override
        public short[] generate() {
            short[] resultArray = new short[1 + RANDOM_UTILS.nextInt(10)];
            for (int i = 0; i < resultArray.length; i++)
                resultArray[i] = SHORT_VALUE_GENERATOR.generate();
            return resultArray;
        }
    };

    /**
     * {@link Long} random value generator.
     */
    final public static ValueGenerator<Long> LONG_VALUE_GENERATOR = new RandomValueGenerator<Long>() {
        @Override
        public Long generate() {
            return RANDOM_UTILS.nextLong();
        }
    };

    /**
     * {@link Long} array random value generator.
     */
    final public static ValueGenerator<long[]> LONG_ARRAY_VALUE_GENERATOR = new RandomValueGenerator<long[]>() {
        @Override
        public long[] generate() {
            long[] resultArray = new long[1 + RANDOM_UTILS.nextInt(10)];
            for (int i = 0; i < resultArray.length; i++)
                resultArray[i] = LONG_VALUE_GENERATOR.generate();
            return resultArray;
        }
    };

    /**
     * {@link Float} random value generator.
     */
    final public static ValueGenerator<Float> FLOAT_VALUE_GENERATOR = new RandomValueGenerator<Float>() {
        @Override
        public Float generate() {
            return RANDOM_UTILS.nextFloat();
        }
    };

    /**
     * {@link Float} array random value generator.
     */
    final public static ValueGenerator<float[]> FLOAT_ARRAY_VALUE_GENERATOR = new RandomValueGenerator<float[]>() {
        @Override
        public float[] generate() {
            float[] resultArray = new float[1 + RANDOM_UTILS.nextInt(10)];
            for (int i = 0; i < resultArray.length; i++)
                resultArray[i] = LONG_VALUE_GENERATOR.generate();
            return resultArray;
        }
    };

    /**
     * {@link Double} random value generator.
     */
    final public static ValueGenerator<Double> DOUBLE_VALUE_GENERATOR = new RandomValueGenerator<Double>() {
        @Override
        public Double generate() {
            return RANDOM_UTILS.nextDouble();
        }
    };

    /**
     * {@link Double} array random value generator.
     */
    final public static ValueGenerator<double[]> DOUBLE_ARRAY_VALUE_GENERATOR = new RandomValueGenerator<double[]>() {
        @Override
        public double[] generate() {
            double[] resultArray = new double[1 + RANDOM_UTILS.nextInt(10)];
            for (int i = 0; i < resultArray.length; i++)
                resultArray[i] = LONG_VALUE_GENERATOR.generate();
            return resultArray;
        }
    };

    /**
     * {@link String} generates random String of 10 characters long.
     */
    final public static ValueGenerator<String> STRING_VALUE_GENERATOR = new RandomValueGenerator<String>() {
        final private static int DEFAULT_GENERATOR_SIZE = 10;

        @Override
        public String generate() {
            return RandomStringUtils.randomAscii(DEFAULT_GENERATOR_SIZE);
        }
    };

    /**
     * Generates random selection from list of enums
     * 
     * @param enumClass source enum classs
     * @return random enum value
     */
    final public static <T> ValueGenerator<T> enumValueGenerator(final Class<T> enumClass) {
        if (!enumClass.isEnum())
            throw new IllegalArgumentException("Class must be of type enum");
        return valueGenerator(Arrays.asList(enumClass.getEnumConstants()));
    }

    /**
     * Generates random selection from list of elements
     * 
     * @param iterable
     *            {@link Iterable} of possible values.
     * @return ValueGenerator that returns one of the elements of original {@link Iterable}.
     */
    final public static <T> ValueGenerator<T> valueGenerator(final Iterable<T> iterable) {
        final List<T> randomValues = new ArrayList<T>();
        for (T value : iterable)
            randomValues.add(value);
        return new RandomValueGenerator<T>() {
            @Override
            public T generate() {
                return randomValues.get(RANDOM_UTILS.nextInt(randomValues.size()));
            }

            @Override
            public int scope() {
                return randomValues.size();
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
        return new RandomValueGenerator<String>() {
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
        return new RandomValueGenerator<String>() {
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
        return new RandomValueGenerator<String>() {
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
        return new RandomValueGenerator<String>() {
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
        Map<Class<?>, ValueGenerator<?>> valueGenerators = new HashMap<Class<?>, ValueGenerator<?>>();
        valueGenerators.put(String.class, RandomValueGenerator.STRING_VALUE_GENERATOR);

        valueGenerators.put(Boolean.class, RandomValueGenerator.BOOLEAN_VALUE_GENERATOR);
        valueGenerators.put(boolean.class, RandomValueGenerator.BOOLEAN_VALUE_GENERATOR);
        valueGenerators.put(boolean[].class, RandomValueGenerator.BOOLEAN_ARRAY_VALUE_GENERATOR);

        valueGenerators.put(Byte.class, RandomValueGenerator.BYTE_VALUE_GENERATOR);
        valueGenerators.put(byte.class, RandomValueGenerator.BYTE_VALUE_GENERATOR);
        valueGenerators.put(byte[].class, RandomValueGenerator.BYTE_ARRAY_VALUE_GENERATOR);

        valueGenerators.put(Character.class, RandomValueGenerator.CHAR_VALUE_GENERATOR);
        valueGenerators.put(char.class, RandomValueGenerator.CHAR_VALUE_GENERATOR);
        valueGenerators.put(char[].class, RandomValueGenerator.CHAR_ARRAY_VALUE_GENERATOR);

        valueGenerators.put(Short.class, RandomValueGenerator.SHORT_VALUE_GENERATOR);
        valueGenerators.put(short.class, RandomValueGenerator.SHORT_VALUE_GENERATOR);
        valueGenerators.put(short[].class, RandomValueGenerator.SHORT_ARRAY_VALUE_GENERATOR);

        valueGenerators.put(Integer.class, RandomValueGenerator.INTEGER_VALUE_GENERATOR);
        valueGenerators.put(int.class, RandomValueGenerator.INTEGER_VALUE_GENERATOR);
        valueGenerators.put(int[].class, RandomValueGenerator.INTEGER_ARRAY_VALUE_GENERATOR);

        valueGenerators.put(Long.class, RandomValueGenerator.LONG_VALUE_GENERATOR);
        valueGenerators.put(long.class, RandomValueGenerator.LONG_VALUE_GENERATOR);
        valueGenerators.put(long[].class, RandomValueGenerator.LONG_ARRAY_VALUE_GENERATOR);

        valueGenerators.put(Float.class, RandomValueGenerator.FLOAT_VALUE_GENERATOR);
        valueGenerators.put(float.class, RandomValueGenerator.FLOAT_VALUE_GENERATOR);
        valueGenerators.put(float[].class, RandomValueGenerator.FLOAT_ARRAY_VALUE_GENERATOR);

        valueGenerators.put(Double.class, RandomValueGenerator.DOUBLE_VALUE_GENERATOR);
        valueGenerators.put(double.class, RandomValueGenerator.DOUBLE_VALUE_GENERATOR);
        valueGenerators.put(double[].class, RandomValueGenerator.DOUBLE_ARRAY_VALUE_GENERATOR);

        DEFAULT_GENERATORS = ImmutableMap.<Class<?>, ValueGenerator<?>> copyOf(valueGenerators);
    }

}
