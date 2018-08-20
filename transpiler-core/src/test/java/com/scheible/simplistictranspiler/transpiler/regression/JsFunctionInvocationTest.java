package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import org.junit.Test;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import jsinterop.annotations.JsFunction;

/**
 *
 * @author sj
 */
public class JsFunctionInvocationTest extends AbstractTranspilerTest {

	static class ClassUnderTest {

		@JsFunction
		@FunctionalInterface
		interface Callback {
			
			void doIt(String value);
		}
		
		Callback callback;
		
		void methodUnderTest() {
			callback.doIt(":-)");
		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "this.callback(':-)');");
	}
}
