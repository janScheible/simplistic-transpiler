package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import org.junit.Test;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;

/**
 *
 * @author sj
 */
public class InstanceMemberAccessWithoutThisTest extends AbstractTranspilerTest {

	static class ClassUnderTest {

		private int bla;

		void methodUnderTest() {
			bla = 42;
			this.bla = 42;
			foo();
			this.foo();
		}

		void foo() {

		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "this.bla = 42;\n"
				+ "this.bla = 42;\n"
				+ "this.foo();\n"
				+ "this.foo();");
	}
}
