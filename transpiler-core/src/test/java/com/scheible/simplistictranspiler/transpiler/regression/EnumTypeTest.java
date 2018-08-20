package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class EnumTypeTest extends AbstractTranspilerTest {

	@Test
	public void test() {
		assertThat(transpileClass(EnumType.class)).isSemanticallyEquivalentTo(""
				+ "export default class EnumType\n"
				+ "{\n"
				+ "    constructor(value)\n"
				+ "    {\n"
				+ "        this.value = value;\n"
				+ "    }\n"
				+ "\n"
				+ "    getValue()\n"
				+ "    {\n"
				+ "        return this.value;\n"
				+ "    }\n"
				+ "}\n"
				+ "\n"
				+ "EnumType.TEST = new EnumType(':-)');\n"
				+ "EnumType.BLA = new EnumType('...');\n"
				+ "EnumType.TEST.name = function () { return 'TEST'; };\n"
				+ "EnumType.TEST.toString = function () { return this.name(); };\n"
				+ "EnumType.BLA.name = function () { return 'BLA'; };\n"
				+ "EnumType.BLA.toString = function () { return this.name(); };\n"
				+ "EnumType.valueOf = function (name) {\n"
				+ "	   const enumConst = \n"
				+ "        EnumType.TEST.name() === name ? EnumType.TEST : \n"
				+ "        EnumType.BLA.name() === name ? EnumType.BLA : \n"
				+ "        null;\n"
				+ "    if (enumConst !== null) {\n"
				+ "        return enumConst;\n"
				+ "    } else {\n"
				+ "        throw new Error('java.lang.IllegalArgumentException: No enum constant \"' + name + '\".');\n"
				+ "    }\n"
				+ "};");
	}
}
