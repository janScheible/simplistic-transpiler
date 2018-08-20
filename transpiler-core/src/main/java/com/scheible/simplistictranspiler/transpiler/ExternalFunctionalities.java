package com.scheible.simplistictranspiler.transpiler;

import com.scheible.pocketsaw.api.ExternalFunctionality;

/**
 *
 * @author sj
 */
public class ExternalFunctionalities {

	@ExternalFunctionality(packageMatchPattern = "jsinterop.annotations.**")
	public static class JsInterop {

	}

	@ExternalFunctionality(packageMatchPattern = "com.sun.source.tree.**")
	public static class SunSourceTree {

	}

	@ExternalFunctionality(packageMatchPattern = "com.sun.source.util.**")
	public static class SunSourceUtil {

	}

	@ExternalFunctionality(packageMatchPattern = "com.sun.tools.javac.**")
	public static class SunToolsJavac {

	}
}
