package com.stresstest.selenium;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.collect.ImmutableSet;

public class WebDriverPool implements Closeable {

	final private long DEFAULT_POLL_TIMEOUT = 60000;

	final private Object lock = new Object();
	final private Set<WebDriverBrowser> browserBase;
	final private Set<WebDriver> driverBase;
	final private ConcurrentHashMap<WebDriverBrowser, WebDriver> browserDrivers = new ConcurrentHashMap<WebDriverBrowser, WebDriver>();

	public WebDriverPool(WebDriverBrowser... browsers) {
		this.browserBase = ImmutableSet.<WebDriverBrowser> copyOf(browsers);

		if (browserBase.contains(WebDriverBrowser.FIREFOX)) {
			try {
				FirefoxProfile firefoxProfile = new FirefoxProfile();
				DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
				desiredCapabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
				WebDriver driver = new FirefoxDriver(desiredCapabilities);
				browserDrivers.put(WebDriverBrowser.FIREFOX, driver);
			} catch (Throwable ignore) {
				ignore.printStackTrace();
			}
		}

		if (browserBase.contains(WebDriverBrowser.IE)) {
			try {
				DesiredCapabilities desiredCapabilities = DesiredCapabilities.internetExplorer();
				WebDriver driver = new InternetExplorerDriver(desiredCapabilities);
				browserDrivers.put(WebDriverBrowser.IE, driver);
			} catch (Throwable ignore) {
				ignore.printStackTrace();
			}
		}

		driverBase = ImmutableSet.<WebDriver> copyOf(browserDrivers.values());
	}

	public Set<WebDriverBrowser> getBrowserBase() {
		return browserBase;
	}

	public WebDriver poll(WebDriverBrowser browser) {
		return poll(browser, DEFAULT_POLL_TIMEOUT);
	}

	public WebDriver poll(WebDriverBrowser browser, long timeout) {
		final long waitTimeout = System.currentTimeMillis() + timeout;

		WebDriver webDriver = null;
		synchronized (lock) {
			do {
				browserDrivers.remove(browser);
				if (webDriver == null) {
					try {
						lock.wait(timeout);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			} while (webDriver == null && System.currentTimeMillis() < waitTimeout);
		}
		return webDriver;
	}

	public void put(WebDriverBrowser browser, WebDriver webDriver) {
		if (webDriver != null && browser != null && driverBase.contains(webDriver)) {
			browserDrivers.put(browser, webDriver);
		}
	}

	@Override
	public void close() throws IOException {
		for (WebDriver webDriver : driverBase) {
			try {
				webDriver.quit();
			} catch (Throwable ignore) {
				ignore.printStackTrace();
			}
		}
	}

}
