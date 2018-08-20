package com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework.SpecResult;
import static com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework.SpecResult.Status.FAILED;
import java.util.Map;
import junit.framework.AssertionFailedError;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 *
 * @author sj
 */
public class JasmineSpecMapperRule implements TestRule {

	private final Map<String, SpecResult> specResults;

	JasmineSpecMapperRule(Map<String, SpecResult> specResults) {
		this.specResults = specResults;
	}

	@Override
	public Statement apply(Statement base, Description description) {
		SpecResult result = specResults.get(description.getMethodName());
		if(result.status == FAILED) {
			return new Statement() {
				@Override
				public void evaluate() throws Throwable {
					throw new AssertionFailedError("Matcher '" + result.failedExpectations.get(0).matcherName
							+ "' " + result.failedExpectations.get(0).message.substring(0, 1).toLowerCase()
							+ result.failedExpectations.get(0).message.substring(1));
				}
			};
		}
		
		return base;
	}
	
}
