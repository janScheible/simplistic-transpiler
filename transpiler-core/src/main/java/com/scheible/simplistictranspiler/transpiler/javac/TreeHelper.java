package com.scheible.simplistictranspiler.transpiler.javac;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;

/**
 *
 * @author sj
 */
public class TreeHelper {

	public static boolean isConstructor(MethodTree node) {
		return "<init>".equals(node.getName().toString());
	}

	public static boolean isSuperConstructorInvocation(Name name) {
		return "super".equals(name.toString());
	}

	public static boolean isInstanceInitializationPoint(ExpressionStatementTree node, TreePath currentPath) {
		// TODO getParent(mandatory(BlockTree.class).mandatory(MethodTree.class), ClassTree.class) would be much nicer!
		boolean inExpressionStatementOfClassMethod
				= BlockTree.class.isAssignableFrom(currentPath.getParentPath().getLeaf().getClass())
				&& MethodTree.class.isAssignableFrom(currentPath.getParentPath().getParentPath().getLeaf().getClass())
				&& ClassTree.class.isAssignableFrom(currentPath.getParentPath().getParentPath().getParentPath().getLeaf().getClass());

		if (inExpressionStatementOfClassMethod) {
			MethodTree parentMethodNode = ParentAwareTreePathScanner.getClosestParent(MethodTree.class, currentPath, Integer.MAX_VALUE).get();
			if (TreeHelper.isConstructor(parentMethodNode)) {
				if (node.getExpression() instanceof MethodInvocationTree) {
					ExpressionTree methodSelectNode = ((MethodInvocationTree) node.getExpression()).getMethodSelect();
					if (methodSelectNode instanceof IdentifierTree) {
						if (TreeHelper.isSuperConstructorInvocation(((IdentifierTree) methodSelectNode).getName())) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public enum MemberVariableExtractionOption {
		STATIC, INSTANCE;
	}

	public static List<VariableTree> extractMemberVariables(List<? extends Tree> members,
			MemberVariableExtractionOption memberVariableExtractionOption, boolean onlyWithInitializer) {
		final List<VariableTree> result = new ArrayList<>();

		for (Tree memberNode : members) {
			if (memberNode instanceof VariableTree) {
				VariableTree variableNode = (VariableTree) memberNode;

				boolean isStatic = variableNode.getModifiers().getFlags().contains(Modifier.STATIC);
				boolean hasInitializer = variableNode.getInitializer() != null;

				boolean include = ((MemberVariableExtractionOption.STATIC == memberVariableExtractionOption && isStatic)
						|| (MemberVariableExtractionOption.INSTANCE == memberVariableExtractionOption && !isStatic))
						&& (onlyWithInitializer == hasInitializer);
				if (include) {
					result.add((VariableTree) memberNode);
				}
			}
		}

		return result;
	}
}
