package com.crawljax.core.state.duplicatedetection;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.crawljax.core.state.duplicatedetection.DuplicateDetectionException;
import com.crawljax.core.state.duplicatedetection.FeatureShingles;
import com.crawljax.core.state.duplicatedetection.FeatureException;
import com.crawljax.core.state.duplicatedetection.FeatureType;
import com.crawljax.core.state.duplicatedetection.Fingerprint;
import com.crawljax.core.state.duplicatedetection.HashGenerator;
import com.crawljax.core.state.duplicatedetection.NearDuplicateDetection;
import com.crawljax.core.state.duplicatedetection.NearDuplicateDetectionCrawlhash;
import com.crawljax.core.state.duplicatedetection.XxHashGenerator;
import com.google.common.collect.ImmutableList;

public class NearDuplicateDetectionCrawlhashTest {

	@Test
	public void testGetThreshold() throws FeatureException {
		ArrayList<FeatureType> features = new ArrayList<FeatureType>();
		features.add(new FeatureShingles(9, FeatureShingles.ShingleType.WORDS));

		HashGenerator hasher = new XxHashGenerator();
		NearDuplicateDetectionCrawlhash ndd =
		        new NearDuplicateDetectionCrawlhash(3, ImmutableList.copyOf(features), hasher);
		assertEquals(3, ndd.getDefaultThreshold(), 0.001);
	}

	@Test
	public void testFeatureSizeOnBoundary() throws FeatureException {
		ArrayList<FeatureType> features = new ArrayList<FeatureType>();
		features.add(new FeatureShingles(7, FeatureShingles.ShingleType.WORDS));

		HashGenerator hasher = new XxHashGenerator();
		NearDuplicateDetectionCrawlhash ndd =
		        new NearDuplicateDetectionCrawlhash(3, ImmutableList.copyOf(features), hasher);
		String strippedDom = "This is some text for the test.";
		ndd.generateFingerprint(strippedDom);
	}

	@Test(expected = FeatureException.class)
	public void testFeatureSizeOffBoundary() throws FeatureException {
		ArrayList<FeatureType> features = new ArrayList<FeatureType>();
		features.add(new FeatureShingles(8, FeatureShingles.ShingleType.WORDS));

		HashGenerator hasher = new XxHashGenerator();
		NearDuplicateDetectionCrawlhash ndd =
		        new NearDuplicateDetectionCrawlhash(3, ImmutableList.copyOf(features), hasher);
		String strippedDom = "This is some text for the test.";
		ndd.generateFingerprint(strippedDom);
	}

	@Test
	public void testSameDomToSameHash() throws FeatureException {
		ArrayList<FeatureType> features = new ArrayList<FeatureType>();
		features.add(new FeatureShingles(2, FeatureShingles.ShingleType.CHARS));

		HashGenerator hasher = new XxHashGenerator();
		NearDuplicateDetectionCrawlhash ndd =
		        new NearDuplicateDetectionCrawlhash(3, ImmutableList.copyOf(features), hasher);
		String strippedDom1 = "Test";
		String strippedDom2 = "Test";

		Fingerprint fingerprint = ndd.generateFingerprint(strippedDom1);
		Fingerprint fingerprint2 = ndd.generateFingerprint(strippedDom2);
		assertTrue(fingerprint.isNearDuplicate(fingerprint2, 0));
	}

	@Test
	public void testDifferendDomToDifferendHash() throws FeatureException {
		ArrayList<FeatureType> features = new ArrayList<FeatureType>();
		features.add(new FeatureShingles(2, FeatureShingles.ShingleType.WORDS));

		HashGenerator hasher = new XxHashGenerator();
		NearDuplicateDetectionCrawlhash ndd =
		        new NearDuplicateDetectionCrawlhash(3, ImmutableList.copyOf(features), hasher);
		String strippedDom1 = "This is some text for the test.";
		String strippedDom2 = "Other text will be shown";

		Fingerprint fingerprint = ndd.generateFingerprint(strippedDom1);
		Fingerprint fingerprint2 = ndd.generateFingerprint(strippedDom2);
		assertThat(fingerprint.getDistance(fingerprint2), not(is(0.0)));
	}

	@Test(expected = DuplicateDetectionException.class)
	public void testMissingFeatures() {
		HashGenerator hasher = new XxHashGenerator();
		ArrayList<FeatureType> features = new ArrayList<FeatureType>();
		new NearDuplicateDetectionCrawlhash(3, ImmutableList.copyOf(features), hasher);
	}

	@Test(expected = DuplicateDetectionException.class)
	public void testFeaturesIsNull() {
		HashGenerator hasher = new XxHashGenerator();
		new NearDuplicateDetectionCrawlhash(3, null, hasher);
	}

	@Test
	public void testThresholdOnBoundary() {
		HashGenerator hasher = new XxHashGenerator();
		ArrayList<FeatureType> features = new ArrayList<FeatureType>();
		features.add(new FeatureShingles(2, FeatureShingles.ShingleType.WORDS));
		new NearDuplicateDetectionCrawlhash(32, ImmutableList.copyOf(features), hasher);
	}

	@Test
	public void testThresholdOffBoundary() {
		HashGenerator hasher = new XxHashGenerator();
		ArrayList<FeatureType> features = new ArrayList<FeatureType>();
		features.add(new FeatureShingles(2, FeatureShingles.ShingleType.WORDS));
		new NearDuplicateDetectionCrawlhash(33, ImmutableList.copyOf(features), hasher);
	}

	@Test
	public void testToLowThreshold() {
		HashGenerator hasher = new XxHashGenerator();
		ArrayList<FeatureType> features = new ArrayList<FeatureType>();
		features.add(new FeatureShingles(2, FeatureShingles.ShingleType.WORDS));
		NearDuplicateDetection crawlhash =
		        new NearDuplicateDetectionCrawlhash(1, ImmutableList.copyOf(features), hasher);
		crawlhash.setDefaultThreshold(-1);
	}

	@Test
	public void testSetThresholdCorrect() {
		HashGenerator hasher = new XxHashGenerator();
		ArrayList<FeatureType> features = new ArrayList<FeatureType>();
		features.add(new FeatureShingles(2, FeatureShingles.ShingleType.WORDS));
		NearDuplicateDetection crawlHash =
		        new NearDuplicateDetectionCrawlhash(1, ImmutableList.copyOf(features), hasher);
		crawlHash.setDefaultThreshold(8);
		assertEquals(8, crawlHash.getDefaultThreshold(), 0.1);
	}

	@Test
	public void testSetFeaturesCorrect() {
		HashGenerator hasher = new XxHashGenerator();
		ArrayList<FeatureType> features = new ArrayList<FeatureType>();
		features.add(new FeatureShingles(2, FeatureShingles.ShingleType.WORDS));
		NearDuplicateDetection crawlHash =
		        new NearDuplicateDetectionCrawlhash(1, ImmutableList.copyOf(features), hasher);

		List<FeatureType> newFeatures = new ArrayList<FeatureType>();
		newFeatures.add(new FeatureShingles(1, FeatureShingles.ShingleType.CHARS));
		crawlHash.setFeatures(ImmutableList.copyOf(newFeatures));

		List<String> listOfFeatures = crawlHash.getFeatures().asList().get(0).getFeatures("Test");
		assertEquals("T", listOfFeatures.get(0));
		assertEquals("e", listOfFeatures.get(1));
		assertEquals("s", listOfFeatures.get(2));
		assertEquals("t", listOfFeatures.get(3));
	}
	
	@Test
	public void testCrawlhashConstructorNoHashFactory() {
		List<FeatureType> newFeatures = new ArrayList<FeatureType>();
		newFeatures.add(new FeatureShingles(1, FeatureShingles.ShingleType.CHARS));
		NearDuplicateDetectionCrawlhash ndd = new NearDuplicateDetectionCrawlhash(1, ImmutableList.copyOf(newFeatures));
		String oldString = ndd.toString();
		ndd.setHashGenerator(new XxHashGenerator());
		assertNotEquals(ndd.toString(), oldString);
	}
}
