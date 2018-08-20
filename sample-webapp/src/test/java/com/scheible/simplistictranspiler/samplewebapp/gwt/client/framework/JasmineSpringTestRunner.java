package com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author sj
 */
public class JasmineSpringTestRunner extends SpringJUnit4ClassRunner {

	/**
	 * Idea which allows a beforeClass on instance level: https://saltnlight5.blogspot.com/2012/09/enhancing-spring-test-framework-with.html
	 */
	public interface InstanceTestClassListener {

		void beforeClassSetup() throws Exception;

		void afterClassSetup() throws Exception;
	}

	private InstanceTestClassListener instanceSetupListener;

	public JasmineSpringTestRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	protected Object createTest() throws Exception {
		Object test = super.createTest();
		// Note that JUnit4 will call this createTest() multiple times for each
		// test method, so we need to ensure to call "beforeClassSetup" only once.
		if (test instanceof InstanceTestClassListener && instanceSetupListener == null) {
			instanceSetupListener = (InstanceTestClassListener) test;
			instanceSetupListener.beforeClassSetup();
		}
		return test;
	}

	@Override
	public void run(RunNotifier notifier) {
		super.run(notifier);
		if (instanceSetupListener != null) {			
			try {
				instanceSetupListener.afterClassSetup();
			} catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
		}
	}
}
