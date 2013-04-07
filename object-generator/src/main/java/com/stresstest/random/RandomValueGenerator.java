package com.stresstest.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public abstract class RandomValueGenerator<T> extends ValueGenerator<T> {

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
     * {@link Byte} random value generator.
     */
    final public static ValueGenerator<Byte> BYTE_VALUE_GENERATOR = new RandomValueGenerator<Byte>() {
        @Override
        public Byte generate() {
            return (byte) RANDOM_UTILS.nextInt();
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
     * {@link Integer} random value generator.
     */
    final public static ValueGenerator<Integer> INTEGER_VALUE_GENERATOR = new RandomValueGenerator<Integer>() {
        @Override
        public Integer generate() {
            return RANDOM_UTILS.nextInt();
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
     * {@link Long} random value generator.
     */
    final public static ValueGenerator<Long> LONG_VALUE_GENERATOR = new RandomValueGenerator<Long>() {
        @Override
        public Long generate() {
            return RANDOM_UTILS.nextLong();
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
     * {@link Double} random value generator.
     */
    final public static ValueGenerator<Double> DOUBLE_VALUE_GENERATOR = new RandomValueGenerator<Double>() {
        @Override
        public Double generate() {
            return RANDOM_UTILS.nextDouble();
        }
    };
    
    /**
     * {@link String} generates random String of 10 characters long.
     */
    final public static ValueGenerator<String> STRING_VALUE_GENERATOR = new RandomValueGenerator<String>() {
        final private static int DEFAULT_GENERATOR_SIZE = 10;

        @Override
        public String generate() {
            return RandomStringUtils.random(DEFAULT_GENERATOR_SIZE);
        }
    };

    /**
     * Generates random selection from list of enums
     * 
     * @param enumClass source enum classs
     * @return random enum value
     */
    final public static <T> ValueGenerator<T> enumValueGenerator(final Class<T> enumClass) {
    	if(!enumClass.isEnum())
    		throw new IllegalArgumentException("Class must be of type enum");
        return randomValueGenerator(Arrays.asList(enumClass.getEnumConstants()));
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

}

