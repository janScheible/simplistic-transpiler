package com.scheible.simplistictranspiler.transpiler.regression;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "native")
public class NativeJsTypeClass {
	
	public String bla;
	
	@JsProperty(name = "FOO")
	public static native int getFoo();
	
	@JsOverlay
	public void bla() {
		
	}
}
