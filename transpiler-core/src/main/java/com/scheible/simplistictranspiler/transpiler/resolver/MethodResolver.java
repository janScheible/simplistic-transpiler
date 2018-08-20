package com.scheible.simplistictranspiler.transpiler.resolver;

import com.scheible.simplistictranspiler.transpiler.javac.TreeHelper;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import static com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalHelper.getOwnerFullName;
import com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalHelper;
import java.lang.annotation.Annotation;
import java.util.Optional;
import jsinterop.annotations.JsProperty;
import static com.scheible.simplistictranspiler.transpiler.resolver.MethodResolver.MethodAsFieldResolution.asFieldNamed;
import static com.scheible.simplistictranspiler.transpiler.resolver.MethodResolver.MethodResolution.named;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;
import com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalHelper.OwnerAwareAnnotationProvider;

/**
 *
 * @author sj
 */
public class MethodResolver {

	public interface MethodInvocationTransformer {

		void enableStatementExpressionSkipMode(IdentifierTree node);

		void markAsFieldConversion(MethodInvocationTree node);
	}

	static class MethodResolution {

		private final String name;

		private MethodResolution(String name) {
			this.name = name;
		}

		static MethodResolution named(String name) {
			return new MethodResolution(name);
		}
	}

	static class MethodAsFieldResolution extends MethodResolution {

		private MethodAsFieldResolution(String name) {
			super(name);
		}

		static MethodResolution asFieldNamed(String name) {
			return new MethodAsFieldResolution(name);
		}
	}

	private final static Set<String> METHODS_PREFIXES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("get", "set", "is", "has")));

	private final IdentifierScoper identifierScoper;

	public MethodResolver(IdentifierScoper identifierScoper) {
		this.identifierScoper = identifierScoper;
	}

	public String resolve(IdentifierTree identifierNode, MethodInvocationTree methodInvocationNode, MethodInvocationTransformer transformer,
			boolean identifierOwnerIsInTranspilationScope) {
		final String name = identifierNode.toString();
		final String ownerFullName = getOwnerFullName(identifierNode);

		if (TreeHelper.isSuperConstructorInvocation(identifierNode.getName()) && !identifierOwnerIsInTranspilationScope) {
			transformer.enableStatementExpressionSkipMode(identifierNode);
			return "will-be-removed-anyway";
		}

		return identifierScoper.getScope(identifierNode) + unwrapAndMark(resolve(ownerFullName, name,
				JdkInternalHelper.getAnnotationProvider(methodInvocationNode)), methodInvocationNode, transformer);
	}

	public String resolve(MemberSelectTree memberSelectNode, MethodInvocationTree methodInvocationNode, MethodInvocationTransformer transformer) {
		final String name = memberSelectNode.getIdentifier().toString();
		final String ownerFullName = getOwnerFullName(memberSelectNode);

		return unwrapAndMark(resolve(ownerFullName, name, JdkInternalHelper.getAnnotationProvider(methodInvocationNode)), methodInvocationNode, transformer);
	}

	private String unwrapAndMark(MethodResolution resolution, MethodInvocationTree methodInvocationNode, MethodInvocationTransformer transformer) {
		if (resolution instanceof MethodAsFieldResolution) {
			transformer.markAsFieldConversion(methodInvocationNode);
		}

		return resolution.name;
	}

	//
	// customization of methods (e.g. @JsFunction for renaming or @JsProperty for conversion to assignement)
	//
	private <A extends Annotation> MethodResolution resolve(String ownerFullName, String name, OwnerAwareAnnotationProvider annotationProvider) {
		if ("java.lang.String".equals(ownerFullName) && "concat".equals(name)) {
			return asFieldNamed("kiddelcatta");
		} else if ("java.util.Map".equals(ownerFullName) && "put".equals(name)) {
			return named("set");
		} else if ("java.util.Map".equals(ownerFullName) && "containsKey".equals(name)) {
			return named("has");
		}

		Optional<JsProperty> jsPropertyAnnotation = annotationProvider.get(JsProperty.class);
		String effectiveName = jsPropertyAnnotation.map(annotation -> "<auto>".equals(annotation.name())
				? stripMethodNamePrefix(name) : annotation.name()).orElse(name);
		return jsPropertyAnnotation.isPresent() ? asFieldNamed(effectiveName) : named(effectiveName);
	}

	private String stripMethodNamePrefix(String methodName) {
		Optional<String> foundPrefix = METHODS_PREFIXES.stream().filter(prefix -> methodName.startsWith(prefix)).findFirst();
		return foundPrefix.map(prefix -> methodName.substring(prefix.length(), prefix.length() + 1).toLowerCase()
				+ methodName.substring(prefix.length() + 1)).orElse(methodName);
	}

	public boolean doSkipMethodName(MemberSelectTree memberSelectNode) {
		final String name = memberSelectNode.getIdentifier().toString();
		final String ownerFullName = getOwnerFullName(memberSelectNode);

		return doSkipMethodName(ownerFullName, name, JdkInternalHelper.getAnnotationProvider(memberSelectNode));
	}

	//
	// customization method name skipping (e.g. @JsFunction)
	//
	private boolean doSkipMethodName(String ownerFullName, String name, OwnerAwareAnnotationProvider annotationProvider) {
		Optional<JsFunction> jsFunctionAnnotation = annotationProvider.getFromOwner(JsFunction.class);
		if (jsFunctionAnnotation.isPresent()) {
			return true;
		} else if ("java.lang.String".equals(ownerFullName) && "codePointCount".equals(name)) {
			return true;
		}

		return false;
	}

	public boolean isInTranspilationScope(MethodTree node) {
		return isInTranspilationScope(node.getName().toString(), JdkInternalHelper.getAnnotationProvider(node));
	}

	private boolean isInTranspilationScope(String methodName, OwnerAwareAnnotationProvider annotationProvider) {
		Optional<JsType> jsTypeAnnotation = annotationProvider.getFromOwner(JsType.class);

		if (jsTypeAnnotation.isPresent() && jsTypeAnnotation.get().isNative()) {
			Optional<JsProperty> jsPropertyAnnotation = annotationProvider.get(JsProperty.class);
			Optional<JsMethod> jsMethodAnnotation = annotationProvider.get(JsMethod.class);
			boolean isConstructor = "<init>".equals(methodName);

			return !isConstructor && !jsPropertyAnnotation.isPresent() && !jsMethodAnnotation.isPresent();
		} else {
			return true;
		}
	}
}
