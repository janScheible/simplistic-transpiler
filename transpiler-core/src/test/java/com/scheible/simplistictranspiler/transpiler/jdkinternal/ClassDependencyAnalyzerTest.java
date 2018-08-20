package com.scheible.simplistictranspiler.transpiler.jdkinternal;

import com.scheible.simplistictranspiler.transpiler.javac.JavacHelper;
import com.scheible.simplistictranspiler.transpiler.javac.JavacResult;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author sj
 */
public class ClassDependencyAnalyzerTest {

	@Test
	public void testOnlyTopLevelClassAndNotSelf() {
		JavacResult result = JavacHelper.parseAndAnalyse("test.Test", ""
				+ "package test;\n"
				+ "\n"
				+ "class Test {\n"
				+ "    static class Inner {\n"
				+ "    }\n"
				+ "    Test.Inner myInner;\n"
				+ "}\n");
		assertThat(ClassDependencyAnalyzer.getReferencedTypes(getTopLevelClass(result.getCompilationUnitNode())))
				.extracting("name").asList().containsExactly("java.lang.Object");
	}

	private ClassTree getTopLevelClass(CompilationUnitTree cu) {
		return cu.getTypeDecls().stream().filter(type -> type instanceof ClassTree).map(type -> (ClassTree)type).findFirst().get();
	}
}
