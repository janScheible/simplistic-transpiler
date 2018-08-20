package com.scheible.simplistictranspiler.transpiler.visitor;

/**
 *
 * @author sj
 */
public class VisitorResult {

	private final String javaScript;
	private final String report;

	public VisitorResult(String javaScript, String report) {
		this.javaScript = javaScript;
		this.report = report;
	}

	public String getJavaScript() {
		return javaScript;
	}

	public String getReport() {
		return report;
	}
}
