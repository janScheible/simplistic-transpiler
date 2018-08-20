package com.scheible.simplistictranspiler.transpiler.javac;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class ClassSourceResolverTest {

	private final ClassSourceResolver classSourceResolver = ClassSourceResolver.create();

	@Test
	public void testTestScope() {
		assertThat(classSourceResolver.tryResolve(getClass().getName(), true)).isPresent();
		assertThat(classSourceResolver.tryResolve(getClass().getName(), false)).isNotPresent();
	}

	@Test
	public void testMainScope() {
		assertThat(classSourceResolver.tryResolve(ClassSourceResolver.class.getName(), true)).isPresent();
		assertThat(classSourceResolver.tryResolve(ClassSourceResolver.class.getName(), false)).isPresent();
	}

	@Test
	public void testUnresolvableClass() {
		Assertions.assertThatThrownBy(() -> classSourceResolver.resolve("foo.bar.Bazinga", false))
				.isInstanceOf(IllegalStateException.class).hasMessageContaining("couldn't be resolved");
	}
}
