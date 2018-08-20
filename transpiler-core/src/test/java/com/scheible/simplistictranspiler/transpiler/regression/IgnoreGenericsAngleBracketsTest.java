package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import org.junit.Test;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;

/**
 *
 * @author sj
 */
public class IgnoreGenericsAngleBracketsTest extends AbstractTranspilerTest {

	static class GenericClass<T> {

		public T get() {
			return null;
		}
	}

	static class ClassUnderTest {

		void methodUnderTest() {
			GenericClass<String> bla = new GenericClass<>();
			GenericClass<String> bla2 = new GenericClass<String>();
		}

		void test(String value) {

		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "let bla = new GenericClass();\n"
				+ "    let bla2 = new GenericClass();");
	}
}
