package com.scheible.simplistictranspiler.transpiler;

/**
 *
 * @author sj
 */
public class TranspilationResult {
	
	private final String javaScript;
	private final String report;

	public TranspilationResult(String javaScript, String report) {
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
