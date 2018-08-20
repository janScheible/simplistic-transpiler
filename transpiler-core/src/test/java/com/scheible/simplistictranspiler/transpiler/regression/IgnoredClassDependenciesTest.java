package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class IgnoredClassDependenciesTest extends AbstractTranspilerTest {

	@Test
	public void test() {
		assertThat(transpileClass(IgnoredClassDependencies.class)).isSemanticallyEquivalentTo(""
				+ "import IgnoredClassDependenciesNativeJsTypeWithOverlayField from './IgnoredClassDependenciesNativeJsTypeWithOverlayField.js';\n"
				+ "import IgnoredClassDependenciesNativeJsTypeWithOverlayMethod from './IgnoredClassDependenciesNativeJsTypeWithOverlayMethod.js';\n"
				+ "\n"
				+ "export default class IgnoredClassDependencies {\n"
				+ "    \n"
				+ "    constructor() {\n"
				+ "    }\n"
				+ "    \n"
				+ "     foo() {\n"
				+ "        let nativeJsType;\n"
				+ "        let nativeJsTypeWithOverlayField;\n"
				+ "        let nativeJsTypeWithOverlayMethod;\n"
				+ "        let justAnInterface;\n"
				+ "        let jdkList;\n"
				+ "    }\n"
				+ "}");
	}
}
