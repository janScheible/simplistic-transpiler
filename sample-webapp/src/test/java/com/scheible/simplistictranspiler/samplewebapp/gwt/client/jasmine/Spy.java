package com.scheible.simplistictranspiler.samplewebapp.gwt.client.jasmine;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true)
public interface Spy {

	@JsProperty
	SpyStrategy and();
}
