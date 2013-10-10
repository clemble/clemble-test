package com.clemble.test.random.generator;

import com.clemble.test.random.AbstractValueGeneratorFactory;
import com.clemble.test.random.ValueGenerator;
import com.clemble.test.random.constructor.ClassPropertySetterRegistry;

public class RandomValueGeneratorFactory extends AbstractValueGeneratorFactory {

    public RandomValueGeneratorFactory() {
        super(new ClassPropertySetterRegistry(), RandomValueGenerator.DEFAULT_GENERATORS);
    }

    public RandomValueGeneratorFactory(ClassPropertySetterRegistry propertySetterManager) {
        super(propertySetterManager, RandomValueGenerator.DEFAULT_GENERATORS);
    }

    @Override
    public <T> ValueGenerator<T> enumValueGenerator(Class<T> enumClass) {
        return RandomValueGenerator.enumValueGenerator(enumClass);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ValueGenerator arrayValueGenerator(Class klass) {
        final ValueGenerator valueGenerator = getValueGenerator(klass.getComponentType());
        return new RandomValueGenerator() {

            @Override
            public Object[] generate() {
                int size = 1 + RANDOM_UTILS.nextInt(10);
                Object[] values = new Object[size];
                for (int i = 0; i < size; i++)
                    values[i] = valueGenerator.generate();
                return values;
            }
        };
    }

}
