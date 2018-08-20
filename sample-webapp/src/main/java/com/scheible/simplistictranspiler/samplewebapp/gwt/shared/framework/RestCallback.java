package com.scheible.simplistictranspiler.samplewebapp.gwt.shared.framework;

import jsinterop.annotations.JsFunction;

/**
 *
 * @author sj
 */
@JsFunction
@FunctionalInterface
public interface RestCallback<T> {
	
	void on(int httpStatusCode, T body);
}
