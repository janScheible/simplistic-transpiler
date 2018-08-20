package com.scheible.simplistictranspiler.transpiler;

import com.scheible.pocketsaw.api.SubModule;
import com.scheible.simplistictranspiler.transpiler.javac.JavacSubModule;
import com.scheible.simplistictranspiler.transpiler.visitor.VisitorSubModule;

/**
 *
 * @author sj
 */
@SubModule(includeSubPackages = false, uses = {JavacSubModule.class, VisitorSubModule.class})
public final class TranspilerSubModule {

	private TranspilerSubModule() {
	}
}
