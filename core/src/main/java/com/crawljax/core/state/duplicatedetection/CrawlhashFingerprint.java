package com.crawljax.core.state.duplicatedetection;

import com.google.common.annotations.VisibleForTesting;

/**
 * Fingerprint data-object for Crawlhash-specific hashes generated by
 * NearDuplicateDetectionCrawlhash32.
 * 
 * @see NearDuplicateDetectionCrawlhash32
 */
public class CrawlhashFingerprint implements Fingerprint {

	private final int hash;
	private final double defaultThreshold;

	private static final double THRESHOLD_UPPERLIMIT = 32;
	private static final double THRESHOLD_LOWERLIMIT = 0;

	/**
	 * Constructor for this used by the NearDuplicateDetectionCrawlhash32
	 * 
	 * @param hash
	 *            the generated hash on which this fingerprint is based.
	 * @param defaultThreshold
	 *            the default threshold, which is used when no threshold is provided.
	 */
	CrawlhashFingerprint(int hash, double defaultThreshold) {
		checkIfValidThreshold(defaultThreshold);
		this.hash = hash;
		this.defaultThreshold = defaultThreshold;
	}

	/**
	 * Constructor without setting the defaultThreshold, which will be set to 1.
	 * 
	 * @param hash
	 *            the generated hash on which this fingerprint is based.
	 */
	CrawlhashFingerprint(int hash) {
		this.hash = hash;
		this.defaultThreshold = 1;
	}

	@Override
	public boolean isNearDuplicate(Fingerprint other) {
		return getDistance(other) <= defaultThreshold;
	}

	@Override
	public boolean isNearDuplicate(Fingerprint other, double threshold) {
		checkIfValidThreshold(threshold);
		return getDistance(other) <= threshold;
	}

	@Override
	public double getDistance(Fingerprint other) {
		CrawlhashFingerprint that = fingerprintTypeCheck(other);
		return hammingDistance(hash, that.hash);
	}

	/**
	 * Checks whether the other Fingerprint is of the same type as this, otherwise throw an
	 * exception.
	 * 
	 * @param other
	 *            the Fingerprint of which the type should be the same as this.
	 * @return Broderfingerprint if other is a Broderfingerprint, else a Runtime-exception is thrown.
	 */
	private CrawlhashFingerprint fingerprintTypeCheck(Fingerprint other) {
		if (!this.getClass().isInstance(other))
			throw new DuplicateDetectionException(
			        "Cannot compare fingerprints of different types. (this: " + this.getClass()
			                + " vs. that: " + other.getClass() + ")");
		return (CrawlhashFingerprint) other;
	}

	/**
	 * The <a href="http://en.wikipedia.org/wiki/Hamming_distance">Hamming-distance</a> calculates the distance
	 * between two hashes.
	 * 
	 * @param hash1
	 *            int-represented bit-hash
	 * @param hash2
	 *            int-represented bit-hash
	 * @return The distance is specified as the minimum number of bit-character changes needed to
	 *         transform hash1 to hash2.
	 */
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!getClass().isInstance(obj))
			return false;
		CrawlhashFingerprint other = (CrawlhashFingerprint) obj;
		if (hash != other.hash)
			return false;
		return true;
	}

	/**
	 * Checks if threshold is a double within the upper and lower bounds of the fingerprint-type. If
	 * not a runtime-exception is thrown.
	 * 
	 * @param threshold
	 *            The threshold-value that should be checked.
	 */
	private void checkIfValidThreshold(double threshold) {
		if (threshold > THRESHOLD_UPPERLIMIT || threshold < THRESHOLD_LOWERLIMIT) {
			throw new DuplicateDetectionException("Invalid threshold value " + threshold
			        + ", threshold as to be between " + THRESHOLD_LOWERLIMIT + " and "
			        + THRESHOLD_UPPERLIMIT + ".");
		}
	}

	@Override
	public double getThresholdUpperlimit() {
		return THRESHOLD_UPPERLIMIT;
	}

	@Override
	public double getThresholdLowerlimit() {
		return THRESHOLD_LOWERLIMIT;
	}

	@Override
	public double getDefaultThreshold() {
		return defaultThreshold;
	}

	@Override
	public String toString() {
		return "CrawlhashFingerprint [hash=" + hash + ", defaultThreshold=" + defaultThreshold
		        + "]";
	}
}
