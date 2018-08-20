package com.scheible.simplistictranspiler.samplewebapp.server;

import com.scheible.simplistictranspiler.transpiler.CachedTranspiler;
import com.scheible.simplistictranspiler.transpiler.TranspilationResult;
import com.scheible.simplistictranspiler.transpiler.TranspilationScope;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;

/**
 *
 * @author sj
 */
@Controller
public class TranspilationController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final TranspilationScope scope;
	private final CachedTranspiler cachedTranspiler = new CachedTranspiler();

	TranspilationController() {
		this.scope = TranspilationScope.MAIN;
	}

	public TranspilationController(TranspilationScope scope) {
		this.scope = scope;
	}

	@GetMapping(path = "/transpiler/**", produces = "application/javascript")
	@ResponseBody
	String transpile(@RequestParam Optional<Boolean> debug, HttpServletRequest request) {
		final String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
		final String whildcardPart = new AntPathMatcher().extractPathWithinPattern(pattern, request.getRequestURI());

		final String className = (whildcardPart.substring(0, whildcardPart.length() - 3)).replaceAll(Pattern.quote("/"), ".");

		TranspilationResult result = cachedTranspiler.transpile(className, scope,
				(text) -> logger.debug(text), (text) -> logger.trace(text));

		return (debug.isPresent() && debug.get() ? "/*\n" + result.getReport() + "\n*/\n" : "")
				+ result.getJavaScript();
	}
}
