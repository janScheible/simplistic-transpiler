package com.scheible.simplistictranspiler.transpiler.resolver;

import com.scheible.simplistictranspiler.transpiler.helper.TopLevelClass;
import java.util.Optional;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsType;
import com.scheible.simplistictranspiler.transpiler.api.IgnoredDependency;

/**
 *
 * @author sj
 */
public class DependencyResolver {

	//
	// customization of transpilation scope (means which classes are transpiled)
	//	
	public boolean isInTranspilationScope(TopLevelClass dependecy) {
		return !dependecy.isInterface()
				&& !dependecy.getAnnotationProvider().get(IgnoredDependency.class).isPresent()
				&& !dependecy.getName().startsWith("java.")
				&& doIncludeIfJsTypeClass(dependecy);
	}

	private boolean doIncludeIfJsTypeClass(TopLevelClass dependencyClass) {
		Optional<JsType> jsTypeAnnotation = dependencyClass.getAnnotationProvider().get(JsType.class);
		if (!jsTypeAnnotation.isPresent() || jsTypeAnnotation.get().isNative() == false) {
			return true;
		};

		if (dependencyClass.hasEnclosedClass()) {
			throw new TranspilerLimitationException("Native @JsType classes are not allowed to have any enclosed classes!");
		}

		if (dependencyClass.hasFieldAnnotatedWith(JsOverlay.class)
				|| dependencyClass.hasMethodAnnotatedWith(JsOverlay.class)) {
			return true;
		}

		return false;
	}
}
