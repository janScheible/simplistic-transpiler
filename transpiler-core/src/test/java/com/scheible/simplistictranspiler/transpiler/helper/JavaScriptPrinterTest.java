package com.scheible.simplistictranspiler.transpiler.helper;

import com.scheible.simplistictranspiler.transpiler.helper.JavaScriptPrinter;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author sj
 */
public class JavaScriptPrinterTest {
	
	@Test
	public void testRemovePreviouseLine() {
		assertThat(new JavaScriptPrinter().append("blabla\ntest\n").removePreviousLine().toString())
				.isEqualTo("blabla\n");
	}
}
