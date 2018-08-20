package com.scheible.simplistictranspiler.samplewebapp.vuejs.framework;

import java.util.function.Function;

/**
 *
 * @author sj
 */
public class VueOptions {

	public Function<CreateElementFunction, Object> render;

	public VueRouter router;

	public VueOptions setRender(Function<CreateElementFunction, Object> render) {
		this.render = render;
		return this;
	}

	public VueOptions setRouter(VueRouter router) {
		this.router = router;
		return this;
	}
}
