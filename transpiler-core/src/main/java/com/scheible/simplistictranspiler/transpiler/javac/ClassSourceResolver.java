package com.scheible.simplistictranspiler.transpiler.javac;

import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author sj
 */
class ClassSourceResolver {

	private static final String DOT_PATTERN = Pattern.quote(".");

	private final File currentTestSourceDir;
	private final List<File> sourceDirs;

	private ClassSourceResolver(File currentTestSourceDir, List<File> sourceDirs) {
		this.currentTestSourceDir = currentTestSourceDir;
		this.sourceDirs = sourceDirs;
	}

	static ClassSourceResolver create() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();

		List<File> classpathEntries = new ArrayList<>();

		final Function<String, URL> urlConverter = string -> {
			try {
				return new URL(string);
			} catch (MalformedURLException ex) {
				throw new IllegalStateException(ex);
			}
		};

		if (cl instanceof URLClassLoader) {
			URL[] urls = ((URLClassLoader) cl).getURLs();

			if (urls.length == 1) {
				// NOTE Most likely started by Maven Surefire Plugin, means we have to determince classpath via Manifest.
				try {
					File file = new File(urls[0].getFile());
					Manifest m = new JarFile(file).getManifest();

					classpathEntries.addAll(Arrays.asList(m.getMainAttributes().getValue("Class-Path").split(" "))
							.stream().map(s -> new File(urlConverter.apply(s).getFile())).collect(Collectors.toList()));
				} catch (IOException ex) {
					throw new UncheckedIOException(ex);
				}
			} else if(urls.length > 1) {
				classpathEntries.addAll(Arrays.asList(urls).stream()
						.map(url -> new File(url.getFile())).collect(Collectors.toList()));
			} else {
				throw new IllegalStateException("No classpath entries found... that is pretty strange.");
			}
		} else {
			throw new IllegalStateException("System class loader must be of type URLClassLoader!");
		}

		final Function<File, File> fileCanonicalizer = file -> {
			try {
				return file.getCanonicalFile();
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
		};

		File currentTestSourceDirectory = fileCanonicalizer.apply(new File("./src/test/java"));

		return new ClassSourceResolver(currentTestSourceDirectory, classpathEntries.stream()
				.map(file -> fileCanonicalizer.apply(file))
				.filter(file -> file.toString().contains("target" + File.separator + "classes"))
				.map(file -> new File(file.toString().replace("target" + File.separator + "classes", "src" + File.separator + "main" + File.separator + "java")))
				.collect(Collectors.toList()));
	}

	List<File> getSourceDirectories() {
		return sourceDirs;
	}

	Optional<File> tryResolve(String className, boolean includeTestSources) {
		String relativeFileName = className.replaceAll(DOT_PATTERN, "/") + ".java";
		if (includeTestSources) {
			File testSourceFile = new File(currentTestSourceDir, relativeFileName);
			if (testSourceFile.exists()) {
				return Optional.of(testSourceFile);
			}
		}

		for (File sourceDir : sourceDirs) {
			File sourceFile = new File(sourceDir, relativeFileName);
			if (sourceFile.exists()) {
				return Optional.of(sourceFile);
			}
		}

		return Optional.empty();
	}
	
	File resolve(String className, boolean includeTestSources) {
		return tryResolve(className, includeTestSources).orElseThrow(()
				-> new IllegalStateException("The class " + className + " couldn't be resolved in " + sourceDirs + " ("
						+ (includeTestSources ? "with" : "without") + " test source)!"));
	}
}
