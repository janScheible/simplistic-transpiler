package com.scheible.simplistictranspiler.transpiler.javac;

import com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalHelper;
import com.sun.source.tree.Tree.Kind;
import java.util.EnumSet;
import java.util.stream.Collectors;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class NestedClassScannerTest {

	public enum MyEnum {

	}

	public interface MyFirstInterface {

	}

	public static class FirstLevelOne {

		public class FirstLevelTwo {

		}
	}

	public static class SecondLevelOne {

		public interface YetAnotherInterface {

		}
	}

	@Test
	public void testSomeMethod() {
		JavacResult result = JavacHelper.parseAndAnalyse(ClassSourceResolver.create().resolve(this.getClass().getName(), true));
		assertThat(NestedClassScanner.scan(result.getCompilationUnitNode(), EnumSet.of(Kind.CLASS, Kind.ENUM)).stream()
				.map(clazz -> JdkInternalHelper.getTopLevelRelativeName(clazz)).collect(Collectors.toList())).containsExactly(
				"NestedClassScannerTest", "NestedClassScannerTest.MyEnum", "NestedClassScannerTest.FirstLevelOne",
				"NestedClassScannerTest.FirstLevelOne.FirstLevelTwo",
				"NestedClassScannerTest.SecondLevelOne");
	}
}
