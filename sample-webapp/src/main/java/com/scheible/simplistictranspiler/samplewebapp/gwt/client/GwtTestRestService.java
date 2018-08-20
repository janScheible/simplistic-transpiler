package com.scheible.simplistictranspiler.samplewebapp.gwt.client;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework.AjaxResult;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.ResultDto;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.TestRestService;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.framework.RestResult;

/**
 *
 * @author sj
 */
public class GwtTestRestService implements TestRestService {

	@Override
	public RestResult<ResultDto> getResult() {
		return new AjaxResult<>("http://localhost:8080/get-result");
	}	
}
