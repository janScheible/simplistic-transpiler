package com.scheible.simplistictranspiler.transpiler.regression;

/**
 *
 * @author sj
 */
public class NestedClass {

	public static class FirstLevelOne {

		public static class FirstFirstLevelTwo {

		}
	}

	public static class SecondLevelOne extends FirstLevelOne {

	}

	public interface MyInterface {

	}
}
