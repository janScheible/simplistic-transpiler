package com.scheible.simplistictranspiler.samplewebapp.gwt.client.jasmine;

import java.util.function.Consumer;
import java.util.function.Function;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 *
 * @author sj
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "window")
public class Jasmine {

	public static native <T> Matchers<T> expect(T actual);
	
	public static native <T> Matchers expect(Consumer<T> method);
	
	public static native <T, R> Matchers expect(Function<T, R> method);

	public static native <T> Spy spyOn(T obj, String methodName);
	
	public static native <T, T2> Spy spyOn(T obj, Consumer<T2> method);
	
	public static native <T, T2, R> Spy spyOn(T obj, Function<T2, R> method);
	
	public static native <T> Spy spyOnProperty(T obj, String propertyName, String accessType);

	public static native <T> Spy spyOnProperty(T obj, String propertyName);
}
