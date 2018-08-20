package com.scheible.simplistictranspiler.transpiler.helper;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author sj
 */
public class TopLevelClassTest {

	@Test
	public void testOneSuperjacentLevel() {
		assertThat(create("com.test.bla.Blub").getRelativePath(create("com.test.Bla")))
				.isEqualTo("../Bla");
	}

	@Test
	public void testTwoSuperjacentLevel() {
		assertThat(create("com.test.bla.Blub").getRelativePath(create("com.Bla")))
				.isEqualTo("../../Bla");
	}

	@Test
	public void testSameLevel() {
		assertThat(create("com.test.bla.Blub").getRelativePath(create("com.test.bla.Bla")))
				.isEqualTo("./Bla");
	}

	@Test
	public void testOneSubjacentLevel() {
		assertThat(create("com.test.bla.Blub").getRelativePath(create("com.test.bla.test.Bla")))
				.isEqualTo("./test/Bla");
	}

	@Test
	public void testTwoSubjacentLevel() {
		assertThat(create("com.test.bla.Blub").getRelativePath(create("com.test.bla.test.foo.Bla")))
				.isEqualTo("./test/foo/Bla");
	}

	private TopLevelClass create(String name) {
		TopLevelClass topLevelClass = mock(TopLevelClass.class);
		when(topLevelClass.getRelativePath(any(TopLevelClass.class))).thenCallRealMethod();
		when(topLevelClass.getName()).thenReturn(name);
		return topLevelClass;
	}
}
