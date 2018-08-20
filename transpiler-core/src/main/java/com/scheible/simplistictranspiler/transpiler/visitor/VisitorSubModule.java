package com.scheible.simplistictranspiler.transpiler.visitor;

import com.scheible.pocketsaw.api.SubModule;
import com.scheible.simplistictranspiler.transpiler.ExternalFunctionalities.SunSourceTree;
import com.scheible.simplistictranspiler.transpiler.ExternalFunctionalities.SunSourceUtil;
import com.scheible.simplistictranspiler.transpiler.helper.HelperSubModule;
import com.scheible.simplistictranspiler.transpiler.javac.JavacSubModule;
import com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalSubModule;
import com.scheible.simplistictranspiler.transpiler.resolver.ResolverSubModule;

/**
 *
 * @author sj
 */
@SubModule(uses = {ResolverSubModule.class, JdkInternalSubModule.class, JavacSubModule.class, 
	HelperSubModule.class, SunSourceTree.class, SunSourceUtil.class})
public final class VisitorSubModule {

	private VisitorSubModule() {
	}
}
