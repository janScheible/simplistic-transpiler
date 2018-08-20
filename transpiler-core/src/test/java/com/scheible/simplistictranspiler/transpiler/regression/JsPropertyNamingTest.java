package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class JsPropertyNamingTest extends AbstractTranspilerTest {

	static class ClassUnderTest {

		void methodUnderTest() {
			IgnoredClassDependenciesNativeJsType nativeClass = getNativeJsType();
			String bla = nativeClass.getBla();
			nativeClass.setBla("...");
			String test = nativeClass.test;
			nativeClass.test = ":-)";
			nativeClass.getFoo();
		}

		IgnoredClassDependenciesNativeJsType getNativeJsType() {
			return (IgnoredClassDependenciesNativeJsType) new Object();
		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "let nativeClass = this.getNativeJsType();\n"
				+ "let bla = nativeClass.bla;\n"
				+ "nativeClass.bla = '...';\n"
				+ "let test = nativeClass.test;\n"
				+ "nativeClass.test = ':-)';\n"
				+ "nativeClass.FOO;");
	}
}
