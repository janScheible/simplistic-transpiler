package com.scheible.simplistictranspiler.samplewebapp.gwt.shared;

import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.framework.RestService;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.framework.RestResult;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author sj
 */
public interface TestRestService extends RestService {
	
	@GetMapping("/get-result")
	RestResult<ResultDto> getResult();
}
