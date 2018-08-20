package com.scheible.simplistictranspiler.samplewebapp.vuejs.framework;

import java.util.function.Supplier;
import jsinterop.annotations.JsFunction;

/**
 *
 * @author sj
 */
@JsFunction
public interface DataSupplier<T> extends Supplier<T> {
	
}
