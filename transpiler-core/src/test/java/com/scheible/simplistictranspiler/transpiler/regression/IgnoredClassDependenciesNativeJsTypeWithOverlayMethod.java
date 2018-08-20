package com.scheible.simplistictranspiler.transpiler.regression;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true)
class IgnoredClassDependenciesNativeJsTypeWithOverlayMethod {
	
	@JsOverlay
	public void method() {
	}
	
}
