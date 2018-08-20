package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class IgnoredAnnotationsClassTest extends AbstractTranspilerTest {

	@Test
	public void test() {
		assertThat(transpileClass(IgnoredAnnotationsClass.class)).isSemanticallyEquivalentTo(""
				+ "export default class IgnoredAnnotationsClass {\n"
				+ "    \n"
				+ "    constructor() {\n"
				+ "    }\n"
				+ "    \n"
				+ "    foo() {\n"
				+ "    }\n"
				+ "}");
	}
}
