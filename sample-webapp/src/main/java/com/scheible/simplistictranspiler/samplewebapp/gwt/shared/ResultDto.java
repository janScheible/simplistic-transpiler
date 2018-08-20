package com.scheible.simplistictranspiler.samplewebapp.gwt.shared;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class ResultDto {

	public String name;

	@JsOverlay
	public static ResultDto create(String name) {
		ResultDto result = new ResultDto();
		result.name = name;
		return result;
	}
}
