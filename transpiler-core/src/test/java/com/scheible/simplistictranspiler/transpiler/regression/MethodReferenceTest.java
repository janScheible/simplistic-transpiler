package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import java.util.function.Consumer;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class MethodReferenceTest extends AbstractTranspilerTest {

	static class ClassUnderTest {

		void methodUnderTest() {
			targetMethod(this::referencedMethod);
		}

		void referencedMethod(String blub) {
		}

		void targetMethod(Consumer<String> consuer) {
		}
	}

	@Test
	public void methodLevelTest() {
		assertThat(transpileMethodBody()).isSemanticallyEquivalentTo(""
				+ "this.targetMethod(this.referencedMethod);");
	}
}
