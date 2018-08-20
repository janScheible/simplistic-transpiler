package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class IfStatementTest extends AbstractTranspilerTest {

	static class ClassUnderTest {

		void methodUnderTest() {
			int a = 42;
			if (a == 42) {
				doIf();
			} else {
				doElse();
			}
		}

		void doIf() {
		}

		void doElse() {
		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "let a = 42;\n"
				+ "if(a === 42) {\n"
				+ "    this.doIf();\n"
				+ "} else {\n"
				+ "    this.doElse();\n"
				+ "}");
	}
}
