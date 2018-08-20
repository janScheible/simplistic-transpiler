package com.scheible.simplistictranspiler.transpiler.regression;

import com.scheible.simplistictranspiler.transpiler.framework.AbstractTranspilerTest;
import static com.scheible.simplistictranspiler.transpiler.framework.TranspilationResultAssert.assertThat;
import com.scheible.simplistictranspiler.transpiler.resolver.TranspilerLimitationException;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class MultipleClassesTest extends AbstractTranspilerTest {
	
	@Test(expected = TranspilerLimitationException.class)
	public void test() {
		assertThat(transpileClass(MultipleClasses.class)).isSemanticallyEquivalentTo("");
	}
}
