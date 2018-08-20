package com.scheible.simplistictranspiler.samplewebapp.server;

import com.scheible.simplistictranspiler.samplewebapp.server.framework.ResponseResult;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.framework.RestResult;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.ResultDto;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.TestRestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sj
 */
@RestController
public class SpringMvcTestRestService implements TestRestService {

	@Override
	public RestResult<ResultDto> getResult() {
		return new ResponseResult(HttpStatus.OK, ResultDto.create("Darkwing Duck"));
	}	
}
