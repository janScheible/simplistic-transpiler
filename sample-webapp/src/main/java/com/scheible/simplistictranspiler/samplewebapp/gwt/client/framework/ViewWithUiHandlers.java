package com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework;

/**
 *
 * @author sj
 */
public abstract class ViewWithUiHandlers<U> implements View {
	
	private U uiHandlers;

	@Override
	public void onPreserve() {
	}

	@Override
	public void onHide() {
	}
	
	public void setUiHandlers(U uiHandlers) {
		this.uiHandlers = uiHandlers;
	}
	
	protected U getUiHandlers() {
		return uiHandlers;
	}
}
