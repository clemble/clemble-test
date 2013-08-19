package com.stresstest.random.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.stresstest.random.AbstractValueGeneratorFactory;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.constructor.ClassPropertySetterRegistry;

public class SequentialValueGeneratorFactory extends AbstractValueGeneratorFactory {

    public SequentialValueGeneratorFactory() {
        super(new ClassPropertySetterRegistry(), SequentialValueGenerator.DEFAULT_GENERATORS);
    }

    public SequentialValueGeneratorFactory(ClassPropertySetterRegistry propertySetterManager) {
        super(propertySetterManager, SequentialValueGenerator.DEFAULT_GENERATORS);
    }

    @Override
    protected <T> ValueGenerator<T> enumValueGenerator(Class<T> klass) {
        return SequentialValueGenerator.enumValueGenerator(klass);
    }

    public <T> ValueGenerator<T> replace(ValueGenerator<T> valueGeneratorToReplace) {
        // Step 1. Sanity check
        if (valueGeneratorToReplace == null || valueGeneratorToReplace instanceof SequentialValueGenerator)
            return valueGeneratorToReplace;
        // Step 2. Checking generated class
        T generatedValue = valueGeneratorToReplace.generate();
        if (generatedValue == null)
            return valueGeneratorToReplace;
        @SuppressWarnings("unchecked")
        Class<T> targetClass = (Class<T>) valueGeneratorToReplace.generate().getClass();
        // Step 3. Constructing generator, based on the class
        return getValueGenerator(targetClass);
    }

    public List<ValueGenerator<?>> replace(Collection<ValueGenerator<?>> generators) {
        List<ValueGenerator<?>> newGenerators = new ArrayList<ValueGenerator<?>>();
        for (ValueGenerator<?> constructorGenerator : generators) {
            ValueGenerator<?> newConstructorGenerator = replace(constructorGenerator);
            newGenerators.add(newConstructorGenerator);
        }
        return newGenerators;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ValueGenerator arrayValueGenerator(Class klass) {
        final ValueGenerator valueGenerator = getValueGenerator(klass.getComponentType());
        return new SequentialValueGenerator() {
            @Override
            public Object[] generate() {
                int size = 10;
                Object[] values = new Object[size];
                for (int i = 0; i < size; i++)
                    values[i] = valueGenerator.generate();
                return values;
            }
        };
    }

}
