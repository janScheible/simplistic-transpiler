package com.scheible.simplistictranspiler.samplewebapp.vuejs.framework;

/**
 *
 * @author sj
 */
public class VueRouterOptions {

	public Route[] routes;

	public VueRouterOptions(int routeCount) {
		this.routes = new Route[routeCount];
	}

	public VueRouterOptions setRoute(int index, Route route) {
		routes[index] = route;
		return this;
	}
}
