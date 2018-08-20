package com.scheible.simplistictranspiler.samplewebapp.gwt.client;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework.View;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.ResultDto;

/**
 *
 * @author sj
 */
public class MainPresenter implements MainUiHandlers {

	interface MyView extends View {

		void showText(String text);
	}

	private final MyView view;
	private final MainModel model;

	public MainPresenter(MainView view, MainModel model) {
		this.view = view;
		this.model = model;
	}
	
	@Override
	public void onViewAttached() {
		model.getResult((int httpStatusCode, ResultDto body) -> {
			view.showText(body.name);
		});
	}	
}
