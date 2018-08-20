package com.scheible.simplistictranspiler.samplewebapp.gwt.shared.framework;

/**
 *
 * @author sj
 */
public interface RestResult<T> {
	
	void then(RestCallback<T> callback);
}
