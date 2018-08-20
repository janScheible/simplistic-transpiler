package com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.dom.Element;
import com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.dom.Node;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true)
public interface Document extends Node {
	
    @JsProperty
    Element getBody();	
    
    <T> T createElement(String tag);
	
    Element getElementById(String id);
}
