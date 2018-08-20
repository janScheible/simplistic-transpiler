package com.scheible.simplistictranspiler.transpiler.resolver;

import com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalHelper;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;

/**
 *
 * @author sj
 */
public class ConstructorResolver {

	public String resolve(IdentifierTree identifierNode) {
		final String className = identifierNode.toString();
		final String fullPackageName = JdkInternalHelper.getOwnerFullName(identifierNode);
		
		return resolveConstructor(fullPackageName, className);
	}	
	
	public String resolve(MemberSelectTree memberSelectNode) {
		final String className = memberSelectNode.getIdentifier().toString();
		final String fullPackageName = JdkInternalHelper.getOwnerFullName(memberSelectNode);
		
		return resolveConstructor(fullPackageName, className);
	}
	
	//
	// customization of constructor name (e.g. using '[]' instead of new ArrayList<>())
	//
	private String resolveConstructor(String fullPackageName, String className) {
		if("java.util".equals(fullPackageName) && "ArrayList".equals(className)) {
			return "Array";
		} else if("java.util".equals(fullPackageName) && "HashMap".equals(className)) {
			return "Map";
		} else {
			return className;
		}
	}
}
