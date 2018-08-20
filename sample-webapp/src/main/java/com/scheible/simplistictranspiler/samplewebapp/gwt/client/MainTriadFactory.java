package com.scheible.simplistictranspiler.samplewebapp.gwt.client;

/**
 *
 * @author sj
 */
public class MainTriadFactory {
	
	static MainView create() {
		MainView mainView = new MainView();
		MainPresenter mainPresenter = new MainPresenter(mainView, new MainModel(new GwtTestRestService()));
		mainView.setUiHandlers(mainPresenter);
		return mainView;
	}
}
