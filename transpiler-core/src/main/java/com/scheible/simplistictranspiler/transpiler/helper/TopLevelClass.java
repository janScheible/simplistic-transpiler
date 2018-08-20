package com.scheible.simplistictranspiler.transpiler.helper;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 *
 * @author sj
 */
public interface TopLevelClass extends Comparable<TopLevelClass> {

	String getName();

	String getSimpleName();
	
	boolean isInterface();

	boolean hasEnclosedClass();

	AnnotationProvider getAnnotationProvider();

	<A extends Annotation> boolean hasMethodAnnotatedWith(Class<A> annotationType);

	<A extends Annotation> boolean hasFieldAnnotatedWith(Class<A> annotationType);

	/**
	 * @param other
	 * @return The relative path leading from this top level class's directory (equals package in this case) to the
	 * other top level class's file.
	 */	
	default String getRelativePath(TopLevelClass other) {
		final String otherName = other.getName();
		int i;
		for (i = 0; i < Math.min(this.getName().lastIndexOf(".") + 1, otherName.lastIndexOf(".") + 1); i++) {
			if (this.getName().charAt(i) != otherName.charAt(i)) {
				break;
			}
		}

		String missingPart = this.getName().substring(i);
		int levelUpCount = missingPart.length() - missingPart.replaceAll(Pattern.quote("."), "").length();
		String levelUpPart = levelUpCount == 0 ? "./"
				: String.join("", Collections.nCopies(levelUpCount, "../"));

		String additionalPart = otherName.substring(i);
		additionalPart = additionalPart.replaceAll(Pattern.quote("."), "/");

		return levelUpPart + additionalPart;
	}
}
