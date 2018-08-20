package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class StaticMembersClassTest extends AbstractTranspilerTest {

	@Test
	public void methodLevelTest() {
		assertThat(transpileClass(StaticMembersClass.class)).isSemanticallyEquivalentTo(""
				+ "export default class StaticMembersClass {\n"
				+ "    \n"
				+ "    constructor() {\n"
				+ "    }\n"
				+ "    \n"
				+ "    static bla() {\n"
				+ "    }\n"
				+ "    \n"
				+ "    static blub() {\n"
				+ "        StaticMembersClass.bla();\n"
				+ "        let foo = StaticMembersClass.bla;\n"
				+ "    }\n"
				+ "}");
	}
}
