package com.scheible.simplistictranspiler.transpiler.visitor;

import com.scheible.simplistictranspiler.transpiler.resolver.MethodResolver.MethodInvocationTransformer;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author sj
 */
public class TranspilationContext implements MethodInvocationTransformer {

	private final Function<Tree, Long> lineNumberResolver;

	public TranspilationContext(Function<Tree, Long> lineNumberResolver) {
		this.lineNumberResolver = lineNumberResolver;
	}

	private static class StatementExpressionSkipModeLogEntry {

		private final IdentifierTree identifierNode;
		private final long identifierNodeLineNumber;
		private final ExpressionStatementTree expressionStatementNode;
		private final long expressionStatementNodeLineNumber;

		public StatementExpressionSkipModeLogEntry(IdentifierTree identifierNode, long identifierNodeLineNumber,
				ExpressionStatementTree expressionStatementNode, long expressionStatementNodeLineNumber) {
			this.identifierNode = identifierNode;
			this.identifierNodeLineNumber = identifierNodeLineNumber;
			this.expressionStatementNode = expressionStatementNode;
			this.expressionStatementNodeLineNumber = expressionStatementNodeLineNumber;
		}

		@Override
		public String toString() {
			return "trigger = " + this.identifierNode + ":" + this.identifierNodeLineNumber + ", removed = "
					+ this.expressionStatementNode + ":" + this.expressionStatementNodeLineNumber;
		}
	}

	private final List<MethodInvocationTree> convertedToFieldMethodInvocations = new ArrayList<>();
	private final List<Tree> skippedPackageIndentifier = new ArrayList<>();

	private final List<StatementExpressionSkipModeLogEntry> statementExpressionSkipModeLog = new ArrayList<>();
	private IdentifierTree currentExpressionStatementSkipModeIdentifierNode;
	private boolean expressionStatementSkipMode;

	@Override
	public void enableStatementExpressionSkipMode(IdentifierTree node) {
		if (this.expressionStatementSkipMode) {
			throw new IllegalStateException("Statement expression skip mode is already enabled!");
		}

		this.currentExpressionStatementSkipModeIdentifierNode = node;
		this.expressionStatementSkipMode = true;
	}

	public void disableStatementExpressionSkipMode(ExpressionStatementTree node) {
		if (!this.expressionStatementSkipMode) {
			throw new IllegalStateException("Statement expression skip mode was not enabled at all!");
		}

		this.expressionStatementSkipMode = false;
		this.statementExpressionSkipModeLog.add(new StatementExpressionSkipModeLogEntry(
				this.currentExpressionStatementSkipModeIdentifierNode,
				this.lineNumberResolver.apply(this.currentExpressionStatementSkipModeIdentifierNode),
				node, this.lineNumberResolver.apply(node)));
		this.currentExpressionStatementSkipModeIdentifierNode = null;
	}

	@Override
	public void markAsFieldConversion(MethodInvocationTree node) {
		this.convertedToFieldMethodInvocations.add(node);
	}

	public void markAsSkippedPackageIdentifier(IdentifierTree node) {
		skippedPackageIndentifier.add(node);
	}

	public void markAsSkippedPackageIdentifier(MemberSelectTree node) {
		skippedPackageIndentifier.add(node);
	}

	public boolean isExpressionStatementSkipModeEnabled() {
		return this.expressionStatementSkipMode;
	}

	public boolean doConvertToField(MethodInvocationTree node) {
		return convertedToFieldMethodInvocations.contains(node);
	}

	public boolean isSkippedPackageIdentifier(Tree node) {
		return skippedPackageIndentifier.contains(node);
	}

	public String getReport() {
		return "convertedToFieldMethodInvocations = "
				+ convertedToFieldMethodInvocations.stream().map(meth -> meth.toString() + ":" + lineNumberResolver.apply(meth)).collect(Collectors.toList()) + "\n"
				+ "skippedPackageIndentifier = "
				+ skippedPackageIndentifier.stream().map(node -> node.toString() + ":" + lineNumberResolver.apply(node)).collect(Collectors.toList()) + "\n"
				+ "statementExpressionSkipModeLog = " + statementExpressionSkipModeLog;
	}
}
