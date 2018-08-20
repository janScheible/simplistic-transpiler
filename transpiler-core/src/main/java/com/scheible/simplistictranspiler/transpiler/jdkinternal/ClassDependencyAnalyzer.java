package com.scheible.simplistictranspiler.transpiler.jdkinternal;

import com.scheible.simplistictranspiler.transpiler.helper.TopLevelClass;
import com.sun.source.tree.ClassTree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.TreeScanner;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author sj
 */
class ClassDependencyAnalyzer extends TreeScanner {

	private final Set<TopLevelClass> referencedTypes = new HashSet<>();
	private final TopLevelClass self;

	private ClassDependencyAnalyzer(Type topLevelType) {
		// NOTE Prevent the current top level class to be reported as a dependecy.
		this.self = TopLevelClassImpl.create(topLevelType);
	}

	static Set<TopLevelClass> getReferencedTypes(ClassTree node) {
		ClassDependencyAnalyzer classy = new ClassDependencyAnalyzer(((JCClassDecl) node).type);
		classy.scan((JCTree) node);
		return classy.referencedTypes;
	}

	@Override
	public void visitClassDef(JCClassDecl classDecl) {
		if (classDecl.extending != null) {
			super.scan(classDecl.extending);
		}
		super.visitClassDef(classDecl);
	}

	@Override
	public void visitIdent(JCTree.JCIdent ident) {
		if (ident.type instanceof ClassType) {
			add(ident.type);
		} else if(ident.sym.owner.type instanceof ClassType) {
			add(ident.sym.owner.type);
		}
		super.visitIdent(ident);
	}

	@Override
	public void visitNewClass(JCTree.JCNewClass newClass) {
		add(newClass.clazz.type);
		super.visitNewClass(newClass);
	}

	@Override
	public void visitSelect(JCTree.JCFieldAccess fieldAccess) {
		if (fieldAccess.type instanceof ClassType) {
			add(fieldAccess.type);
		} else if (fieldAccess.selected.type instanceof ClassType) {
			add(fieldAccess.selected.type);
		}
		super.visitSelect(fieldAccess);
	}

	private void add(final Type type) {
		TopLevelClass topLevelClass = TopLevelClassImpl.create(type);
		if (!this.self.equals(topLevelClass)) {
			referencedTypes.add(topLevelClass);
		}
	}
}
