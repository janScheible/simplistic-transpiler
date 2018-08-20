package com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework.AbstractJasmineTest.ControllerConfiguration;
import com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework.JasmineSpringTestRunner.InstanceTestClassListener;
import com.scheible.simplistictranspiler.samplewebapp.server.TranspilationController;
import com.scheible.simplistictranspiler.transpiler.TranspilationScope;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.scheible.simplistictranspiler.transpiler.api.IgnoredDependency;
import java.util.Arrays;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author sj
 */
@RunWith(JasmineSpringTestRunner.class)
@SpringBootTest(classes = {JasmineSpringBootTestApplication.class, ControllerConfiguration.class},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@IgnoredDependency
public abstract class AbstractJasmineTest implements InstanceTestClassListener {

	@TestConfiguration
	public static class ControllerConfiguration {

		@RestController
		public static class ResultController {

			static class SuiteDone {

				int specCount;

				public void setSpecCount(int specCount) {
					this.specCount = specCount;
				}
			}

			@PostMapping("/jasmine-spec-results")
			@ResponseBody
			String saveSpecDone(@RequestBody SpecResult specResult) {
				String id = specResult.id;
				logger.info("Received data for sepc " + id + ".");
				specResults.put(id, specResult);
				return "";
			}

			@PostMapping("/jasmine-suite-dones")
			@ResponseBody
			String suiteDone(@RequestBody SuiteDone suiteDone) {
				logger.info("Received Jasmine done data for " + suiteDone.specCount + " sepcs.");
				jasmineDoneCompletableFuture.complete(suiteDone);
				return "";
			}
			
			@PostMapping("/jasmine-runner-closes")
			@ResponseBody
			String runnerClose() {
				logger.info("Received Jasmine runner closed.");
				jasmineDoneCompletableFuture.complete(null);
				return "";
			}
		}

		@Bean
		TranspilationController transpilationController() {
			return new TranspilationController(TranspilationScope.TEST);
		}

	}

	private static final Logger logger = LoggerFactory.getLogger(AbstractJasmineTest.class);

	private static CompletableFuture jasmineDoneCompletableFuture;
	private static final Map<String, SpecResult> specResults = new ConcurrentHashMap<>();

	@Rule
	public final JasmineSpecMapperRule jasmineSpecMapperRule = new JasmineSpecMapperRule(specResults);

	private WebDriver driver;

	@LocalServerPort
	private int port;

	@Override
	public void beforeClassSetup() throws UnsupportedEncodingException, InterruptedException, ExecutionException, TimeoutException {
		WebDriverManager.chromedriver().setup();
		
		jasmineDoneCompletableFuture = new CompletableFuture();
		specResults.clear();

		ChromeOptions chromeOptions = new ChromeOptions();
		if(enableBrowserDebugging()) {
			chromeOptions.addArguments(Arrays.asList("--auto-open-devtools-for-tabs"));
		}
		driver = new ChromeDriver(chromeOptions);
		logger.info("Created WebDriver instance and getting Jasmine runner page.");

		final String testClassName = "./transpiler/" + getClass().getName().replaceAll(Pattern.quote("."), "/") + ".js";
		driver.get("http://localhost:" + port + "/jasmine-test-runner.html?test-class-name=" 
				+ URLEncoder.encode(testClassName, "UTF8") + "&debug=" + enableBrowserDebugging());
		logger.info("Got Jasmine runner page... waiting for the result...");
		jasmineDoneCompletableFuture.get();
		
		logger.info("Jasmine finished and collected all sepc results: " + specResults);
		driver.quit();
	}

	@Override
	public void afterClassSetup() {
	}
	
	protected boolean enableBrowserDebugging() {
		boolean isDebuggerAttached = java.lang.management.ManagementFactory.getRuntimeMXBean()
				.getInputArguments().toString().indexOf("-Xdebug") > 0;
		return isDebuggerAttached;
	}

	/**
	 * Test code shouldn't really run on server-side... do nothing!
	 */
	public void it(Runnable code) {
	}
}
