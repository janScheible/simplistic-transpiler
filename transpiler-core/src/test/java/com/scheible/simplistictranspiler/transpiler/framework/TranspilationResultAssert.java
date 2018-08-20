package com.scheible.simplistictranspiler.transpiler.framework;

import org.assertj.core.api.AbstractAssert;

/**
 *
 * @author sj
 */
public class TranspilationResultAssert extends AbstractAssert<TranspilationResultAssert, String> {

	public TranspilationResultAssert(String actual) {
		super(actual, TranspilationResultAssert.class);
	}

	public static TranspilationResultAssert assertThat(String actual) {
		return new TranspilationResultAssert(actual);
	}
	
	public TranspilationResultAssert isSemanticallyEquivalentTo(String expected) {
		if(!areSemanticallyEquivalent(actual, expected)) {
			failWithMessage("Expected transpilation result to be <%s> but was <%s>", expected, actual);
		}
		
		return this;
	}
	
	private String removeFormatting(String source) {
		return source.replaceAll("\\s+", "");
	}

	private boolean areSemanticallyEquivalent(String firstSource, String secondSource) {
		return removeFormatting(firstSource).equals(removeFormatting(secondSource));
	}		
}
