package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import org.junit.Test;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;

/**
 *
 * @author sj
 */
public class StringLiteralsWithSingleQuotesTest extends AbstractTranspilerTest {

	static class ClassUnderTest {

		void methodUnderTest() {
			test(":-)");
		}
		
		void test(String value) {
			
		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "this.test(':-)');");
	}
}
