package com.scheible.simplistictranspiler.transpiler.helper;

import com.scheible.simplistictranspiler.transpiler.helper.TreePathHelper.ParentMatcherList;
import static com.scheible.simplistictranspiler.transpiler.helper.TreePathHelper.mandatory;
import static com.scheible.simplistictranspiler.transpiler.helper.TreePathHelper.optional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author sj
 */
public class TreePathHelperTest {

	@Test
	public void testSomeMethod() {
		assertThat(getParent(optional("b"), "c", Arrays.asList("a", "b", "c")))
				.isPresent().hasValue("c");
		assertThat(getParent(optional("b"), "c", Arrays.asList("a", "c")))
				.isPresent().hasValue("c");
		assertThat(getParent(optional("b").optional("c"), "d", Arrays.asList("a", "d")))
				.isPresent().hasValue("d");		
		
		assertThat(getParent(mandatory("b"), "c", Arrays.asList("a", "b", "c")))
				.isPresent().hasValue("c");
		assertThat(getParent(mandatory("b").mandatory("c"), "d", Arrays.asList("a", "b", "c", "d")))
				.isPresent().hasValue("d");		

		assertThat(getParent(mandatory("b").optional("c").mandatory("d"), "e", Arrays.asList("a", "b", "c", "d", "e")))
				.isPresent().hasValue("e");	
		assertThat(getParent(mandatory("b").optional("c").mandatory("d"), "e", Arrays.asList("a", "b", "d", "e")))
				.isPresent().hasValue("e");			
	}

	private Optional<String> getParent(ParentMatcherList<String> parentMatcherList, String lastMatcher, List<String> path) {
		return TreePathHelper.getParent(parentMatcherList.mandatory(lastMatcher), path, (String matchInfo, String current) -> {
			return matchInfo.equals(current);
		});
	}
}
