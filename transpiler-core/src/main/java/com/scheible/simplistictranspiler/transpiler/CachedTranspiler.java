package com.scheible.simplistictranspiler.transpiler;

import java.io.File;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 *
 * @author sj
 */
public class CachedTranspiler {

	private final Map<File, Map.Entry<Long, TranspilationResult>> cache = new ConcurrentHashMap<>();

	public TranspilationResult transpile(String className, TranspilationScope scope,
			Consumer<String> debugLogger, Consumer<String> traceLogger) {
		File file = Transpiler.resolve(className, scope);
		long lastModified = file.lastModified();

		if (cache.containsKey(file)) {
			Map.Entry<Long, TranspilationResult> cacheEntry = cache.get(file);
			if (lastModified == cacheEntry.getKey()) {
				traceLogger.accept("Got " + className + " from cache.");
				return cacheEntry.getValue();
			}
		}

		long now = System.nanoTime();
		TranspilationResult transpilationResult = Transpiler.transpile(className, scope);
		long afterTranspile = System.nanoTime();
		cache.put(file, new AbstractMap.SimpleImmutableEntry<>(lastModified, transpilationResult));
		debugLogger.accept("Transpiled " + className + " in " + TimeUnit.NANOSECONDS.toMillis(afterTranspile - now) + " ms");
		return transpilationResult;
	}
}
