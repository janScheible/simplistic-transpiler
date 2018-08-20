package com.scheible.simplistictranspiler.samplewebapp.gwt.client.jasmine;

import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true)
public interface SpyStrategy {

	void returnValue(Object value);
}
