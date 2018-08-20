package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class MemberVariableInitializerTest extends AbstractTranspilerTest {

	@Test
	public void test() {
		assertThat(transpileClass(MemberVariableInitializer.class)).isSemanticallyEquivalentTo(""
				+ "export default class MemberVariableInitializer {\n"
				+ "    constructor() {\n"
				+ "        this.instanceValue = 42;\n"
				+ "    }\n"
				+ "}\n"
				+ "\n"
				+ "MemberVariableInitializer.staticValue = 42;\n");
	}
}
