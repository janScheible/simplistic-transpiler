package com.scheible.simplistictranspiler.transpiler.javac;

import com.sun.source.tree.ClassTree;
import com.sun.source.util.TreeScanner;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class JavacResultTest {
	
	@Test
	public void testLineNumberResolver() {
		JavacResult result = JavacHelper.parseAndAnalyse("test.Test", ""
				+ "package test;\n"
				+ "\n"
				+ "class Test {\n"
				+ "    static class Inner {\n"
				+ "    }\n"
				+ "    Test.Inner myInner;\n"
				+ "}\n");
		
		final Map<String, Long> classLineNumbers = new HashMap<>();
		
		new TreeScanner<Void, Void>() {
			@Override
			public Void visitClass(ClassTree node, Void unnused) {
				classLineNumbers.put(node.getSimpleName().toString(), result.getLineNumberResolver().apply(node));
				return super.visitClass(node, unnused);
			}
		}.scan(result.getCompilationUnitNode(), null);
		
		assertThat(classLineNumbers).hasSize(2).containsEntry("Test", 3l).containsEntry("Inner", 4l);
	}
}
