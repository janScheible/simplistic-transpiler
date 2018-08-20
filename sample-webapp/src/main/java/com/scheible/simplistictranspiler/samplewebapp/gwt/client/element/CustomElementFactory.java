package com.scheible.simplistictranspiler.samplewebapp.gwt.client.element;

import static com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.Window.getDocument;

/**
 *
 * @author sj
 */
public abstract class CustomElementFactory {

	public static CustomElement create(String id) {
		CustomElement customElement = getDocument().createElement("custom-element");
		customElement.setAttribute("id", id);
		return customElement;
	}

	public static CustomElement findById(String id) {
		return (CustomElement) (getDocument().getElementById(id));
	}
}
