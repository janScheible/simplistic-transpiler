package com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.request;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "XMLHttpRequest")
public class XMLHttpRequest {

    public native void open(String method, String url, boolean isAsync);

    public native void send(Object data);

    @JsProperty(name = "onreadystatechange")
    public native void setOnReadyStateChange(ReadyStateHandler handler);

    @JsProperty
    public native int getReadyState();

    @JsProperty
    public native int getStatus();
    
    @JsProperty
    public native String getStatusText();    

    @JsProperty
    public native String getResponseText();
}
