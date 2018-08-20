package com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.dom;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.event.EventTarget;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true)
public interface Node extends EventTarget {
	
	void appendChild(Object child);
}
