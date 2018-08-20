package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import org.junit.Test;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;

/**
 *
 * @author sj
 */
public class EmptyClassTest extends AbstractTranspilerTest {

	@Test
	public void test() {
		assertThat(transpileClass(EmptyClass.class)).isSemanticallyEquivalentTo(""
				+ "export default class EmptyClass {\n"
				+ "    constructor() {\n"
				+ "    }\n"
				+ "}");
	}
}
