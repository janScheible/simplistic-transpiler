package com.scheible.simplistictranspiler.transpiler.regression;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true)
class IgnoredClassDependenciesNativeJsType {
	
	@JsProperty
	public native String getBla();

	@JsProperty
	public native String setBla(String bla);
	@JsProperty
	public String test;

	@JsProperty(name = "FOO")
	public native int getFoo();	
}
