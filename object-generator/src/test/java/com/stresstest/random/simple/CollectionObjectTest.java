package com.stresstest.random.simple;

import junit.framework.Assert;

import org.junit.Test;

import com.stresstest.random.ObjectGenerator;

public class CollectionObjectTest extends AbstractSimpleObjectTest<CollectionObject> {

    public CollectionObjectTest() {
        super(CollectionObject.class, 100);
        ObjectGenerator.enableCaching();
    }

    @Test
    public void testCollectionsNotEmpty() {
        CollectionObject collectionObject = ObjectGenerator.generate(CollectionObject.class);

        Assert.assertFalse(collectionObject.getFloatCollection().isEmpty());
        Assert.assertFalse(collectionObject.getIntegerCollection().isEmpty());
        Assert.assertFalse(collectionObject.getStringCollection().isEmpty());
        
        Assert.assertEquals(collectionObject.getFloatCollection().size(), 1);
        Assert.assertEquals(collectionObject.getIntegerCollection().size(), 1);
        Assert.assertEquals(collectionObject.getStringCollection().size(), 1);

        Assert.assertTrue(collectionObject.getDoubleCollection().isEmpty());
        Assert.assertTrue(collectionObject.getLongCollection().isEmpty());

        Assert.assertFalse(collectionObject.getAnotherDoubleCollection().isEmpty());
        Assert.assertFalse(collectionObject.getAnotherFloatCollection().isEmpty());
        Assert.assertFalse(collectionObject.getAnotherIntegerCollection().isEmpty());
        Assert.assertFalse(collectionObject.getAnotherLongCollection().isEmpty());

        Assert.assertEquals(collectionObject.getAnotherDoubleCollection().size(), 1);
        Assert.assertEquals(collectionObject.getAnotherFloatCollection().size(), 1);
        Assert.assertEquals(collectionObject.getAnotherIntegerCollection().size(), 1);
        Assert.assertEquals(collectionObject.getAnotherLongCollection().size(), 1);

        Assert.assertNull(collectionObject.getAnotherStringCollection());
    
        Assert.assertNull(CollectionObject.getStaticStrings());
    }

}
