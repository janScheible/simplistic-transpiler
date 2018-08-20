package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class IgnoredImportedInterfacesTest extends AbstractTranspilerTest {

	@Test
	public void test() {
		assertThat(transpileClass(IgnoredImportedInterfaces.class)).isSemanticallyEquivalentTo(""
				+ "import EmptyClass from './EmptyClass.js';\n"
				+ "\n"
				+ "export default class IgnoredImportedInterfaces extends EmptyClass {\n"
				+ "    \n"
				+ "    constructor() {\n"
				+ "        super();\n"
				+ "    }\n"
				+ "}");
	}
}
