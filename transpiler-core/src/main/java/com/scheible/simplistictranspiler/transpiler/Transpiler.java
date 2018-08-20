package com.scheible.simplistictranspiler.transpiler;

import com.scheible.simplistictranspiler.transpiler.visitor.TranspilingVisitor;
import com.scheible.simplistictranspiler.transpiler.javac.JavacHelper;
import com.scheible.simplistictranspiler.transpiler.javac.JavacResult;
import com.scheible.simplistictranspiler.transpiler.visitor.VisitorResult;
import java.io.File;
import java.util.ServiceLoader;

/**
 *
 * @author sj
 */
public class Transpiler {
	
	private static TranspilationResult transpile(JavacResult javacResult) {
		final TranspilingVisitor transpilingVisitor = ServiceLoader.load(TranspilingVisitor.class).iterator().next();
		VisitorResult visitorResult = transpilingVisitor.transpile(javacResult);
		return new TranspilationResult(visitorResult.getJavaScript(), visitorResult.getReport());
	}

	public static TranspilationResult transpile(String className, TranspilationScope scope) {
		return transpile(JavacHelper.parseAndAnalyse(className, scope == TranspilationScope.TEST));
	}
	
	static File resolve(String className, TranspilationScope scope) {
		return JavacHelper.resolve(className, scope == TranspilationScope.TEST);
	}
}
