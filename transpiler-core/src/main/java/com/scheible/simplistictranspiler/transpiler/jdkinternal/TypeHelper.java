package com.scheible.simplistictranspiler.transpiler.jdkinternal;

import com.sun.tools.javac.code.Type;

/**
 *
 * @author sj
 */
public class TypeHelper {

	static Type getTopLevelType(Type type) {
		while (type.tsym.owner.type instanceof Type.ClassType) {
			type = type.tsym.owner.type;
		}

		return type;
	}

	/**
	 * 'foo.bar.Test.Inner' returns 'Test.Inner'.
	 */
	static String getTopLevelRelativeName(Type type) {
		String result = type.tsym.getSimpleName().toString();

		while (type.tsym.owner.type instanceof Type.ClassType) {
			type = type.tsym.owner.type;
			result = type.tsym.getSimpleName().toString() + "." + result;
		}

		return result;
	}
}
