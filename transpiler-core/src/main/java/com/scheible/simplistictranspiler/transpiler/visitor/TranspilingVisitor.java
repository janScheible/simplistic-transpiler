package com.scheible.simplistictranspiler.transpiler.visitor;

import com.scheible.simplistictranspiler.transpiler.javac.JavacResult;
import com.scheible.simplistictranspiler.transpiler.javac.ParentAwareTreePathScanner;

/**
 *
 * @author sj
 */
public abstract class TranspilingVisitor <R extends Object, P extends Object> extends ParentAwareTreePathScanner<R, P> {
	
	public abstract VisitorResult transpile(JavacResult javacResult);
}
