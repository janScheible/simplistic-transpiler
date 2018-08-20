package com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.dom;

import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true)
public interface Element extends Node {

    void setAttribute(String name, Object value);
}
