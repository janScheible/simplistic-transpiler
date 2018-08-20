package com.scheible.simplistictranspiler.samplewebapp.gwt.client;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework.AbstractJasmineTest;
import static com.scheible.simplistictranspiler.samplewebapp.gwt.client.jasmine.Jasmine.expect;
import static com.scheible.simplistictranspiler.samplewebapp.gwt.client.jasmine.Jasmine.spyOn;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.ResultDto;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.TestRestService;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class MainPresenterTest extends AbstractJasmineTest {

	@Test
	public void testShowText() {
		it(() -> {
			MainView view = new MainView();
			spyOn(view, "showText");
			
			TestRestService testRestService = new GwtTestRestService();
			ResultDto resultDto = new ResultDto();
			resultDto.name = "James Bond";					
			spyOn(testRestService, "getResult").and().returnValue(new TestRestResult(resultDto));
			MainModel model = new MainModel(testRestService);			
			
			MainPresenter mainPresenter = new MainPresenter(view, model);
			mainPresenter.onViewAttached();
			
			expect(view::showText).toHaveBeenCalled();
		});
	}
}
