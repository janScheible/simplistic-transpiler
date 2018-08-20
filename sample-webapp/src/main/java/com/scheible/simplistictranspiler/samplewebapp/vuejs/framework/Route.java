package com.scheible.simplistictranspiler.samplewebapp.vuejs.framework;

/**
 *
 * @author sj
 */
public class Route {

	public final String path;
	public Object component;
	public String redirect;

	public Route(String path) {
		this.path = path;
	}

	public Route setComponent(Object component) {
		this.component = component;
		return this;
	}

	public Route setRedirect(String redirect) {
		this.redirect = redirect;
		return this;
	}
}
