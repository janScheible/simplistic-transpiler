package com.scheible.simplistictranspiler.samplewebapp.gwt.client;

import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.ResultDto;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.TestRestService;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.framework.RestCallback;

/**
 *
 * @author sj
 */
public class MainModel {

	private final TestRestService testRestService;

	public MainModel(TestRestService testRestService) {
		this.testRestService = testRestService;
	}

	public void getResult(RestCallback<ResultDto> callback) {
		testRestService.getResult().then(callback);
	}
}
