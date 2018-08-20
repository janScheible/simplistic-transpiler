package com.scheible.simplistictranspiler.transpiler.jdkinternal;

import com.scheible.pocketsaw.api.SubModule;
import com.scheible.simplistictranspiler.transpiler.ExternalFunctionalities.SunSourceTree;
import com.scheible.simplistictranspiler.transpiler.ExternalFunctionalities.SunSourceUtil;
import com.scheible.simplistictranspiler.transpiler.ExternalFunctionalities.SunToolsJavac;
import com.scheible.simplistictranspiler.transpiler.helper.HelperSubModule;

/**
 *
 * @author sj
 */
@SubModule(uses = {HelperSubModule.class, SunSourceUtil.class, SunToolsJavac.class, SunSourceTree.class})
public final class JdkInternalSubModule {

	private JdkInternalSubModule() {
	}
}
