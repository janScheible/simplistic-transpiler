package com.scheible.simplistictranspiler.samplewebapp.vuejs;

import com.scheible.simplistictranspiler.samplewebapp.vuejs.component.CustomComponent;
import com.scheible.simplistictranspiler.samplewebapp.vuejs.framework.Route;
import com.scheible.simplistictranspiler.samplewebapp.vuejs.framework.Vue;
import com.scheible.simplistictranspiler.samplewebapp.vuejs.framework.VueOptions;
import com.scheible.simplistictranspiler.samplewebapp.vuejs.framework.VueRouter;
import com.scheible.simplistictranspiler.samplewebapp.vuejs.framework.VueRouterOptions;

/**
 *
 * @author sj
 */
public class Main {

	public static void run() {
		VueRouter router = new VueRouter(new VueRouterOptions(2)
				.setRoute(0, new Route("*").setRedirect("/component"))
				.setRoute(1, new Route("/component").setComponent(new CustomComponent()))
		);

		new Vue(new VueOptions()
				.setRouter(router)
				.setRender(h -> h.apply(new App()))
		).$mount("#app");
	}
}
