package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class ArrayTest extends AbstractTranspilerTest {

	static class ClassUnderTest {

		void methodUnderTest() {
			Object[] array = new Object[1];
			array[0] = ":-)";
		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "let array = new Array(1);\n"
				+ "array[0] = ':-)';"
		);
	}
}
