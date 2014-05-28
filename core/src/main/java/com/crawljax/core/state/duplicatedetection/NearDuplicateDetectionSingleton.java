package com.crawljax.core.state.duplicatedetection;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton-class responsible for maintaining the configurations of a single
 * NearDuplicateDetection-algorithm.
 */
public class NearDuplicateDetectionSingleton {
	private static NearDuplicateDetection ndd;
	private static double threshold;
	private static List<FeatureType> features = new ArrayList<FeatureType>();
	
	static {
	//	features.add(new FeatureShingles(3, FeatureSizeType.WORDS));
	}
	
	/**
	 * Retrieves, if necessary creates, an instance
	 * @return near-duplicate detection instance
	 */
	public static NearDuplicateDetection getInstance() {
		if (ndd == null) {
			ndd = new NearDuplicateDetectionBroder32(threshold, features);
		}
		return ndd;
	}
	
	/**
	 * Sets the threshold and reconstructs the instance
	 * @param t the new threshold
	 */
	public static void setThreshold(double t) {
		threshold = t;
		resetInstance();
	}
	
	/**
	 * Returns the threshold
	 * @return current threshold
	 */
	public static double getThreshold() {
		return threshold;
	}
	
	/**
	 * Adds an additional feature and reconstructs the instance
	 * @param ft the feature to be added. 
	 */
	public static void addFeature(FeatureType ft) {
		features.add(ft);
		resetInstance();
	}
	
	/**
	 * Resets/rebuilds the instance stored by the singleton.
	 */
	public static void resetInstance() {
		ndd = null;
	}
}
