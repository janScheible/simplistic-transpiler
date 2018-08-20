package com.scheible.simplistictranspiler.samplewebapp.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import static com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.Window.getDocument;
import static com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.Window.getLocation;
import com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.event.Event;
import com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework.View;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sj
 */
public class GwtSampleEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		MainView mainView = MainTriadFactory.create();
		
		String defaultRoute = "/mvp";
		Map<String, View> viewMap = new HashMap<>();
		viewMap.put(defaultRoute, mainView);
		
		getDocument().addEventListener("hashchange", (Event event) -> {
			match(event.toString(), viewMap, defaultRoute);
		});
		
		View view = match(getLocation().getHash(), viewMap, defaultRoute);
		view.attach(getDocument().getBody());
		getLocation().setHash(defaultRoute);
	}
	
	private View match(String url, Map<String, View> viewMap, String defaultRoute) {
		if(viewMap.containsKey(url)) {
			return viewMap.get(url);
		} else {
			return viewMap.get(defaultRoute);
		}				
	}
}
