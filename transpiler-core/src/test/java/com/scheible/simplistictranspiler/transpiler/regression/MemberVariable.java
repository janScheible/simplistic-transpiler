package com.scheible.simplistictranspiler.transpiler.regression;

public class MemberVariable {

	private String hi;

	MemberVariable() {
		hi = ":-)";
	}

	void foo() {
		hi.trim();
	}
}
