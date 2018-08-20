package com.scheible.simplistictranspiler.transpiler.javac;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import java.util.function.Function;
import javax.tools.JavaCompiler;

/**
 *
 * @author sj
 */
public class JavacResult {

	private final CompilationUnitTree compilationUnitNode;
	private final Trees trees;

	JavacResult(JavaCompiler.CompilationTask task, CompilationUnitTree compilationUnitNode) {
		this.trees = Trees.instance(task);
		this.compilationUnitNode = compilationUnitNode;
	}

	public CompilationUnitTree getCompilationUnitNode() {
		return compilationUnitNode;
	}

	public Function<Tree, Long> getLineNumberResolver() {
		return (Tree node) -> {
			long pos = trees.getSourcePositions().getStartPosition(compilationUnitNode, node);
			long line = compilationUnitNode.getLineMap().getLineNumber(pos);
			return line;
		};
	}
}
