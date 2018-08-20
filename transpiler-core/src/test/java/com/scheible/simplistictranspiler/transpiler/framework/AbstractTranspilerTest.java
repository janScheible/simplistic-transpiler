package com.scheible.simplistictranspiler.transpiler.framework;

import com.scheible.simplistictranspiler.transpiler.TranspilationScope;
import com.scheible.simplistictranspiler.transpiler.javac.JavacHelper;
import com.scheible.simplistictranspiler.transpiler.Transpiler;
import com.scheible.simplistictranspiler.transpiler.javac.JavacResult;
import com.scheible.simplistictranspiler.transpiler.visitor.SimplisticTranspilingVisitor;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author sj
 */
public abstract class AbstractTranspilerTest {
	
	protected String transpileClass(Class<?> testClass) {
		return Transpiler.transpile(testClass.getName(), TranspilationScope.TEST).getJavaScript();
	}

	protected String transpileMethodBody() {
		return transpileInnerStaticClass(getClass().getName(), "ClassUnderTest", "methodUnderTest");
	}
	
	private String transpileInnerStaticClass(String className, String innerClassName, String methodName) {
		JavacResult javacResult = JavacHelper.parseAndAnalyse(className, true);
		final CompilationUnitTree compilationUnitNode = javacResult.getCompilationUnitNode();
		ClassTree innerClassNode = findInnerClassNode(compilationUnitNode.getTypeDecls(), innerClassName);

		try {
			Constructor<com.sun.tools.javac.util.List> listConstructor = com.sun.tools.javac.util.List.class.getDeclaredConstructor(Object.class, com.sun.tools.javac.util.List.class);
			listConstructor.setAccessible(true);
			com.sun.tools.javac.util.List defs = listConstructor.newInstance(innerClassNode, com.sun.tools.javac.util.List.nil());

			Field defsField = compilationUnitNode.getClass().getDeclaredField("defs");
			defsField.setAccessible(true);
			defsField.set(compilationUnitNode, defs);
		} catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
			throw new IllegalStateException(ex);
		}

		String output = new SimplisticTranspilingVisitor().transpile(javacResult).getJavaScript();
		final int methodStart = output.indexOf("\n    " + methodName + "()\n    {") + 13 + methodName.length();
		output = output.substring(methodStart, output.indexOf("\n    }", methodStart));
		output = output.trim().replaceAll(Pattern.quote("\n        "), "\n");
		return output;
	}

	private ClassTree findInnerClassNode(List<? extends Tree> typeDeclNodes, String innerClassName) {
		List<ClassTree> classNodes = typeDeclNodes.stream()
				.filter(typeDeclTree -> typeDeclTree instanceof ClassTree).map(node -> (ClassTree) node).collect(Collectors.toList());

		Optional<? extends Tree> result = classNodes.stream()
				.filter(classNode -> ((ClassTree) classNode).getSimpleName().toString().equals(innerClassName)).findFirst();
		if (result.isPresent()) {
			return (ClassTree) result.get();
		} else {
			for (ClassTree currentClassNode : classNodes) {
				return findInnerClassNode(currentClassNode.getMembers(), innerClassName);
			}
		}

		return null;
	}	
}
