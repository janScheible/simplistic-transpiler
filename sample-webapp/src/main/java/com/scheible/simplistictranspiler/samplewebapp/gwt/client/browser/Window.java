package com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "window")
public class Window {

	@JsProperty(name = "document")
	public static native Document getDocument();

	public static native void alert(String message);

	@JsProperty(name = "JSON")
	public static native Json getJson();

	@JsProperty(name = "location")
	public static native Location getLocation();
}
