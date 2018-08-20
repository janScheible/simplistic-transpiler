package com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.event;

import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true)
public interface EventTarget {

	void addEventListener(String type, EventListener listener);
}
