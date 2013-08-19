package com.stresstest.selenium;

public interface PresentationComparator<C, T> {

	public C compare(T first, T second);
}
