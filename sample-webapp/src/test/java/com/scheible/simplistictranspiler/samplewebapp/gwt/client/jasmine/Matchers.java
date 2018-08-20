package com.scheible.simplistictranspiler.samplewebapp.gwt.client.jasmine;

import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true)
public interface Matchers<T> {

	boolean toBe(T expected);
	
	boolean toHaveBeenCalled();
}
