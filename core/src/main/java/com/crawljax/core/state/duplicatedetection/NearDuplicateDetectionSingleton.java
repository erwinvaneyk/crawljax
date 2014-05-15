package com.crawljax.core.state.duplicatedetection;

import java.util.ArrayList;
import java.util.List;

public class NearDuplicateDetectionSingleton {
	private static NearDuplicateDetection ndd;
	
	private static List<FeatureType> features = new ArrayList<FeatureType>();
	
	public static NearDuplicateDetection getInstance() {
		if (ndd == null) {
			features.add(new FeatureShingles(5, Type.WORDS));
			ndd = new NearDuplicateDetectionCrawlHash32(3, features);
		}
		return ndd;
	}
	
	public static void addFeature(FeatureType ft) {
		features.add(ft);
	}
}
