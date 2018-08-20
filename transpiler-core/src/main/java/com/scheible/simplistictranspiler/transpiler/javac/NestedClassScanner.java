package com.scheible.simplistictranspiler.transpiler.javac;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePathScanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author sj
 */
public class NestedClassScanner {

	public static List<ClassTree> scan(CompilationUnitTree node, Set<Tree.Kind> kinds) {
		ArrayList<ClassTree> result = new ArrayList<>();

		new TreePathScanner<Void, List<ClassTree>>() {
			@Override
			public Void visitClass(ClassTree node, List<ClassTree> classNodes) {
				if (kinds.contains(node.getKind())) {
					classNodes.add(node);
				}
				Void result = super.visitClass(node, classNodes);
				return result;
			}
		}.scan(node, result);

		return result;
	}
}
