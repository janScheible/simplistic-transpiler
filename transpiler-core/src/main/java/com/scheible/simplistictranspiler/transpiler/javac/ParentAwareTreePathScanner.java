package com.scheible.simplistictranspiler.transpiler.javac;

import com.scheible.simplistictranspiler.transpiler.helper.TreePathHelper;
import com.scheible.simplistictranspiler.transpiler.helper.TreePathHelper.ParentMatcherList;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author sj
 */
public class ParentAwareTreePathScanner<R extends Object, P extends Object> extends TreePathScanner<R, P> {

	protected <T extends Tree> Optional<T> getParent(Class<T> treeClass) {
		return ParentAwareTreePathScanner.getClosestParent(treeClass, getCurrentPath(), 0);
	}

	protected <T extends Tree> Optional<T> getClosestParent(Class<T> treeClass) {
		return ParentAwareTreePathScanner.getClosestParent(treeClass, getCurrentPath(), Integer.MAX_VALUE);
	}

	protected <T extends Tree> Optional<T> getClosestParent(Class<T> treeClass, int maxDistance) {
		return ParentAwareTreePathScanner.getClosestParent(treeClass, getCurrentPath(), maxDistance);
	}

	static <T extends Tree> Optional<T> getClosestParent(Class<T> treeClass, TreePath treePath, int maxDistance) {
		return TreePathHelper.getClosestParent(treeClass, treePath, maxDistance,
				(Class<T> lambdaTreeClass, Tree currentNode) -> lambdaTreeClass.isAssignableFrom(currentNode.getClass()));
	}
	
	protected <T extends Tree> Optional<T> getParent(ParentMatcherList<Class<? extends Tree>> parentMatcherList, Class<T> lastParentNodeClass) {
		return TreePathHelper.getParent(parentMatcherList.mandatory(lastParentNodeClass), getCurrentPath(),
				(Class<? extends Tree> lambdaTreeClass, Tree currentNode) -> lambdaTreeClass.isAssignableFrom(currentNode.getClass()));
	}
	
	protected List<Tree> getCurrentPathSnapshot() {
		List<Tree> result = new ArrayList<>();
		for(Tree current : getCurrentPath()) {
			result.add(current);
		}
		return result;
	}
}
