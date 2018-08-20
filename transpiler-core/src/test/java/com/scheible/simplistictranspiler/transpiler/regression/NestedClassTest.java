package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class NestedClassTest extends AbstractTranspilerTest {

	@Test
	public void test() {
		assertThat(transpileClass(NestedClass.class)).isSemanticallyEquivalentTo(""
				+ "export default class NestedClass {\n"
				+ "    constructor() {\n"
				+ "    }\n"
				+ "}\n"
				+ "\n"
				+ "NestedClass.FirstLevelOne = class {\n"
				+ "    constructor() {\n"
				+ "    }\n"
				+ "}\n"
				+ "\n"
				+ "NestedClass.FirstLevelOne.FirstFirstLevelTwo = class {\n"
				+ "    constructor() {\n"
				+ "    }\n"
				+ "}\n"
				+ "\n"
				+ "NestedClass.SecondLevelOne = class extends NestedClass {\n"
				+ "    constructor() {\n"
				+ "        super();\n"
				+ "    }\n"
				+ "}");
	}
}
