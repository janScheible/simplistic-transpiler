package com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.dom.Element;

/**
 *
 * @author sj
 */
public interface View {

	/**
	 * Invoked by a navigation action when the view has to be attachment initaly to the DOM.
	 */
	void attach(Element element);

	/**
	 * Lifecycle callback invoked when a view was already attached before navigation action and is still atttached after it.
	 */
	void onPreserve();

	/**
	 * Lifecycle callback invoked when a perviously attached view will be hidden aftern a navigation action.
	 */
	void onHide();
}
