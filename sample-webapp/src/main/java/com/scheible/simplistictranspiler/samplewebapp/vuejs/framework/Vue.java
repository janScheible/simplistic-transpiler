package com.scheible.simplistictranspiler.samplewebapp.vuejs.framework;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Vue {

	public Vue(VueOptions options) {
	}
	
	public native void $mount(String id);
}
