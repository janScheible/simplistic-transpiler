package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class EmptyClassExtendingEmptyClassTest extends AbstractTranspilerTest {

	@Test
	public void test() {
		assertThat(transpileClass(EmptyClassExtendingEmptyClass.class)).isSemanticallyEquivalentTo(""
				+"import EmptyClass from './EmptyClass.js';\n"
				+"\n"
				+ "export default class EmptyClassExtendingEmptyClass extends EmptyClass {\n"
				+ "    constructor() {\n"
				+ "        super();\n"
				+ "    }\n"
				+ "}");
	}
}