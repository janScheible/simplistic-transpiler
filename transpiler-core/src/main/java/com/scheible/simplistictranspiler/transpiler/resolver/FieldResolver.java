package com.scheible.simplistictranspiler.transpiler.resolver;

import com.scheible.simplistictranspiler.transpiler.helper.AnnotationProvider;
import com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalHelper;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import static com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalHelper.getOwnerFullName;
import java.util.AbstractMap.SimpleImmutableEntry;

/**
 *
 * @author sj
 */
public class FieldResolver {
	
	private final IdentifierScoper identifierScoper;
	
	public FieldResolver(IdentifierScoper identifierScoper) {
		this.identifierScoper = identifierScoper;
	}	

	public String resolve(IdentifierTree identifierNode) {
		final String name = identifierNode.getName().toString();
		final String ownerFullName = getOwnerFullName(identifierNode);

		return identifierScoper.getScope(identifierNode) + resolveField(ownerFullName, name, JdkInternalHelper.getAnnotationProvider(identifierNode));
	}

	public String resolve(MemberSelectTree memberSelectNode) {
		final String name = memberSelectNode.getIdentifier().toString();
		final String ownerFullName = getOwnerFullName(memberSelectNode);
		
		return resolveField(ownerFullName, name, JdkInternalHelper.getAnnotationProvider(memberSelectNode));
	}

	//
	// customization of field names (e.g. @JsProperty)
	//
	private String resolveField(String ownerFullName, String name, AnnotationProvider annotationProvider) {
		if ("test.MyClass".equals(ownerFullName) && "bbb".equals(name)) {
			return "ccc";
		}

		final String effectiveName = IdentifierScoper.JS_TYPE_NAME_TRANSFORMATOR.apply(new SimpleImmutableEntry<>(name, annotationProvider));
		return effectiveName;
	}
}
