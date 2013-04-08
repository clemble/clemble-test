package com.stresstest.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CollectionAssert {
	
	public static <T> void assertNull(T value) {
		if(value instanceof String && ((String) value).length() == 0)
			return;
		if(value != null) {
			fail("Unexpectd \"" + value + "\" non null value");
		}
	}

	public static <T> void assertContains(Collection<T> values, T ... valuesArray) {
		assertContains(values, Arrays.asList(valuesArray));
	}
	
	public static <T> void assertContains(Collection<T> values, Collection<T> valuesArray) {
		if (values == null || values.size() == 0) {
			if(valuesArray == null || valuesArray.size() == 0)
				return;
			fail();
		}
		
		if(valuesArray == null) {
			fail("Null invalid for " + values);
		}
		
		Collection<T> missingValues = new ArrayList<T>();
		for(T value: valuesArray) {
			if(!values.contains(value))
				missingValues.add(value);
		}
		if(missingValues.size() > 0) {
			String errorMessage = missingValues + " missing from " + values;
			fail(errorMessage);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> void assertEmpty(Collection<T> values) {
		assertContent(values);
	}
	
	public static <T> void assertContent(Collection<T> firstCollection, Collection<T> secondCollection) {
		if (firstCollection == null) {
			if(secondCollection != null && secondCollection.size() > 0)
				fail();
			return;
		}
		
		ArrayList<T> missingFromFirstCollection = new ArrayList<T>(firstCollection.size());
		for(T value: secondCollection) {
			if(!firstCollection.contains(value))
				missingFromFirstCollection.add(value);
		}
		
		ArrayList<T> missingFromSecondCollection = new ArrayList<T>(firstCollection.size());
		for(T value: firstCollection) {
			if(!secondCollection.contains(value))
				missingFromSecondCollection.add(value);
		}
		
		if(missingFromFirstCollection.size() != 0 || missingFromSecondCollection.size() != 0) {
			fail(
				(missingFromFirstCollection.size() != 0 ? missingFromFirstCollection.size() + " missing (" + missingFromFirstCollection + ") ": "") + 
				(missingFromSecondCollection.size() != 0 ? missingFromSecondCollection.size() + " extra (" + missingFromSecondCollection + ") ": ""));
		}

	}
	
	public static <T> void assertContent(Collection<T> values, T ... valueArray) {
		if (valueArray == null) {
			if(values != null && values.size() > 0)
				fail();
			return;
		}
		
		assertContent(values, Arrays.asList(valueArray));
	}
	
	static public void fail() {
		fail(null);
	}
	
	static public void fail(String message) {
		if (message == null)
			throw new AssertionError();
		throw new AssertionError(message);
	}
}
