package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class LambdaTest extends AbstractTranspilerTest {

	static class ClassUnderTest {

		void methodUnderTest() {
			BiConsumer<Integer, String> xyz = (Integer test, String bla) -> {
				int abc = 42;
				BiConsumer<String, Integer> kkkk = (String bla2, Integer val) -> {
					int fff = 13;
				};
			};
			int fff = 13;

			Consumer<String> aaa = (String bla) -> {
			};
		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "let xyz = (test, bla) => {\n"
				+ "    let abc = 42;\n"
				+ "    let kkkk = (bla2, val)=>{\n"
				+ "        let fff = 13;\n"
				+ "    };\n"
				+ "};\n"
				+ "let fff = 13;\n"
				+ "let aaa = (bla)=>{\n"
				+ "};");
	}
}
