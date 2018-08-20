package com.scheible.simplistictranspiler.transpiler.helper;

import static com.scheible.simplistictranspiler.transpiler.helper.TreePathHelper.ParentConstraint.MANDATORY;
import static com.scheible.simplistictranspiler.transpiler.helper.TreePathHelper.ParentConstraint.OPTIONAL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 *
 * @author sj
 */
public class TreePathHelper {

	public static <T, I, M> Optional<T> getClosestParent(M matchInfo, Iterable<I> path, int maxDistance,
			BiFunction<M, I, Boolean> matcherFunction) {
		int distance = -1; // NOTE Starting at -1 cause the first node is the current node.
		for (I current : path) {
			if (distance != -1 && matcherFunction.apply(matchInfo, current)) {
				return Optional.of((T) current);
			}

			distance++;
			if (distance > maxDistance) {
				break;
			}
		}

		return Optional.empty();
	}

	public enum ParentConstraint {
		MANDATORY, OPTIONAL;
	}

	public static class ParentMatcherList<M> {

		private final List<M> matchInfos = new ArrayList<>();
		private final List<ParentConstraint> parentConstraints = new ArrayList<>();

		private ParentMatcherList(M matchInfo, ParentConstraint parentConstraint) {
			matchInfos.add(matchInfo);
			parentConstraints.add(parentConstraint);
		}

		public M getMatchInfo(int index) {
			return matchInfos.get(index);
		}

		public ParentConstraint getConstraint(int index) {
			return parentConstraints.get(index);
		}

		public int size() {
			return matchInfos.size();
		}

		public ParentMatcherList optional(M matchInfo) {
			matchInfos.add(matchInfo);
			parentConstraints.add(OPTIONAL);
			return this;
		}

		public ParentMatcherList mandatory(M matchInfo) {
			matchInfos.add(matchInfo);
			parentConstraints.add(MANDATORY);
			return this;
		}
	}

	public static <M> ParentMatcherList<M> optional(M matchInfo) {
		return new ParentMatcherList<>(matchInfo, OPTIONAL);
	}

	public static <M> ParentMatcherList<M> mandatory(M matchInfo) {
		return new ParentMatcherList<>(matchInfo, MANDATORY);
	}

	public static <T, I, M> Optional<T> getParent(ParentMatcherList<M> parentMatcherList, Iterable<I> path,
			BiFunction<M, I, Boolean> matcherFunction) {
		int index = -1; // NOTE Starting at -1 cause the first node is the current node.
		for (I current : path) {
			if (index >= 0) {
				boolean isMatching;

				// NOTE "Consume" all non matching optional parents.
				while (true) {
					isMatching = matcherFunction.apply(parentMatcherList.getMatchInfo(index), current);
					if (!isMatching && parentMatcherList.getConstraint(index) == OPTIONAL
							&& index < parentMatcherList.size() - 1) {
						index++;
					} else {
						break;
					}
				}
			
				if (index >= parentMatcherList.size()
						|| (parentMatcherList.getConstraint(index) == MANDATORY && !isMatching)) {
					return Optional.empty();
				} else if (index == parentMatcherList.size() - 1) {
					return Optional.of((T) current);
				}
			}

			index++;
		}

		return Optional.empty();
	}
}
