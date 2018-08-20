package com.scheible.simplistictranspiler.transpiler.resolver;

import com.scheible.pocketsaw.api.SubModule;
import com.scheible.simplistictranspiler.transpiler.ExternalFunctionalities.JsInterop;
import com.scheible.simplistictranspiler.transpiler.ExternalFunctionalities.SunSourceTree;
import com.scheible.simplistictranspiler.transpiler.api.ApiSubModule;
import com.scheible.simplistictranspiler.transpiler.helper.HelperSubModule;
import com.scheible.simplistictranspiler.transpiler.javac.JavacSubModule;
import com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalSubModule;

/**
 *
 * @author sj
 */
@SubModule(uses={JavacSubModule.class, HelperSubModule.class, JdkInternalSubModule.class,
	ApiSubModule.class, JsInterop.class, SunSourceTree.class})
public final class ResolverSubModule {

	private ResolverSubModule() {
	}
}
