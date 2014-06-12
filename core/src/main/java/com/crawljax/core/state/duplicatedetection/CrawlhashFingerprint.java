package com.crawljax.core.state.duplicatedetection;

import com.google.common.annotations.VisibleForTesting;

public class CrawlhashFingerprint implements Fingerprint {

	private int hash;
	private double defaultThreshold;

	public CrawlhashFingerprint(int hash, double defaultThreshold) {
		this.hash = hash;
		this.defaultThreshold = defaultThreshold;
	}
	
	@Override
	public boolean isNearDuplicateHash(Fingerprint other) {
		return ((double) hammingDistance(this.hash, other.getHashesAsIntArray()[0])) <= defaultThreshold;
	}
	

	@Override
	public boolean isNearDuplicateHash(Fingerprint other, double threshold) {
		return ((double) hammingDistance(this.hash, other.getHashesAsIntArray()[0])) <= threshold;
	}
	
	public double getDistance(Fingerprint other) {
		fingerprintTypeCheck(other);
		return hammingDistance(this.hash, other.getHashesAsIntArray()[0]);
	}
	
	private void fingerprintTypeCheck(Fingerprint other) {
		if(this.getClass().isInstance(other))
			throw new RuntimeException("Cannot compare fingerprints of different types. (this: " + this.getClass() + " vs. that: " + other.getClass() + ")");
	}
	
	@VisibleForTesting
	public int hammingDistance(int hash1, int hash2) {
		int i = hash1 ^ hash2;
		i = i - ((i >>> 1) & 0x55555555);
		i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
		i = (i + (i >>> 4)) & 0x0f0f0f0f;
		i = i + (i >>> 8);
		i = i + (i >>> 16);
		return i & 0x3f;
	}

	@Override
	public int[] getHashesAsIntArray() {
		return new int[]{hash};
	}

}
