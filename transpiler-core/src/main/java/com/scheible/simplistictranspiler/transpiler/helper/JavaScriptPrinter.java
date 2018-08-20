package com.scheible.simplistictranspiler.transpiler.helper;

/**
 *
 * @author sj
 */
public class JavaScriptPrinter {

	private final StringBuilder output = new StringBuilder();
	private int indentationLevel = 0;
	
	public JavaScriptPrinter align() {
		for(int i = 0; i < indentationLevel * 4; i++) {
			output.append(" ");
		}
		return this;
	}
	
	public JavaScriptPrinter align(boolean really) {
		return really ? this.align() : this;
	}
	
	public JavaScriptPrinter indent() {
		indentationLevel++;
		return this;
	}
	
	public JavaScriptPrinter undent() {
		indentationLevel--;
		return this;
	}

	public JavaScriptPrinter removePreviousLine() {
		int newlineCounter = 0;
		for(int i = this.output.length() - 1; i >= 0; i--) {
			if(this.output.charAt(i) == '\n') {
				newlineCounter++;
				if(newlineCounter == 2) {
					this.output.delete(i + 1, this.output.length());
					return this;
				}
			}
		}
		
		throw new IllegalStateException("Can't remove the previous line of '" + this.output + "'.");
	}
	
	public JavaScriptPrinter append(Object obj) {
		this.output.append(obj);
		return this;
	}

	@Override
	public String toString() {
		return this.output.toString();
	}
}
