package com.scheible.simplistictranspiler.transpiler.visitor;

import com.scheible.simplistictranspiler.transpiler.resolver.TranspilerLimitationException;
import com.scheible.simplistictranspiler.transpiler.helper.JavaScriptPrinter;
import com.scheible.simplistictranspiler.transpiler.resolver.MethodResolver;
import com.scheible.simplistictranspiler.transpiler.resolver.ConstructorResolver;
import com.scheible.simplistictranspiler.transpiler.resolver.FieldResolver;
import com.scheible.simplistictranspiler.transpiler.resolver.DependencyResolver;
import com.scheible.simplistictranspiler.transpiler.helper.TopLevelClass;
import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.IntersectionTypeTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;
import java.util.stream.Collectors;
import com.scheible.simplistictranspiler.transpiler.helper.StringEscapeUtils;
import static com.scheible.simplistictranspiler.transpiler.helper.TreePathHelper.optional;
import com.scheible.simplistictranspiler.transpiler.javac.JavacResult;
import com.scheible.simplistictranspiler.transpiler.javac.NestedClassScanner;
import com.scheible.simplistictranspiler.transpiler.javac.TreeHelper;
import com.scheible.simplistictranspiler.transpiler.javac.TreeHelper.MemberVariableExtractionOption;
import static com.scheible.simplistictranspiler.transpiler.javac.TreeHelper.extractMemberVariables;
import com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalHelper;
import com.scheible.simplistictranspiler.transpiler.resolver.IdentifierScoper;
import com.sun.source.tree.Tree.Kind;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;

/**
 *
 * @author sj
 */
public class SimplisticTranspilingVisitor extends TranspilingVisitor<Void, Void> {

	private final JavaScriptPrinter output = new JavaScriptPrinter();

	private final IdentifierScoper identifierScoper = new IdentifierScoper();

	private final DependencyResolver dependencyResolver = new DependencyResolver();

	private final MethodResolver methodResolver = new MethodResolver(identifierScoper);
	private final FieldResolver fieldResolver = new FieldResolver(identifierScoper);
	private final ConstructorResolver constructorResolver = new ConstructorResolver();
	
	private final EnumCodeAugmentation enumCodeAugmentation = new EnumCodeAugmentation();

	private TranspilationContext context;

	@Override
	public VisitorResult transpile(JavacResult javacResult) {
		this.context = new TranspilationContext(javacResult.getLineNumberResolver());
		this.scan(javacResult.getCompilationUnitNode(), null);
		return new VisitorResult(output.toString(), context.getReport());
	}

	@Override
	public Void visitCompilationUnit(CompilationUnitTree node, Void unused) {
//		Void result = scan(node.getPackageAnnotations(), unused);
//		result = reduce(scan(node.getPackageName(), unused), result);
//		result = reduce(scan(node.getImports(), unused), result);

		List<ClassTree> classTypeDecls = node.getTypeDecls().stream()
				.filter(type -> type instanceof ClassTree).map(type -> (ClassTree) type).collect(Collectors.toList());
		if (classTypeDecls.size() > 1) {
			throw new TranspilerLimitationException("Only a single top level class per Java file is allowed.");
		}

		TopLevelClass currentClass = JdkInternalHelper.createTopLevelClass(classTypeDecls.get(0));

		boolean hasImports = false;
		List<TopLevelClass> dependencies = new ArrayList<>(JdkInternalHelper.collectClassDependencies(classTypeDecls.get(0)));
		Collections.sort(dependencies);
		for (TopLevelClass dependecy : dependencies) {
			if (dependencyResolver.isInTranspilationScope(dependecy)) {
				hasImports = true;
				output.append("import ").append(dependecy.getSimpleName()).append(" from '")
						.append(currentClass.getRelativePath(dependecy)).append(".js';\n");
			}
		}
		if (hasImports) {
			output.append("\n");
		}

		Void result = unused;
		// NOTE JavaScript does not yet support nested classes. Therfore the nested Java classes are ssigned to static
		//      members of the enclosing class.
		for (final ClassTree classNode : NestedClassScanner.scan(node, EnumSet.of(Kind.CLASS, Kind.ENUM))) {
			final boolean isTopLevelClass = JdkInternalHelper.isTopLevelClass(classNode);

			if (!isTopLevelClass) {
				output.append("\n");
			}

			if (classNode.getKind() != Kind.ENUM && !isTopLevelClass
					&& !classNode.getModifiers().getFlags().contains(Modifier.STATIC)) {
				throw new TranspilerLimitationException("Inner (non-static) classes are not supported!");
			}

			result = reduce(scan(classNode, unused), unused);
		}

		return result;
	}

	@Override
	public Void visitImport(ImportTree node, Void unused) {
		return scan(node.getQualifiedIdentifier(), unused);
	}

	@Override
	public Void visitClass(ClassTree node, Void unused) {
		Void result = scan(node.getModifiers(), unused);

		if (JdkInternalHelper.isTopLevelClass(node)) {
			output.append("export default class ").append(JdkInternalHelper.getTopLevelRelativeName(node));
		} else {
			output.append(JdkInternalHelper.getTopLevelRelativeName(node)).append(" = class");
		}
//		result = reduce(scan(node.getTypeParameters(), unused), result);
//		result = reduce(scan(node.getExtendsClause(), unused), result);

		final Optional<TopLevelClass> optionalExtendsClause = Optional.ofNullable(node.getExtendsClause())
				.map(JdkInternalHelper::createTopLevelClass);
		optionalExtendsClause.filter(extendsClause -> dependencyResolver.isInTranspilationScope(extendsClause))
				.ifPresent(extendedClassName -> output.append(" extends " + extendedClassName.getSimpleName()));
//		result = reduce(scan(node.getImplementsClause(), unused), result);
		output.append("\n").align().append("{").indent();

		//node.getModifiers().getFlags().contains(Modifier.STATIC);
		
		List<? extends Tree> methodMemberNodes = node.getMembers().stream().filter(memberNode -> {
			return memberNode instanceof MethodTree;
		}).collect(Collectors.toList());
		if(methodMemberNodes.stream().filter(memberNode -> TreeHelper.isConstructor((MethodTree)memberNode)).count() > 1) {
			throw new TranspilerLimitationException("Only a single constructor per class is allowed.");
		}
		
		result = reduce(scan(methodMemberNodes, unused), result);
		output.undent().align().append("}\n");
		
		// NOTE JavaScript does not yet support fields and therfore no static field initializers.
		//      This might change sooner or later: https://github.com/tc39/proposal-class-fields
		//      For now static fields are initialized after the class definition.
		result = visitStaticVariableInitializers(node, result);
		
		if (node.getKind() == Kind.ENUM) {
			enumCodeAugmentation.augment(JdkInternalHelper.getTopLevelRelativeName(node),
					extractMemberVariables(node.getMembers(), MemberVariableExtractionOption.STATIC, true), output);
		}
		
		return result;
	}

	private Void visitStaticVariableInitializers(ClassTree node, Void result) {
		boolean isFirstStaticInitializer = true;

		for (VariableTree variableNode : extractMemberVariables(node.getMembers(), 
				MemberVariableExtractionOption.STATIC, true)) {
			if (isFirstStaticInitializer) {
				output.append("\n");
				isFirstStaticInitializer = false;
			}

			output.align().append(JdkInternalHelper.getTopLevelRelativeName(node)).append(".")
					.append(variableNode.getName().toString()).append(" = ");
			result = scan(variableNode.getInitializer(), result);
			output.append(";\n");
		}

		return result;
	}

	private Void visitInstanceVariableInitializers(Void result) {
		for (VariableTree variableNode : extractMemberVariables(getClosestParent(ClassTree.class).get().getMembers(),
				MemberVariableExtractionOption.INSTANCE, true)) {
			output.align().append("this.").append(variableNode.getName().toString()).append(" = ");
			result = scan(variableNode.getInitializer(), result);
			output.append(";\n");
		}

		return result;
	}

	@Override
	public Void visitMethod(MethodTree node, Void unused) {
		if (methodResolver.isInTranspilationScope(node)) {
//			Void result = scan(node.getModifiers(), unused);
//			result = reduce(scan(node.getReturnType(), unused), result);
//			result = reduce(scan(node.getTypeParameters(), unused), result);
			output.append("\n").align();

			if (node.getModifiers().getFlags().contains(Modifier.STATIC)) {
				output.append("static ");
			}

			final String name = TreeHelper.isConstructor(node) ? "constructor" : node.getName().toString();
			output.append(name).append("(");
			Void result = scan(node.getParameters(), unused);
			output.append(")\n");
//			result = reduce(scan(node.getReceiverParameter(), unused), result);
//			result = reduce(scan(node.getThrows(), unused), result);
			result = reduce(scan(node.getBody(), unused), result);
//			result = reduce(scan(node.getDefaultValue(), unused), result);
			return result;
		} else {
			return unused;
		}
	}

	@Override
	public Void visitVariable(VariableTree node, Void unused) {
//		Void result = scan(node.getModifiers(), unused);
		Void result = unused;
//		result = reduce(scan(node.getType(), unused), result);

		List<? extends VariableTree> paramterNodes = Stream.of(
				getParent(LambdaExpressionTree.class).map(LambdaExpressionTree::getParameters),
				getParent(MethodTree.class).map(MethodTree::getParameters))
				.flatMap(o -> o.isPresent() ? o.get().stream() : Stream.empty()).collect(Collectors.toList());

		boolean isParamter = !paramterNodes.isEmpty() && paramterNodes.contains(node);

		if (isParamter && paramterNodes.indexOf(node) > 0) {
			output.append(", ");
		}

		output.align(!isParamter).append(!isParamter ? "let " : "");
		if (node.getNameExpression() == null) {
			output.append(node.getName());
		} else {
			result = reduce(scan(node.getNameExpression(), unused), result);
		}

		if (node.getInitializer() != null) {
			output.append(" = ");
		}
		result = reduce(scan(node.getInitializer(), unused), result);
		output.append(!isParamter ? ";\n" : "");
		return result;
	}

	@Override
	public Void visitEmptyStatement(EmptyStatementTree node, Void unused) {
		return null;
	}

	@Override
	public Void visitBlock(BlockTree node, Void unused) {
		output.align().append("{\n").indent();
		Void result = scan(node.getStatements(), unused);
		output.undent().align().append("}\n");
		return result;
	}

	@Override
	public Void visitDoWhileLoop(DoWhileLoopTree node, Void unused) {
		Void result = scan(node.getStatement(), unused);
		result = reduce(scan(node.getCondition(), unused), result);
		return result;
	}

	@Override
	public Void visitWhileLoop(WhileLoopTree node, Void unused) {
		Void result = scan(node.getCondition(), unused);
		result = reduce(scan(node.getStatement(), unused), result);
		return result;
	}

	@Override
	public Void visitForLoop(ForLoopTree node, Void unused) {
		Void result = scan(node.getInitializer(), unused);
		result = reduce(scan(node.getCondition(), unused), result);
		result = reduce(scan(node.getUpdate(), unused), result);
		result = reduce(scan(node.getStatement(), unused), result);
		return result;
	}

	@Override
	public Void visitEnhancedForLoop(EnhancedForLoopTree node, Void unused) {
		Void result = scan(node.getVariable(), unused);
		result = reduce(scan(node.getExpression(), unused), result);
		result = reduce(scan(node.getStatement(), unused), result);
		return result;
	}

	@Override
	public Void visitLabeledStatement(LabeledStatementTree node, Void unused) {
		return scan(node.getStatement(), unused);
	}

	@Override
	public Void visitSwitch(SwitchTree node, Void unused) {
		Void result = scan(node.getExpression(), unused);
		result = reduce(scan(node.getCases(), unused), result);
		return result;
	}

	@Override
	public Void visitCase(CaseTree node, Void unused) {
		Void result = scan(node.getExpression(), unused);
		result = reduce(scan(node.getStatements(), unused), result);
		return result;
	}

	@Override
	public Void visitSynchronized(SynchronizedTree node, Void unused) {
		Void result = scan(node.getExpression(), unused);
		result = reduce(scan(node.getBlock(), unused), result);
		return result;
	}

	@Override
	public Void visitTry(TryTree node, Void unused) {
		Void result = scan(node.getResources(), unused);
		result = reduce(scan(node.getBlock(), unused), result);
		result = reduce(scan(node.getCatches(), unused), result);
		result = reduce(scan(node.getFinallyBlock(), unused), result);
		return result;
	}

	@Override
	public Void visitCatch(CatchTree node, Void unused) {
		Void result = scan(node.getParameter(), unused);
		result = reduce(scan(node.getBlock(), unused), result);
		return result;
	}

	@Override
	public Void visitConditionalExpression(ConditionalExpressionTree node, Void unused) {
		Void result = scan(node.getCondition(), unused);
		result = reduce(scan(node.getTrueExpression(), unused), result);
		result = reduce(scan(node.getFalseExpression(), unused), result);
		return result;
	}

	@Override
	public Void visitIf(IfTree node, Void unused) {
		output.align().append("if(");
		Void result = scan(node.getCondition(), unused);
		output.append(")\n");
		result = reduce(scan(node.getThenStatement(), unused), result);
		if (node.getElseStatement() != null) {
			output.align().append("else\n");
			result = reduce(scan(node.getElseStatement(), unused), result);
		}
		return result;
	}

	@Override
	public Void visitExpressionStatement(ExpressionStatementTree node, Void unused) {
		output.align();
		Void result = scan(node.getExpression(), unused);
		output.append(";\n");

		if (context.isExpressionStatementSkipModeEnabled()) {
			output.removePreviousLine();
			context.disableStatementExpressionSkipMode(node);
		}

		if (TreeHelper.isInstanceInitializationPoint(node, getCurrentPath())) {
			// NOTE JavaScript does not yet support fields and therfore no instance field initializers.
			//      This might change sooner or later: https://github.com/tc39/proposal-class-fields
			//      For now the all instance fields with are initialized after the super class constructor invocation.
			result = visitInstanceVariableInitializers(result);
		}

		return result;
	}

	@Override
	public Void visitBreak(BreakTree node, Void unused) {
		return null;
	}

	@Override
	public Void visitContinue(ContinueTree node, Void unused) {
		return null;
	}

	@Override
	public Void visitReturn(ReturnTree node, Void unused) {
		output.align().append("return ");
		Void result = scan(node.getExpression(), unused);
		output.append(";\n");
		return result;
	}

	@Override
	public Void visitThrow(ThrowTree node, Void unused) {
		return scan(node.getExpression(), unused);
	}

	@Override
	public Void visitAssert(AssertTree node, Void unused) {
		Void result = scan(node.getCondition(), unused);
		result = reduce(scan(node.getDetail(), unused), result);
		return result;
	}

	@Override
	public Void visitMethodInvocation(MethodInvocationTree node, Void unused) {
//		Void result = scan(node.getTypeArguments(), unused);
		Void result = reduce(scan(node.getMethodSelect(), unused), unused);

		output.append(context.doConvertToField(node) ? node.getArguments().size() > 0 ? " = " : "" : "(");
		if (node.getArguments().size() > 0) {
			result = reduce(scan(node.getArguments().get(0), unused), result);
		}
		for (int i = 1; i < node.getArguments().size(); i++) {
			output.append(", ");
			result = reduce(scan(node.getArguments().get(i), unused), result);
		}
		output.append(context.doConvertToField(node) ? "" : ")");

		return result;
	}

	@Override
	public Void visitNewClass(NewClassTree node, Void unused) {
		Void result = scan(node.getEnclosingExpression(), unused);
		output.append("new ");
		result = reduce(scan(node.getIdentifier(), unused), result);
//		result = reduce(scan(node.getTypeArguments(), unused), result);
		output.append("(");
		for (int i = 0; i < node.getArguments().size(); i++) {
			if (i != 0) {
				output.append(", ");
			}
			result = reduce(scan(node.getArguments().get(i), unused), result);
		}
		output.append(")");
		result = reduce(scan(node.getClassBody(), unused), result);
		return result;
	}

	@Override
	public Void visitNewArray(NewArrayTree node, Void unused) {
		//Void result = scan(node.getType(), unused);
		output.append("new Array(");
		Void result = scan(node.getDimensions(), unused);
		output.append(")");
		result = reduce(scan(node.getInitializers(), unused), result);
//		result = reduce(scan(node.getAnnotations(), unused), result);
		for (Iterable< ? extends Tree> dimAnno : node.getDimAnnotations()) {
			result = reduce(scan(dimAnno, unused), result);
		}
		return result;
	}

	@Override
	public Void visitLambdaExpression(LambdaExpressionTree node, Void unused) {
		boolean isMultipleValueLambda = node.getBody() instanceof BlockTree;
		output.append("(");
		Void result = scan(node.getParameters(), unused);

		output.append(") => ");
		if (isMultipleValueLambda) {
			output.append("{\n").indent();
			result = reduce(scan(((BlockTree) node.getBody()).getStatements(), unused), result);
			output.undent().align().append("}");
		} else {
			result = reduce(scan(node.getBody(), unused), result);
		}

		return result;
	}

	@Override
	public Void visitParenthesized(ParenthesizedTree node, Void unused) {
		return scan(node.getExpression(), unused);
	}

	@Override
	public Void visitAssignment(AssignmentTree node, Void unused) {
		Void result = scan(node.getVariable(), unused);
		output.append(" = ");
		result = reduce(scan(node.getExpression(), unused), result);
		return result;
	}

	@Override
	public Void visitCompoundAssignment(CompoundAssignmentTree node, Void unused) {
		Void result = scan(node.getVariable(), unused);
		result = reduce(scan(node.getExpression(), unused), result);
		return result;
	}

	@Override
	public Void visitUnary(UnaryTree node, Void unused) {
		return scan(node.getExpression(), unused);
	}

	@Override
	public Void visitBinary(BinaryTree node, Void unused) {
		Void result = scan(node.getLeftOperand(), unused);
		if (Kind.EQUAL_TO == node.getKind()) {
			output.append(" === ");
		} else if (Kind.NOT_EQUAL_TO == node.getKind()) {
			output.append(" !== ");
		} else if (Kind.PLUS == node.getKind()) {
			output.append(" + ");
		} else {
			throw new IllegalStateException("Computer says NO!");
		}
		result = reduce(scan(node.getRightOperand(), unused), result);
		return result;
	}

	@Override
	public Void visitTypeCast(TypeCastTree node, Void unused) {
		Void result = scan(node.getType(), unused);
		result = reduce(scan(node.getExpression(), unused), result);
		return result;
	}

	@Override
	public Void visitInstanceOf(InstanceOfTree node, Void unused) {
		Void result = scan(node.getExpression(), unused);
		result = reduce(scan(node.getType(), unused), result);
		return result;
	}

	@Override
	public Void visitArrayAccess(ArrayAccessTree node, Void unused) {
		Void result = scan(node.getExpression(), unused);
		output.append("[");
		result = reduce(scan(node.getIndex(), unused), result);
		output.append("]");
		return result;
	}

	@Override
	public Void visitMemberSelect(MemberSelectTree node, Void unused) {
		Void result = scan(node.getExpression(), unused);

		Optional<MethodInvocationTree> parentMethodInvocationNode = getParent(MethodInvocationTree.class);
		boolean doSkipMethodName = parentMethodInvocationNode
				.map(methodInvocationNode -> methodResolver.doSkipMethodName(node)).orElse(false);
		boolean wasExpressionPartSkipped = context.isSkippedPackageIdentifier(node.getExpression());
		output.append(!doSkipMethodName && !wasExpressionPartSkipped ? "." : "");
		if (!doSkipMethodName && !JdkInternalHelper.isPackageIndentifier(node)) {
			output.append(parentMethodInvocationNode
					.map(methodInvocationNode -> methodResolver.resolve(node, methodInvocationNode, context))
					.orElseGet(() -> getParent(optional(ParameterizedTypeTree.class), NewClassTree.class)
					.map(newClassNode -> constructorResolver.resolve(node))
					.orElseGet(() -> fieldResolver.resolve(node))));
		} else {
			context.markAsSkippedPackageIdentifier(node);
		}
		return result;
	}

	@Override
	public Void visitMemberReference(MemberReferenceTree node, Void unused) {
		Void result = scan(node.getQualifierExpression(), unused);
		output.append(".");
		output.append(node.getName());
		result = reduce(scan(node.getTypeArguments(), unused), result);
		return result;
	}

	@Override
	public Void visitIdentifier(IdentifierTree node, Void unused) {
		if (!JdkInternalHelper.isPackageIndentifier(node)) {
			boolean identifierOwnerIsInTranspilationScope = JdkInternalHelper.createOwnerTopLevelClass(node)
					.map(ownerTopLevelClass -> dependencyResolver.isInTranspilationScope(ownerTopLevelClass)).orElse(false);
			output.append(getParent(MethodInvocationTree.class)
					.map(methodInvocationNode
							-> methodResolver.resolve(node, methodInvocationNode, context, identifierOwnerIsInTranspilationScope))
					.orElseGet(() -> getParent(optional(ParameterizedTypeTree.class), NewClassTree.class)
					.map(newClassNode -> constructorResolver.resolve(node))
					.orElseGet(() -> fieldResolver.resolve(node))));
		} else {
			context.markAsSkippedPackageIdentifier(node);
		}
		return null;
	}

	@Override
	public Void visitLiteral(LiteralTree node, Void unused) {
		if (Tree.Kind.STRING_LITERAL.equals(node.getKind())) {
			output.append("'").append(StringEscapeUtils.escapeEcmaScript(node.getValue().toString())).append("'");
		} else if (Tree.Kind.INT_LITERAL.equals(node.getKind())) {
			output.append(node.getValue().toString());
		} else if (Tree.Kind.NULL_LITERAL.equals(node.getKind())) {
			output.append("null");
		} else if (Tree.Kind.BOOLEAN_LITERAL.equals(node.getKind())) {
			output.append(node.getValue().toString());
		} else {
			throw new IllegalStateException("Computer says NO!");
		}
		return null;
	}

	@Override
	public Void visitPrimitiveType(PrimitiveTypeTree node, Void unused) {
		return null;
	}

	@Override
	public Void visitArrayType(ArrayTypeTree node, Void unused) {
		return scan(node.getType(), unused);
	}

	@Override
	public Void visitParameterizedType(ParameterizedTypeTree node, Void unused) {
		Void result = scan(node.getType(), unused);
//		result = reduce(scan(node.getTypeArguments(), unused), result);
		return result;
	}

	@Override
	public Void visitUnionType(UnionTypeTree node, Void unused) {
		return scan(node.getTypeAlternatives(), unused);
	}

	@Override
	public Void visitIntersectionType(IntersectionTypeTree node, Void unused) {
		return scan(node.getBounds(), unused);
	}

	@Override
	public Void visitTypeParameter(TypeParameterTree node, Void unused) {
//		Void result = scan(node.getAnnotations(), unused);
//		result = reduce(scan(node.getBounds(), unused), result);
		return unused;
	}

	@Override
	public Void visitWildcard(WildcardTree node, Void unused) {
		return scan(node.getBound(), unused);
	}

	@Override
	public Void visitModifiers(ModifiersTree node, Void unused) {
//		return scan(node.getAnnotations(), unused);
		return unused;
	}

	@Override
	public Void visitAnnotation(AnnotationTree node, Void unused) {
		Void result = scan(node.getAnnotationType(), unused);
		result = reduce(scan(node.getArguments(), unused), result);
		return result;
	}

	@Override
	public Void visitAnnotatedType(AnnotatedTypeTree node, Void unused) {
//		Void result = scan(node.getAnnotations(), unused);
		Void result = scan(node.getUnderlyingType(), unused);
		return result;
	}

	@Override
	public Void visitOther(Tree node, Void unused) {
		return null;
	}

	@Override
	public Void visitErroneous(ErroneousTree node, Void unused) {
		return null;
	}
}
