package com.scheible.simplistictranspiler.transpiler.jdkinternal;

import com.scheible.simplistictranspiler.transpiler.helper.AnnotationProvider;
import com.scheible.simplistictranspiler.transpiler.helper.TopLevelClass;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.tools.JavaCompiler;

/**
 *
 * @author sj
 */
public class JdkInternalHelper {

	public interface OwnerAwareAnnotationProvider extends AnnotationProvider {

		<B extends Annotation> Optional<B> getFromOwner(Class<B> annotationType);
	}

	public static OwnerAwareAnnotationProvider getAnnotationProvider(MethodInvocationTree node) {
		final Supplier<Symbol> getSymbol = () -> {
			JCExpression method = ((JCMethodInvocation) node).meth;
			if (method instanceof JCIdent) {
				return ((JCIdent) method).sym;
			} else if (method instanceof JCFieldAccess) {
				return ((JCFieldAccess) method).sym;
			} else {
				throw new IllegalStateException("Can't get symbol of'" + method.getClass().getSimpleName() + "'!");
			}
		};

		return new OwnerAwareAnnotationProvider() {
			@Override
			public <A extends Annotation> Optional<A> get(Class<A> annotationType) {
				return Optional.ofNullable(getSymbol.get().getAnnotation(annotationType));
			}

			@Override
			public <A extends Annotation> Optional<A> getFromOwner(Class<A> annotationType) {
				return Optional.ofNullable(getSymbol.get().owner.getAnnotation(annotationType));
			}
		};
	}

	public static OwnerAwareAnnotationProvider getAnnotationProvider(MemberSelectTree node) {
		return new OwnerAwareAnnotationProvider() {
			@Override
			public <A extends Annotation> Optional<A> get(Class<A> annotationType) {
				return Optional.ofNullable(((JCFieldAccess) node).sym.getAnnotation(annotationType));
			}

			@Override
			public <B extends Annotation> Optional<B> getFromOwner(Class<B> annotationType) {
				return Optional.ofNullable(((JCFieldAccess) node).sym.owner.getAnnotation(annotationType));
			}
		};
	}

	public static OwnerAwareAnnotationProvider getAnnotationProvider(MethodTree node) {
		return new OwnerAwareAnnotationProvider() {
			@Override
			public <A extends Annotation> Optional<A> get(Class<A> annotationType) {
				return Optional.ofNullable(((JCMethodDecl) node).sym.getAnnotation(annotationType));
			}

			@Override
			public <B extends Annotation> Optional<B> getFromOwner(Class<B> annotationType) {
				return Optional.ofNullable(((JCMethodDecl) node).sym.owner.getAnnotation(annotationType));
			}
		};
	}

	public static OwnerAwareAnnotationProvider getAnnotationProvider(IdentifierTree node) {
		return new OwnerAwareAnnotationProvider() {
			@Override
			public <A extends Annotation> Optional<A> get(Class<A> annotationType) {
				return Optional.ofNullable(((JCIdent) node).sym.getAnnotation(annotationType));
			}

			@Override
			public <B extends Annotation> Optional<B> getFromOwner(Class<B> annotationType) {
				return Optional.ofNullable(((JCIdent) node).sym.owner.getAnnotation(annotationType));
			}
		};
	}

	private static AnnotationProvider getAnnotationProvider(Type type) {
		return new AnnotationProvider() {
			@Override
			public <A extends Annotation> Optional<A> get(Class<A> annotationType) {
				return Optional.ofNullable(type.tsym.getAnnotation(annotationType));
			}
		};
	}

	public static String getFullName(ClassTree node) {
		return ((JCClassDecl) node).sym.getQualifiedName().toString();
	}

	public static String getOwnerFullName(MemberSelectTree node) {
		return ((JCFieldAccess) node).sym.owner.getQualifiedName().toString();
	}

	/**
	 * 'foo.bar.Test.Inner.myStaticMethod()' returns 'Test.Inner'.
	 */
	public static String getOwnerTopLevelRelativeName(IdentifierTree node, Function<Entry<String, AnnotationProvider>, String> nameTransformator) {
		final List<String> typeNames = new ArrayList<>();

		Type type = ((JCIdent) node).sym.owner.type;
		typeNames.add(nameTransformator.apply(new SimpleImmutableEntry<>(type.tsym.getSimpleName().toString(),
				getAnnotationProvider(type))));

		while (type.tsym.owner.type instanceof Type.ClassType) {
			type = type.tsym.owner.type;
			final Type currentType = type;
			typeNames.add(0, nameTransformator.apply(new SimpleImmutableEntry<>(type.tsym.getSimpleName().toString(),
					getAnnotationProvider(type))));
		}

		return typeNames.stream().collect(Collectors.joining("."));
	}
	
	public static String getTopLevelRelativeName(ClassTree node) {
		return TypeHelper.getTopLevelRelativeName(((JCClassDecl)node).type);
	}

	public static String getOwnerFullName(IdentifierTree node) {
		return ((JCIdent) node).sym.owner.getQualifiedName().toString();
	}

	public static Optional<TopLevelClass> createOwnerTopLevelClass(IdentifierTree node) {
		JCIdent identifier = (JCIdent) node;
		return Optional.ofNullable(identifier.sym.owner instanceof ClassSymbol
				? TopLevelClassImpl.create(identifier.sym.owner.type) : null);
	}

	public static String getOwnerFullName(MethodTree node) {
		return ((JCTree.JCMethodDecl) node).sym.owner.getQualifiedName().toString();
	}
	
	public static boolean isTopLevelClass(ClassTree node) {
		return ((JCClassDecl) node).sym.owner instanceof PackageSymbol;
	}

	public static boolean isTopLevelClass(IdentifierTree node) {
		return ((JCIdent) node).sym instanceof ClassSymbol && ((JCIdent) node).sym.owner instanceof PackageSymbol;
	}

	public static boolean isStatic(IdentifierTree node) {
		return ((JCIdent) node).sym.isStatic();
	}

	public static boolean isMemberVariable(IdentifierTree node) {
		return ((JCIdent) node).sym.owner instanceof ClassSymbol;
	}

	public static boolean isPackageIndentifier(IdentifierTree node) {
		return ((JCIdent) node).sym instanceof PackageSymbol;
	}

	public static boolean isPackageIndentifier(MemberSelectTree node) {
		return ((JCFieldAccess) node).sym instanceof PackageSymbol;
	}

	public static TopLevelClass createTopLevelClass(Tree node) {
		return TopLevelClassImpl.create(node);
	}

	public static Set<TopLevelClass> collectClassDependencies(ClassTree node) {
		return ClassDependencyAnalyzer.getReferencedTypes(node);
	}

	public static CompilationUnitTree parseAndAnalyze(JavaCompiler.CompilationTask task) throws IOException {
		final Iterable<? extends CompilationUnitTree> trees = ((JavacTask) task).parse();
		((JavacTask) task).analyze();
		CompilationUnitTree result = null;
		for (CompilationUnitTree tree : trees) {
			if (result == null) {
				result = tree;
			} else {
				throw new IllegalStateException("More than one compilation unit was found!");
			}

		}
		if (result == null) {
			throw new IllegalStateException("No compilation unit was found!");
		}
		return result;
	}
}
