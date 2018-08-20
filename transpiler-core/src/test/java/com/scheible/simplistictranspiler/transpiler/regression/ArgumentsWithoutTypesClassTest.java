package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class ArgumentsWithoutTypesClassTest extends AbstractTranspilerTest {

	@Test
	public void test() {
		assertThat(transpileClass(ArgumentsWithoutTypesClass.class)).isSemanticallyEquivalentTo(""
				+ "export default class ArgumentsWithoutTypesClass {\n"
				+ "    \n"
				+ "    constructor(a) {\n"
				+ "    }\n"
				+ "    \n"
				+ "    doIt(bla) {\n"
				+ "    }\n"
				+ "}");
	}
}
