package com.crawljax.core.state;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class NearDuplicateDetectionTest {
	
	@Test
	public void testDuplicateOnSameState() {
		StateVertex v = new StateVertexImpl(1, "http://demo.crawljax.com", "State1", "<html><body><h1>Test</h1></body></html>", "<html><body><h1></h1></body></html>");
		boolean duplicate = v.isNearDuplicate(v);
		assertEquals(true, duplicate);
	}
	
	@Test
	public void testDuplicateOnNewState() {
		StateVertex v = new StateVertexImpl(1, "http://demo.crawljax.com", "State1", "<html><body><h1>Test</h1></body></html>", "<html><body><h1></h1></body></html>");
		StateVertex w = new StateVertexImpl(1, "http://demo.crawljax.com", "State1", "<html><body><h1>Test</h1></body></html>", "<html><body><h1></h1></body></html>");
		boolean duplicate = v.isNearDuplicate(w);
		assertEquals(true, duplicate);
	}
	
	@Test
	public void testNotDuplicate() {
		StateVertex v = new StateVertexImpl(1, "http://demo.crawljax.com", "State1", "<html><body><h1>Test</h1></body></html>", "<html><body><h1></h1></body></html>");
		StateVertex w = new StateVertexImpl(1, "http://test.com", "State5", "<p>hello world</p>", "<p></p>");
		boolean duplicate = v.isNearDuplicate(w);
		assertEquals(false, duplicate);
	}
	
	@Test
	public void testSameContentDifferentSite() {
		StateVertex v = new StateVertexImpl(1, "http://demo.crawljax.com", "State1", "<html><body><h1>Test</h1></body></html>", "<html><body><h1></h1></body></html>");
		StateVertex w = new StateVertexImpl(1, "http://test.com", "Index", "<html><body><h1>Test</h1></body></html>", "<html><body><h1></h1></body></html>");
		boolean duplicate = v.isNearDuplicate(w);
		assertEquals(true, duplicate);
	}
}
