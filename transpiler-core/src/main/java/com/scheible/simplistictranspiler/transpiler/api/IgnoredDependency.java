package com.scheible.simplistictranspiler.transpiler.api;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author sj
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {TYPE})
public @interface IgnoredDependency {

}
