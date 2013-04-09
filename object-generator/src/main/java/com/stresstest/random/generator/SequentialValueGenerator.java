package com.stresstest.random.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.stresstest.random.AbstractValueGenerator;
import com.stresstest.random.ValueGenerator;

abstract public class SequentialValueGenerator<T>  extends AbstractValueGenerator<T> {
	
    /**
     * {@link Boolean} random value generator.
     */
    final public static ValueGenerator<Boolean> BOOLEAN_VALUE_GENERATOR = new SequentialValueGenerator<Boolean>() {
        private boolean currentBoolean = false;

        @Override
        public Boolean generate() {
            currentBoolean = !currentBoolean;
            return currentBoolean;
        }
    };

    /**
     * {@link Byte} random value generator.
     */
    final public static ValueGenerator<Byte> BYTE_VALUE_GENERATOR = new SequentialValueGenerator<Byte>() {
    	private byte currentByte = 0;
        
        @Override
        public Byte generate() {
        	currentByte++;
            return currentByte++;
        }
    };

    /**
     * {@link Character} random value generator.
     */
    final public static ValueGenerator<Character> CHAR_VALUE_GENERATOR = new SequentialValueGenerator<Character>() {
    	private char currentChar = 0;

        @Override
        public Character generate() {
            return currentChar++;
        }
    };

    /**
     * {@link Integer} random value generator.
     */
    final public static ValueGenerator<Integer> INTEGER_VALUE_GENERATOR = new SequentialValueGenerator<Integer>() {
    	private int currentInt = 0;
    	
        @Override
        public Integer generate() {
            return currentInt++;
        }
    };

    /**
     * {@link Short} random value generator.
     */
    final public static ValueGenerator<Short> SHORT_VALUE_GENERATOR = new SequentialValueGenerator<Short>() {
    	private short currentShort = 0;
    	
        @Override
        public Short generate() {
            return currentShort++;
        }
    };

    /**
     * {@link Long} random value generator.
     */
    final public static ValueGenerator<Long> LONG_VALUE_GENERATOR = new SequentialValueGenerator<Long>() {
    	private long currentLong = 0;
    	
        @Override
        public Long generate() {
            return currentLong++;
        }
    };

    /**
     * {@link Float} random value generator.
     */
    final public static ValueGenerator<Float> FLOAT_VALUE_GENERATOR = new SequentialValueGenerator<Float>() {
    	private float currentFloat = 0.0F;
    	
        @Override
        public Float generate() {
            return currentFloat++;
        }
    };

    /**
     * {@link Double} random value generator.
     */
    final public static ValueGenerator<Double> DOUBLE_VALUE_GENERATOR = new SequentialValueGenerator<Double>() {
    	private double currentDouble = 0.0;
    	
        @Override
        public Double generate() {
            return currentDouble++;
        }
    };
    
    final public static ValueGenerator<Integer> intRange(int start, int end) {
    	return intRange(start, end, 1);
    }
    
    final public static ValueGenerator<Integer> intRange(final int start, final int end, final int step) {
    	if(end < start && step > 0)
    		throw new IllegalArgumentException("Provided parameters " + start + " " + end + " " + step + " invalid");
    	if (end > start && step < 0)
    		throw new IllegalArgumentException("Provided parameters " + start + " " + end + " " + step + " invalid");
    	
    	final int range = end - start;
    	
    	return new SequentialValueGenerator<Integer>() {
    		private int currentInteger = 0;
    		
			@Override
			public Integer generate() {
				int resultValue = start + (currentInteger % range);
				currentInteger += step;
				return resultValue;
			}
		};
    }
    
	final public static <T> ValueGenerator<T> constantValueGenerator(final T constant) {
		return new SequentialValueGenerator<T>() {
			@Override
			public T generate() {
				return constant;
			}
		};
	}
    
    /**
     * Generates random selection from list of enums
     * 
     * @param enumClass source enum classs
     * @return random enum value
     */
    final public static <T> ValueGenerator<T> enumValueGenerator(final Class<T> enumClass) {
    	if(!enumClass.isEnum())
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
        return new SequentialValueGenerator<T>() {
        	private int position = 0;
            @Override
            public T generate() {
                return randomValues.get(position++ % randomValues.size());
            }
        };
    }

	/**
	 * Collection of standard value generators, which must be used by default
	 */
	final public static Map<Class<?>, ValueGenerator<?>> DEFAULT_GENERATORS;
	static {
		Map<Class<?>, ValueGenerator<?>> valueGenerators = new HashMap<Class<?>, ValueGenerator<?>>();
		valueGenerators.put(Boolean.class, SequentialValueGenerator.BOOLEAN_VALUE_GENERATOR);
		valueGenerators.put(boolean.class, SequentialValueGenerator.BOOLEAN_VALUE_GENERATOR);

		valueGenerators.put(Byte.class, SequentialValueGenerator.BYTE_VALUE_GENERATOR);
		valueGenerators.put(byte.class, SequentialValueGenerator.BYTE_VALUE_GENERATOR);
		valueGenerators.put(Character.class, SequentialValueGenerator.CHAR_VALUE_GENERATOR);
		valueGenerators.put(char.class, SequentialValueGenerator.CHAR_VALUE_GENERATOR);
		valueGenerators.put(Short.class, SequentialValueGenerator.SHORT_VALUE_GENERATOR);
		valueGenerators.put(short.class, SequentialValueGenerator.SHORT_VALUE_GENERATOR);
		valueGenerators.put(Integer.class, SequentialValueGenerator.INTEGER_VALUE_GENERATOR);
		valueGenerators.put(int.class, SequentialValueGenerator.INTEGER_VALUE_GENERATOR);
		valueGenerators.put(Long.class, SequentialValueGenerator.LONG_VALUE_GENERATOR);
		valueGenerators.put(long.class, SequentialValueGenerator.LONG_VALUE_GENERATOR);

		valueGenerators.put(Float.class, SequentialValueGenerator.FLOAT_VALUE_GENERATOR);
		valueGenerators.put(float.class, SequentialValueGenerator.FLOAT_VALUE_GENERATOR);
		valueGenerators.put(Double.class, SequentialValueGenerator.DOUBLE_VALUE_GENERATOR);
		valueGenerators.put(double.class, SequentialValueGenerator.DOUBLE_VALUE_GENERATOR);

		DEFAULT_GENERATORS = ImmutableMap.<Class<?>, ValueGenerator<?>>copyOf(valueGenerators);
	}

}
