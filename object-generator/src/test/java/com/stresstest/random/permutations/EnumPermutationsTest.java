package com.stresstest.random.permutations;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

import com.stresstest.random.ObjectGenerator;
import com.stresstest.random.permutations.external.ExternalObjectWithDefaultEnum;
import com.stresstest.random.permutations.external.ExternalObjectWithPublicEnum;
import com.stresstest.random.permutations.external.ExternalPublicEnum;

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
	public void testObjectWithPublicEnumPermutations() {
		Iterable<ObjectWithPublicEnum> publicEnums = ObjectGenerator.getPossibleValues(ObjectWithPublicEnum.class);
		Iterator<ObjectWithPublicEnum> publicEnumsIterator = publicEnums.iterator();

		Assert.assertEquals(publicEnumsIterator.next().getPublicEnum(), PublicEnum.PUBLIC_ONE);
		Assert.assertEquals(publicEnumsIterator.next().getPublicEnum(), PublicEnum.PUBLIC_TWO);
		Assert.assertEquals(publicEnumsIterator.next().getPublicEnum(), PublicEnum.PUBLIC_THREE);
		Assert.assertEquals(publicEnumsIterator.next().getPublicEnum(), PublicEnum.PUBLIC_FOUR);
		Assert.assertEquals(publicEnumsIterator.next().getPublicEnum(), PublicEnum.PUBLIC_FIVE);
		Assert.assertEquals(publicEnumsIterator.next().getPublicEnum(), PublicEnum.PUBLIC_SIX);
		Assert.assertEquals(publicEnumsIterator.next().getPublicEnum(), PublicEnum.PUBLIC_SEVEN);
		Assert.assertEquals(publicEnumsIterator.next().getPublicEnum(), PublicEnum.PUBLIC_EIGHT);
		Assert.assertEquals(publicEnumsIterator.next().getPublicEnum(), PublicEnum.PUBLIC_NINE);
		Assert.assertFalse(publicEnumsIterator.hasNext());
		Assert.assertNull(publicEnumsIterator.next());
	}

	@Test
	public void testExternalPublicEnumPermutations() {
		Iterable<ExternalPublicEnum> publicEnums = ObjectGenerator.getPossibleValues(ExternalPublicEnum.class);
		Iterator<ExternalPublicEnum> publicEnumsIterator = publicEnums.iterator();

		Assert.assertEquals(publicEnumsIterator.next(), ExternalPublicEnum.PUBLIC_ONE);
		Assert.assertEquals(publicEnumsIterator.next(), ExternalPublicEnum.PUBLIC_TWO);
		Assert.assertEquals(publicEnumsIterator.next(), ExternalPublicEnum.PUBLIC_THREE);
		Assert.assertEquals(publicEnumsIterator.next(), ExternalPublicEnum.PUBLIC_FOUR);
		Assert.assertEquals(publicEnumsIterator.next(), ExternalPublicEnum.PUBLIC_FIVE);
		Assert.assertEquals(publicEnumsIterator.next(), ExternalPublicEnum.PUBLIC_SIX);
		Assert.assertEquals(publicEnumsIterator.next(), ExternalPublicEnum.PUBLIC_SEVEN);
		Assert.assertEquals(publicEnumsIterator.next(), ExternalPublicEnum.PUBLIC_EIGHT);
		Assert.assertEquals(publicEnumsIterator.next(), ExternalPublicEnum.PUBLIC_NINE);
		Assert.assertFalse(publicEnumsIterator.hasNext());
		Assert.assertNull(publicEnumsIterator.next());
	}
	
	@Test
	public void testExternalObjectWithPublicEnumPermutations() {
		Iterable<ExternalObjectWithPublicEnum> publicEnums = ObjectGenerator.getPossibleValues(ExternalObjectWithPublicEnum.class);
		Iterator<ExternalObjectWithPublicEnum> publicEnumsIterator = publicEnums.iterator();

		Assert.assertEquals(publicEnumsIterator.next().getExternalPublicEnum(), ExternalPublicEnum.PUBLIC_ONE);
		Assert.assertEquals(publicEnumsIterator.next().getExternalPublicEnum(), ExternalPublicEnum.PUBLIC_TWO);
		Assert.assertEquals(publicEnumsIterator.next().getExternalPublicEnum(), ExternalPublicEnum.PUBLIC_THREE);
		Assert.assertEquals(publicEnumsIterator.next().getExternalPublicEnum(), ExternalPublicEnum.PUBLIC_FOUR);
		Assert.assertEquals(publicEnumsIterator.next().getExternalPublicEnum(), ExternalPublicEnum.PUBLIC_FIVE);
		Assert.assertEquals(publicEnumsIterator.next().getExternalPublicEnum(), ExternalPublicEnum.PUBLIC_SIX);
		Assert.assertEquals(publicEnumsIterator.next().getExternalPublicEnum(), ExternalPublicEnum.PUBLIC_SEVEN);
		Assert.assertEquals(publicEnumsIterator.next().getExternalPublicEnum(), ExternalPublicEnum.PUBLIC_EIGHT);
		Assert.assertEquals(publicEnumsIterator.next().getExternalPublicEnum(), ExternalPublicEnum.PUBLIC_NINE);
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
	
	@Test
	public void testObjectWithDefaultEnumPermutations() {
		Iterable<ObjectWithDefaultEnum> publicEnums = ObjectGenerator.getPossibleValues(ObjectWithDefaultEnum.class);
		Iterator<ObjectWithDefaultEnum> publicEnumsIterator = publicEnums.iterator();

		Assert.assertEquals(publicEnumsIterator.next().getDefaultEnum(), DefaultEnum.DEFAULT_ONE);
		Assert.assertEquals(publicEnumsIterator.next().getDefaultEnum(), DefaultEnum.DEFAULT_TWO);
		Assert.assertEquals(publicEnumsIterator.next().getDefaultEnum(), DefaultEnum.DEFAULT_THREE);
		Assert.assertEquals(publicEnumsIterator.next().getDefaultEnum(), DefaultEnum.DEFAULT_FOUR);
		Assert.assertEquals(publicEnumsIterator.next().getDefaultEnum(), DefaultEnum.DEFAULT_FIVE);
		Assert.assertEquals(publicEnumsIterator.next().getDefaultEnum(), DefaultEnum.DEFAULT_SIX);
		Assert.assertEquals(publicEnumsIterator.next().getDefaultEnum(), DefaultEnum.DEFAULT_SEVEN);
		Assert.assertEquals(publicEnumsIterator.next().getDefaultEnum(), DefaultEnum.DEFAULT_EIGHT);
		Assert.assertEquals(publicEnumsIterator.next().getDefaultEnum(), DefaultEnum.DEFAULT_NINE);
		Assert.assertEquals(publicEnumsIterator.next().getDefaultEnum(), DefaultEnum.DEFAULT_TEN);
		Assert.assertFalse(publicEnumsIterator.hasNext());
		Assert.assertNull(publicEnumsIterator.next());
	}
	
	@Test
	public void testExternalObjectWithDefaultEnumPermutations() {
		Iterable<ExternalObjectWithDefaultEnum> publicEnums = ObjectGenerator.getPossibleValues(ExternalObjectWithDefaultEnum.class);
		Iterator<ExternalObjectWithDefaultEnum> publicEnumsIterator = publicEnums.iterator();

		Assert.assertNotNull(publicEnumsIterator.next().getExternalDefaultEnum());
		Assert.assertNotNull(publicEnumsIterator.next().getExternalDefaultEnum());
		Assert.assertNotNull(publicEnumsIterator.next().getExternalDefaultEnum());
		Assert.assertNotNull(publicEnumsIterator.next().getExternalDefaultEnum());
		Assert.assertNotNull(publicEnumsIterator.next().getExternalDefaultEnum());
		Assert.assertNotNull(publicEnumsIterator.next().getExternalDefaultEnum());
		Assert.assertNotNull(publicEnumsIterator.next().getExternalDefaultEnum());
		Assert.assertNotNull(publicEnumsIterator.next().getExternalDefaultEnum());
		Assert.assertNotNull(publicEnumsIterator.next().getExternalDefaultEnum());
		Assert.assertNotNull(publicEnumsIterator.next().getExternalDefaultEnum());
		Assert.assertFalse(publicEnumsIterator.hasNext());
		Assert.assertNull(publicEnumsIterator.next());
	}

}
