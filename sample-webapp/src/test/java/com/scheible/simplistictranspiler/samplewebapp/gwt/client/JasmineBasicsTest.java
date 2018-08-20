package com.scheible.simplistictranspiler.samplewebapp.gwt.client;

import com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework.AbstractJasmineTest;
import static com.scheible.simplistictranspiler.samplewebapp.gwt.client.jasmine.Jasmine.expect;
import static com.scheible.simplistictranspiler.samplewebapp.gwt.client.jasmine.Jasmine.spyOn;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class JasmineBasicsTest extends AbstractJasmineTest {

	@Test
	public void testNumberJasmineMatcher() {
		it(() -> {
			expect(42).toBe(42);
		});
	}

	@Test
	public void testBooleanJasmineMatcher() {
		it(() -> {
			expect(true).toBe(true);
		});
	}

	@Test
	public void testStringJasmineMatcher() {
		it(() -> {
			expect("foo").toBe("foo");
		});
	}
	
	@Test
	public void testJasmineSpyOn() {
		it(() -> {
			SpyObject spyObject = new SpyObject(0);
			expect(spyObject.getValue()).toBe(0);
			
			spyOn(spyObject, "getValue").and().returnValue(42);
			expect(spyObject.getValue()).toBe(42);
		});
	}	
}
