package com.scheible.simplistictranspiler.transpiler.regression;

public class StaticMembersClass {
	
	public static String bla;
	
	
	public static void bla() {
		
	}
	
	public static void blub() {
		StaticMembersClass.bla();
		String foo = StaticMembersClass.bla;
	}
}
