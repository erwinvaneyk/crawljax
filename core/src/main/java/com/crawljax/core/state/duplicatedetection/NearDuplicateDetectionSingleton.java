package com.crawljax.core.state.duplicatedetection;

import java.util.ArrayList;
import java.util.List;


public class NearDuplicateDetectionSingleton {
	private static NearDuplicateDetection ndd;
	private static int threshold;
	private static List<FeatureType> features = new ArrayList<FeatureType>();
	
	public NearDuplicateDetection getInstance() {
		if (ndd == null) {
			features.add(new FeatureShingles(3, Type.WORDS));
			ndd = new NearDuplicateDetectionCrawlHash32(threshold, features);
		}
		return ndd;
	}
	
	public void setThreshold(int t) {
		threshold = t;
	}
	public int getThreshold() {
		return threshold;
	}
	public void addFeature(FeatureType ft) {
		features.add(ft);
	}
}
