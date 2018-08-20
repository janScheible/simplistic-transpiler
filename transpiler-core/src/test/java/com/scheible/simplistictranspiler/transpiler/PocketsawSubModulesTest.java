package com.scheible.simplistictranspiler.transpiler;

import com.scheible.pocketsaw.impl.Pocketsaw;
import com.scheible.pocketsaw.impl.descriptor.FastClasspathScanner;
import java.io.IOException;
import java.net.MalformedURLException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class PocketsawSubModulesTest {

	private static Pocketsaw.AnalysisResult result;

	@BeforeClass
	public static void beforeClass() throws MalformedURLException, IOException {
		result = Pocketsaw.analizeCurrentProject(FastClasspathScanner.create(Transpiler.class));
	}
	
	@Test
	public void testNoDescriptorCycle() {
		assertThat(result.getAnyDescriptorCycle()).isEmpty();
	}

	@Test
	public void testNoCodeCycle() {
		assertThat(result.getAnyCodeCycle()).isEmpty();
	}

	@Test
	public void testNoIllegalCodeDependencies() {
		assertThat(result.getIllegalCodeDependencies()).isEmpty();
	}
}
