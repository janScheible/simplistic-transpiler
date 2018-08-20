package com.scheible.simplistictranspiler.samplewebapp.gwt.client.element;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.dom.Element;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public interface CustomElement extends Element {

	@JsProperty
	public void setText(String text);
}
