package com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true)
public interface Location {

	@JsProperty
	String getHash();

	@JsProperty
	void setHash(String anchorname);
}
