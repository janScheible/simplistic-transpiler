package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import org.junit.Test;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;

/**
 *
 * @author sj
 */
public class StaticMemberAccessWithoutClassNameTest extends AbstractTranspilerTest {

	static class ClassUnderTest {

		private static int bla;

		void methodUnderTest() {
			bla = 42;
			ClassUnderTest.bla = 42;
			StaticMemberAccessWithoutClassNameTest.ClassUnderTest.bla = 42;
			foo();
			ClassUnderTest.foo();
			StaticMemberAccessWithoutClassNameTest.ClassUnderTest.foo();
		}

		static void foo() {

		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "StaticMemberAccessWithoutClassNameTest.ClassUnderTest.bla = 42;\n"
				+ "StaticMemberAccessWithoutClassNameTest.ClassUnderTest.bla = 42;\n"
				+ "StaticMemberAccessWithoutClassNameTest.ClassUnderTest.bla = 42;\n"
				+ "StaticMemberAccessWithoutClassNameTest.ClassUnderTest.foo();\n"
				+ "StaticMemberAccessWithoutClassNameTest.ClassUnderTest.foo();\n"
				+ "StaticMemberAccessWithoutClassNameTest.ClassUnderTest.foo();");
	}
}
