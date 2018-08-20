package com.scheible.simplistictranspiler.transpiler.resolver;

import com.scheible.simplistictranspiler.transpiler.helper.AnnotationProvider;
import com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalHelper;
import com.sun.source.tree.IdentifierTree;
import java.util.Map;
import java.util.function.Function;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
public class IdentifierScoper {

	//
	// customization of scope classes names (e.g. '@JsType(isNative = true, name = "custom")')
	//
	final static Function<Map.Entry<String, AnnotationProvider>, String> JS_TYPE_NAME_TRANSFORMATOR = (Map.Entry<String, AnnotationProvider> entry) -> {
		return entry.getValue().get(JsType.class)
				.map(annotation -> "<auto>".equals(annotation.name()) ? entry.getKey() : annotation.name()).orElse(entry.getKey());
	};

	String getScope(final IdentifierTree node) {
		final String name = node.getName().toString();

		String scope = "";
		if (!JdkInternalHelper.isTopLevelClass(node)) {
			if (JdkInternalHelper.isStatic(node)) {
				scope = JdkInternalHelper.getOwnerTopLevelRelativeName(node, JS_TYPE_NAME_TRANSFORMATOR) + ".";
			} else if (JdkInternalHelper.isMemberVariable(node) && !"this".equals(name) && !"super".equals(name)) {
				scope = "this.";
			}
		}
		return scope;
	}
}
