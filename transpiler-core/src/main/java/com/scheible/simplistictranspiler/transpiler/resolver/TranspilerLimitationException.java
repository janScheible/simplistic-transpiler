package com.scheible.simplistictranspiler.transpiler.resolver;

/**
 *
 * @author sj
 */
public class TranspilerLimitationException extends RuntimeException {

	public TranspilerLimitationException(String message) {
		super(message);
	}

	public TranspilerLimitationException(String message, Throwable cause) {
		super(message, cause);
	}
}
