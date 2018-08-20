package com.scheible.simplistictranspiler.transpiler.regression;

import static com.scheible.simplistictranspiler.transpiler.regression.NativeJsTypeClass.getFoo;
import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;
import static java.lang.System.out;

/**
 *
 * @author sj
 */
public class StaticImportedMethodTest extends AbstractTranspilerTest {

	static class ClassUnderTest {

		void methodUnderTest() {
			out.println(":-)");
			int foo = getFoo();
			int foo2 = NativeJsTypeClass.getFoo();
			int foo3 = com.scheible.simplistictranspiler.transpiler.regression.NativeJsTypeClass.getFoo();
		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "System.out.println(':-)');\n"
				+ "let foo = native.FOO;\n"
				+ "let foo2 = native.FOO;\n"
				+ "let foo3 = native.FOO;\n");
	}
}
