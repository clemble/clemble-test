package com.stresstest.random.construction;

import com.clemble.test.random.ObjectGenerator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mavarazy on 22/02/14.
 */
public class FormatedFactoryMethodGenerationTest {

    public static enum Some {
        ONE,
        TWO,
        THREE;
    }

    final public static class SomeOne {
        final private Some some;
        final private String one;

        public SomeOne(String one, Some some) {
            this.some = some;
            this.one = one;
        }

        public Some getSome() {
            return some;
        }

        public String getOne() {
            return one;
        }

        public static SomeOne fromString(String someOne) {
            String[] parts = someOne.split(":");
            return new SomeOne(parts[0], Some.valueOf(parts[1]));
        }
    }

    @Test
    public void testGeneration(){
        Assert.assertNotNull(ObjectGenerator.generate(SomeOne.class));
    }

}
