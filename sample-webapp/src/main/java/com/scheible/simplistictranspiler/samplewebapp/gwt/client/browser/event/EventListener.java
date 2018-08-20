package com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.event;

import jsinterop.annotations.JsFunction;

/**
 *
 * @author sj
 */
@JsFunction
@FunctionalInterface
public interface EventListener {

	void handleEvent(Event event);
}
