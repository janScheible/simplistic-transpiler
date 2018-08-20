package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class MemberVariableTest extends AbstractTranspilerTest {

	@Test
	public void test() {
		assertThat(transpileClass(MemberVariable.class)).isSemanticallyEquivalentTo(""
				+ "export default class MemberVariable {\n"
				+ "    \n"
				+ "    constructor() {\n"
				+ "        this.hi = ':-)';\n"
				+ "    }\n"
				+ "\n"
				+ "    foo() {\n"
				+ "        this.hi.trim();\n"
				+ "    }\n"
				+ "}");
	}
}
