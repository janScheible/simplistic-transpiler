package com.scheible.simplistictranspiler.samplewebapp.vuejs.framework;

import java.util.function.Function;
import jsinterop.annotations.JsFunction;

/**
 *
 * @author sj
 */
@JsFunction
public interface CreateElementFunction extends Function<Object, Object> {

	@Override
	Object apply(Object t);	
}
