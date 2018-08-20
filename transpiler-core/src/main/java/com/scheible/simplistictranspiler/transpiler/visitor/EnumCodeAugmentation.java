package com.scheible.simplistictranspiler.transpiler.visitor;

import com.scheible.simplistictranspiler.transpiler.helper.JavaScriptPrinter;
import com.sun.source.tree.VariableTree;
import java.util.List;

/**
 *
 * @author sj
 */
public class EnumCodeAugmentation {

	public void augment(String topLevelRelativeName, List<VariableTree> enumConsts, JavaScriptPrinter out) {
		for (VariableTree enumConst : enumConsts) {
			out.align().append(topLevelRelativeName).append(".").append(enumConst.getName())
					.append(".name = function () { return '" + enumConst.getName() + "'; };\n");
			out.align().append(topLevelRelativeName).append(".").append(enumConst.getName())
					.append(".toString = function () { return this.name(); };\n");
		}

		out
				.align().append(topLevelRelativeName).append(".valueOf = function (name) {\n")
				.align().append("	   const enumConst = \n");

		for (VariableTree enumConst : enumConsts) {
			out.align().append("        EnumType." + enumConst.getName() + ".name() === name ? EnumType." + enumConst.getName() + " : \n");
		}

		out.align().append("        null;\n")
				.align().append("    if (enumConst !== null) {\n")
				.align().append("        return enumConst;\n")
				.align().append("    } else {\n")
				.align().append("        throw new Error('java.lang.IllegalArgumentException: No enum constant \"' + name + '\".');\n")
				.align().append("    }\n")
				.align().append("};");
	}
}
