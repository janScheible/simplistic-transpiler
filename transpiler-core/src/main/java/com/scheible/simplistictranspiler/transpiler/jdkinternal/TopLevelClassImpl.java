package com.scheible.simplistictranspiler.transpiler.jdkinternal;

import com.scheible.simplistictranspiler.transpiler.helper.AnnotationProvider;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.scheible.simplistictranspiler.transpiler.helper.TopLevelClass;

/**
 *
 * @author sj
 */
class TopLevelClassImpl implements TopLevelClass {

	private final String name;
	private final String simpleName;
	private final boolean isInterface;
	private final boolean hasEnclosedClass;
	private final AnnotationProvider annotationProvider;

	private final Set<String> methodAnnotations;
	private final Set<String> fieldAnnotations;

	static TopLevelClass create(Tree node) {
		return create(TypeHelper.getTopLevelType(((JCTree) node).type));
	}
	
	static TopLevelClass create(Type type) {
		final Type topLevelType = TypeHelper.getTopLevelType(type);
		
		final String name = topLevelType.tsym.getQualifiedName().toString();
		final String simpleName = topLevelType.tsym.getSimpleName().toString();
		final boolean isInterface = topLevelType.tsym.isInterface();

		final AnnotationProvider annotationProvider = new AnnotationProvider() {
			@Override
			public <A extends Annotation> Optional<A> get(Class<A> annotationType) {
				return Optional.ofNullable(topLevelType.tsym.getAnnotation(annotationType));
			}
		};

		final Set<String> methodAnnotations = new HashSet<>();
		final Set<String> fieldAnnotations = new HashSet<>();
		boolean hasEnclosedClass = false;

		for (final Symbol enclosedElement : topLevelType.tsym.getEnclosedElements()) {
			Set<String> annotationSet = null;
			if (enclosedElement instanceof ClassSymbol) {
				hasEnclosedClass = true;
			} else if (enclosedElement instanceof MethodSymbol) {
				annotationSet = methodAnnotations;
			} else if (enclosedElement instanceof VarSymbol) {
				annotationSet = fieldAnnotations;
			}

			if (annotationSet != null) {
				annotationSet.addAll(enclosedElement.getAnnotationMirrors().stream()
						.map(annotationMirror -> annotationMirror.type.tsym.getQualifiedName().toString()).collect(Collectors.toList()));
			}
		}

		return new TopLevelClassImpl(name, simpleName, isInterface, hasEnclosedClass, annotationProvider, methodAnnotations, fieldAnnotations);
	}	

	TopLevelClassImpl(String name, String simpleName, boolean isInterface, boolean hasEnclosedClass,
			AnnotationProvider annotationProvider, Set<String> methodAnnotations, Set<String> fieldAnnotations) {
		this.name = name;
		this.simpleName = simpleName;
		this.isInterface = isInterface;
		this.hasEnclosedClass = hasEnclosedClass;
		this.annotationProvider = annotationProvider;
		this.methodAnnotations = methodAnnotations;
		this.fieldAnnotations = fieldAnnotations;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSimpleName() {
		return simpleName;
	}

	@Override
	public boolean isInterface() {
		return isInterface;
	}

	@Override
	public boolean hasEnclosedClass() {
		return hasEnclosedClass;
	}

	@Override
	public AnnotationProvider getAnnotationProvider() {
		return annotationProvider;
	}

	@Override
	public <A extends Annotation> boolean hasMethodAnnotatedWith(Class<A> annotationType) {
		return methodAnnotations.contains(annotationType.getName());
	}

	@Override
	public <A extends Annotation> boolean hasFieldAnnotatedWith(Class<A> annotationType) {
		return fieldAnnotations.contains(annotationType.getName());
	}

	@Override
	public int compareTo(TopLevelClass other) {
		return name.compareTo(other.getName());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.getClass().equals(this.getClass()) && name.equals(((TopLevelClassImpl) obj).getName());
	}

	@Override
	public String toString() {
		return name;
	}
}
