package com.scheible.simplistictranspiler.samplewebapp.gwt.client;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.dom.Element;
import com.scheible.simplistictranspiler.samplewebapp.gwt.client.element.CustomElement;
import com.scheible.simplistictranspiler.samplewebapp.gwt.client.element.CustomElementFactory;
import com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework.ViewWithUiHandlers;

/**
 *
 * @author sj
 */
public class MainView extends ViewWithUiHandlers<MainUiHandlers> implements MainPresenter.MyView {

	private CustomElement customElement;

	@Override
	public void attach(Element element) {
		customElement = CustomElementFactory.create("custom-element");
		element.appendChild(customElement);
		
		this.getUiHandlers().onViewAttached();
	}

	@Override
	public void showText(String text) {
		customElement.setText(text);
	}
}
