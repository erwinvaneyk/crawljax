package com.crawljax.core.state.duplicatedetection;

/**
 * Settings:
 * - Features
 * - Feature weights
 * - Threshold
 *
 */
public interface NearDuplicateDetection {
	
	public long generateHash(String doc);
	
	public boolean hasNearDuplicateHash(long hash); // Threshold defined in settings?
	
	public long findNearDuplicateHash(long hash); // Threshold defined internal or in settings

<<<<<<< HEAD
	public boolean isNearDuplicateHash(long hash1, long hash2);
=======
	public boolean isNearDuplicateHash(int hash1, int hash2);
		
	public int getDistance(int hash1, int hash2);
>>>>>>> 0de0b32... Get the distance between two hashes
}
