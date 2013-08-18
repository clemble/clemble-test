package com.stresstest.selenium;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.openqa.selenium.WebDriver;

public class WebPresentationComparator<C, T> implements Closeable {

	final private PresentationComparator<Boolean, T> DEFAULT_COMPARATOR = new PresentationComparator<Boolean, T>() {
		@Override
		public Boolean compare(T o1, T o2) {
			if (o1 == null) {
				return o2 == null ? true : false;
			} else {
				return o2 == null ? false : o1.equals(o2);
			}
		}

	};

	final private PresentationProcessor<T> presentationProcessor;
	final private PresentationComparator<C, T> resultComparator;
	final private WebDriverPool webDriverPool;

	final private ExecutorService executorService;

	public WebPresentationComparator(final PresentationProcessor<T> presentationProcessor) {
		this(presentationProcessor, null);
	}

	public WebPresentationComparator(final PresentationProcessor<T> presentationProcessor,
			final PresentationComparator<C, T> resultComparator) {
		this(presentationProcessor, resultComparator, WebDriverBrowser.values());
	}

	public WebPresentationComparator(final PresentationProcessor<T> presentationProcessor,
			final PresentationComparator<C, T> resultComparator, final WebDriverBrowser... webDriverBrowsers) {
		this.presentationProcessor = presentationProcessor;
		this.resultComparator = resultComparator;
		this.webDriverPool = new WebDriverPool(webDriverBrowsers);
		this.executorService = Executors.newFixedThreadPool(webDriverBrowsers.length);
	}

	public ProcessingResult<C, T> analyze(String url) {
		// Step 1. Generating tasks for processing
		Collection<Callable<Entry<WebDriverBrowser, T>>> processingTasks = new ArrayList<Callable<Entry<WebDriverBrowser, T>>>(webDriverPool.getBrowserBase().size());
		for(WebDriverBrowser driverBrowser: webDriverPool.getBrowserBase())
			processingTasks.add(new PresentationProcessorTask(driverBrowser, url));
		// Step 2. Checking processing results
		ProcessingResult<C, T> processingResult = new ProcessingResult<C, T>();
		try {
			List<Future<Entry<WebDriverBrowser, T>>> processingResults = executorService.invokeAll(processingTasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return processingResult;
	}
	
	public static class ProcessingResult <C, T> {
		
		final private Map<WebDriverBrowser, T> processingMap = new HashMap<WebDriverBrowser, T>();
		final private Map<Entry<WebDriverBrowser, WebDriverBrowser>, C> comparisonMap = new HashMap<Map.Entry<WebDriverBrowser,WebDriverBrowser>, C>();
		
		public void add(Entry<WebDriverBrowser, T> processingResult) {
			processingMap.put(processingResult.getKey(), processingResult.getValue());
			
		}
		
	}

	public class PresentationProcessorTask implements Callable<Entry<WebDriverBrowser, T>> {

		final private WebDriverBrowser browser;
		final private String url;

		public PresentationProcessorTask(WebDriverBrowser browserToUse, String url) {
			this.browser = browserToUse;
			this.url = url;
		}

		@Override
		public Entry<WebDriverBrowser, T> call() throws Exception {
			// Step 1. Fetching web driver
			WebDriver driver = webDriverPool.poll(browser);
			driver.get(url);
			// Step 2. Checking processing results
			return new ImmutablePair<WebDriverBrowser, T>(browser, presentationProcessor.process(driver));
		}

	}

	@Override
	public void close() throws IOException {
		webDriverPool.close();
	}

}
