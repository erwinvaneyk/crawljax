package com.crawljax.core.state;

public class NearDuplicateDetectionFactory {
	private static NearDuplicateDetection ndd;
	
	public static NearDuplicateDetection getInstance() {
		if (ndd == null) {
			ndd = new NearDuplicateDetection();
		}
		return ndd;
	}
}
