package com.scheible.simplistictranspiler.transpiler.javac;

import com.scheible.pocketsaw.api.SubModule;
import com.scheible.simplistictranspiler.transpiler.ExternalFunctionalities.SunSourceTree;
import com.scheible.simplistictranspiler.transpiler.ExternalFunctionalities.SunSourceUtil;
import com.scheible.simplistictranspiler.transpiler.helper.HelperSubModule;
import com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalSubModule;

/**
 *
 * @author sj
 */
@SubModule(uses = {JdkInternalSubModule.class, HelperSubModule.class, SunSourceUtil.class, SunSourceTree.class})
public final class JavacSubModule {

	private JavacSubModule() {
	}
}
