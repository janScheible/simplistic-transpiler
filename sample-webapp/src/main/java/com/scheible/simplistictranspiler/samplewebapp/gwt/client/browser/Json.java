package com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser;

import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true)
public interface Json {

    <T> T parse(String json);
}
