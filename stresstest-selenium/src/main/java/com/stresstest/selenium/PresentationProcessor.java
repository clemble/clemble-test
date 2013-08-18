package com.stresstest.selenium;

import org.openqa.selenium.WebDriver;

public interface PresentationProcessor<T> {

	public T process(WebDriver webDriver);

}
