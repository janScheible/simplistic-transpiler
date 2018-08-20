package com.scheible.simplistictranspiler.transpiler.helper;

import java.util.regex.Pattern;

/**
 *
 * @author sj
 */
public class StringEscapeUtils {

	/**
	 * Inspired by org.apache.commons:commons-text's org.apache.commons.text.StringEscapeUtils.escapeEcmaScript(String).
	 */
	public static String escapeEcmaScript(final String input) {
		return input
				// JAVA_CTRL_CHARS_ESCAPE
				.replaceAll(Pattern.quote("\b"), "\\b")
				.replaceAll(Pattern.quote("\n"), "\\n")
				.replaceAll(Pattern.quote("\t"), "\\t")
				.replaceAll(Pattern.quote("\f"), "\\f")
				.replaceAll(Pattern.quote("\r"), "\\r")
				// ECMASCRIPT_ESCAPE
				.replaceAll(Pattern.quote("'"), "\\'")
				.replaceAll(Pattern.quote("\""), "\\\"")
				.replaceAll(Pattern.quote("\\"), "\\\\")
				.replaceAll(Pattern.quote("/"), "\\/");	
	}	
}
