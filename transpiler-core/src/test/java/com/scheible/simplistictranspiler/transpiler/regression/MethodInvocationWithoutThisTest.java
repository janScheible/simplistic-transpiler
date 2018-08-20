package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import org.junit.Test;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;

/**
 *
 * @author sj
 */
public class MethodInvocationWithoutThisTest extends AbstractTranspilerTest {

	static class ClassUnderTest {

		void methodUnderTest() {
			test(42);
			this.test(42);
		}
		
		void test(int value) {
			
		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "this.test(42);\n"
				+ "this.test(42);");
	}
}
