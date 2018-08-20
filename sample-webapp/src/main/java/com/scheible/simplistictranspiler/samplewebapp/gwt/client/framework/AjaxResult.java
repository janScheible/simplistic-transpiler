package com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework;

import static com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.Window.alert;
import static com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.Window.getJson;
import com.scheible.simplistictranspiler.samplewebapp.gwt.client.browser.request.XMLHttpRequest;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.framework.RestCallback;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.framework.RestResult;

/**
 *
 * @author sj
 */
public class AjaxResult<T> implements RestResult<T> {

	private final String url;

	public AjaxResult(String url) {
		this.url = url;
	}
	
	@Override
	public void then(RestCallback<T> callback) {
		XMLHttpRequest request = new XMLHttpRequest();
        request.open("GET", this.url, true);
        request.setOnReadyStateChange(event -> {
            int state = request.getReadyState();
            if(state == 4) {
                if(request.getStatus() == 200) {
					callback.on(request.getStatus(), getJson().parse(request.getResponseText()));
                } else {
					alert("Error: " + request.getStatusText());
                }
            }
        });
        request.send(null);   
	}	
}
