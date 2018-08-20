package com.scheible.simplistictranspiler.transpiler.javac;

import com.scheible.simplistictranspiler.transpiler.jdkinternal.JdkInternalHelper;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 *
 * @author sj
 */
public class JavacHelper {
	
	private static class StringSource extends SimpleJavaFileObject {

		private String sourceCode;

		private StringSource(String className, String sourceCode) {
			super(URI.create("string:///" + className.replaceAll("\\.", "/") + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
			this.sourceCode = sourceCode;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
			return this.sourceCode;
		}
	}
	
	private static class JavacInput {

		private String className = null;
		private String sourceCode = null;
		private File[] files = null;
		
		private JavacInput(String className, String sourceCode) {
			this.className = className;
			this.sourceCode = sourceCode;			
		}
		
		private JavacInput(File[] files) {
			this.files = files;
		}		
		
		private Iterable<? extends JavaFileObject> get(StandardJavaFileManager fileManager) {
			if(this.files != null) {
				return fileManager.getJavaFileObjects(this.files);
			} else {
				return Arrays.asList(new StringSource(this.className, this.sourceCode));
			}
		}
	}
	
	static final ClassSourceResolver classSourceResolver = ClassSourceResolver.create();
	
	public static File resolve(String className, boolean includeTestSources) {
		return classSourceResolver.resolve(className, includeTestSources);
	}
	
	public static JavacResult parseAndAnalyse(String className, boolean includeTestSources) {
		return parseAndAnalyse(resolve(className, includeTestSources));
	}

	public static JavacResult parseAndAnalyse(String className, String sourceCode) {
		return parseAndAnalyse(new JavacInput(className, sourceCode));
	}

	public static JavacResult parseAndAnalyse(File file) {		
		return parseAndAnalyse(new JavacInput(new File[] {file}));
	}
	
	private static JavacResult parseAndAnalyse(JavacInput javacInput) {
		ensureToolsJar();
		try {
			final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

			final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			final Iterable<? extends JavaFileObject> sourceFiles = javacInput.get(fileManager);

			final CompilationTask javacTask = compiler.getTask(null, fileManager, null, null, null, sourceFiles);
			return new JavacResult(javacTask, JdkInternalHelper.parseAndAnalyze(javacTask));
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	private static void ensureToolsJar() {
		try {
			URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			boolean found = false;
			for (URL url : urlClassLoader.getURLs()) {
				if (url.getPath().endsWith("/tools.jar")) {
					found = true;
//					System.out.println("tools.jar already in classpath");
					break;
				}
			}

			if (!found) {
				Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
				method.setAccessible(true);

				File toolsLib = new File(System.getenv("JAVA_HOME"), "lib/tools.jar");
				if (toolsLib.exists()) {
					method.invoke(urlClassLoader, toolsLib.toURI().toURL());
//					System.out.println("updated classpath with: " + toolsLib);
				} else {
					throw new IllegalStateException("Can't find tools.jar in '" + toolsLib.getAbsolutePath() + "'! Is JAVA_HOME set?");
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | IllegalStateException | NoSuchMethodException | SecurityException | InvocationTargetException | MalformedURLException ex) {
			throw new IllegalStateException("Error while adding tools.jar to the classpath.", ex);
		}
	}
}
