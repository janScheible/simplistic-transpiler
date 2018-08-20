package com.scheible.simplistictranspiler.samplewebapp.vuejs.component;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.GwtTestRestService;
import com.scheible.simplistictranspiler.samplewebapp.gwt.shared.ResultDto;
import com.scheible.simplistictranspiler.samplewebapp.vuejs.framework.DataSupplier;

/**
 *
 * @author sj
 */
public class CustomComponent {
	
	public String name = "CustomComponent";

	private CustomComponentData _data = new CustomComponentData().setName("");
	public DataSupplier<CustomComponentData> data = () -> _data;

	public String template = "<div style=\"background-color: orange; font-family: Times, serif;\">{{name}}</div>";

	public Runnable mounted = () -> {
		new GwtTestRestService().getResult().then((int httpStatusCode, ResultDto body) -> {
			_data.name = body.name;
		});
	};
}
