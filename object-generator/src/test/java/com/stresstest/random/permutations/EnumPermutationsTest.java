package com.stresstest.random.permutations;

import java.util.Iterator;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import junit.framework.Assert;

import org.junit.Test;

import com.stresstest.random.ObjectGenerator;

public class EnumPermutationsTest {

	@Test
	public void testPublicEnumPermutations() {
		Iterable<PublicEnum> publicEnums = ObjectGenerator.getPossibleValues(PublicEnum.class);
		Iterator<PublicEnum> publicEnumsIterator = publicEnums.iterator();
		
		Assert.assertEquals(publicEnumsIterator.next(), PublicEnum.PUBLIC_ONE);
		Assert.assertEquals(publicEnumsIterator.next(), PublicEnum.PUBLIC_TWO);
		Assert.assertEquals(publicEnumsIterator.next(), PublicEnum.PUBLIC_THREE);
		Assert.assertEquals(publicEnumsIterator.next(), PublicEnum.PUBLIC_FOUR);
		Assert.assertEquals(publicEnumsIterator.next(), PublicEnum.PUBLIC_FIVE);
		Assert.assertEquals(publicEnumsIterator.next(), PublicEnum.PUBLIC_SIX);
		Assert.assertEquals(publicEnumsIterator.next(), PublicEnum.PUBLIC_SEVEN);
		Assert.assertEquals(publicEnumsIterator.next(), PublicEnum.PUBLIC_EIGHT);
		Assert.assertEquals(publicEnumsIterator.next(), PublicEnum.PUBLIC_NINE);
		Assert.assertFalse(publicEnumsIterator.hasNext());
		Assert.assertNull(publicEnumsIterator.next());
	}
	
	@Test
	public void testDefaultEnumPermutations() {
		Iterable<DefaultEnum> publicEnums = ObjectGenerator.getPossibleValues(DefaultEnum.class);
		Iterator<DefaultEnum> publicEnumsIterator = publicEnums.iterator();
		
		Assert.assertEquals(publicEnumsIterator.next(), DefaultEnum.DEFAULT_ONE);
		Assert.assertEquals(publicEnumsIterator.next(), DefaultEnum.DEFAULT_TWO);
		Assert.assertEquals(publicEnumsIterator.next(), DefaultEnum.DEFAULT_THREE);
		Assert.assertEquals(publicEnumsIterator.next(), DefaultEnum.DEFAULT_FOUR);
		Assert.assertEquals(publicEnumsIterator.next(), DefaultEnum.DEFAULT_FIVE);
		Assert.assertEquals(publicEnumsIterator.next(), DefaultEnum.DEFAULT_SIX);
		Assert.assertEquals(publicEnumsIterator.next(), DefaultEnum.DEFAULT_SEVEN);
		Assert.assertEquals(publicEnumsIterator.next(), DefaultEnum.DEFAULT_EIGHT);
		Assert.assertEquals(publicEnumsIterator.next(), DefaultEnum.DEFAULT_NINE);
		Assert.assertEquals(publicEnumsIterator.next(), DefaultEnum.DEFAULT_TEN);
		Assert.assertFalse(publicEnumsIterator.hasNext());
		Assert.assertNull(publicEnumsIterator.next());
	}
	
}
