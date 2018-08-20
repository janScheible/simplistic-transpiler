package com.scheible.simplistictranspiler.transpiler.regression;

/**
 *
 * @author sj
 */
public enum EnumType {

	TEST(":-)"), BLA("...");
	
	private String value;

	private EnumType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
