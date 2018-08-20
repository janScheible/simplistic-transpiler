package com.scheible.simplistictranspiler.transpiler.helper;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 *
 * @author sj
 */
@FunctionalInterface
public interface AnnotationProvider {

	<A extends Annotation> Optional<A> get(Class<A> annotationType);
}
