package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class NativeJsTypeClassTest extends AbstractTranspilerTest {

	@Test
	public void test() {
		assertThat(transpileClass(NativeJsTypeClass.class)).isSemanticallyEquivalentTo(""
				+ "export default class NativeJsTypeClass {\n"
				+ "    \n"
				+ "    bla() {\n"
				+ "    }\n"
				+ "}");
	}
}
