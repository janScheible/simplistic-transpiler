package com.scheible.simplistictranspiler.samplewebapp.gwt.client;

import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.framework.RestCallback;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.framework.RestResult;

/**
 *
 * @author sj
 */
public class TestRestResult<T> implements RestResult<T> {
	
	private final T body;

	public TestRestResult(T body) {
		this.body = body;
	}

	@Override
	public void then(RestCallback<T> callback) {
		callback.on(200, body);
	}	
}
