package com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.request;

import jsinterop.annotations.JsFunction;

/**
 *
 * @author sj
 */
@JsFunction
@FunctionalInterface
public interface ReadyStateHandler {

    void onStateChanged(Object event);
}
