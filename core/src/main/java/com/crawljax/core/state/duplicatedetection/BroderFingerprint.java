package com.crawljax.core.state.duplicatedetection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Fingerprint data-object for Broder-specific hashes generated by NearDuplicateDetectionBroder.
 * 
 * @see NearDuplicateDetectionBroder
 */
public class BroderFingerprint implements Fingerprint {

	private final double defaultThreshold;
	private final int[] hashes;

	private static final double THRESHOLD_UPPERLIMIT = 1;
	private static final double THRESHOLD_LOWERLIMIT = 0;

	/**
	 * Constructor for this used by the NearDuplicateDetectionBroder
	 * 
	 * @param hashes
	 *            the generated hashes on which this fingerprint is based.
	 * @param defaultThreshold
	 *            the default threshold, which is used when no threshold is provided.
	 */
	BroderFingerprint(int[] hashes, double defaultThreshold) {
		checkIfValidThreshold(defaultThreshold);
		this.defaultThreshold = defaultThreshold;
		this.hashes = hashes;
	}

	/**
	 * Constructor without setting the defaultThreshold, which will be set to 1.
	 * 
	 * @param hashes
	 *            the generated hashes on which this fingerprint is based.
	 */
	BroderFingerprint(int[] hashes) {
		this.hashes = hashes;
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

	/**
	 * Get the distance between two sets.
	 * 
	 * @param other
	 *            The other BroderFingerprint, to which the distance should be calculated.
	 * @return Zero if both sets contains exactly the same hashes and one if the two sets contains
	 *         all different hashes and values in between for the corresponding difference. The
	 *         closer the value is to zero, the more hashes in the sets are the same.
	 */
	@Override
	public double getDistance(Fingerprint other) {
		BroderFingerprint that = fingerprintTypeCheck(other);
		return 1 - this.getJaccardCoefficient(hashes, that.hashes);
	}

	/**
	 * Checks whether the other Fingerprint is of the same type as this, otherwise throw an
	 * exception.
	 * 
	 * @param other
	 *            the Fingerprint of which the type should be the same as this.
	 * @return Broderfingerprint if other is a Broderfingerprint, else a Runtime-exception is
	 *         thrown.
	 */
	private BroderFingerprint fingerprintTypeCheck(Fingerprint other) {
		if (!this.getClass().isInstance(other))
			throw new DuplicateDetectionException(
			        "Cannot compare fingerprints of different types. (this: " + this.getClass()
			                + " vs. that: " + other.getClass() + ")");
		return (BroderFingerprint) other;
	}

	/**
	 * Calculate the <a href="http://en.wikipedia.org/wiki/Jaccard_index">Jaccard Coefficient</a> of
	 * two sets of integers.
	 * 
	 * @param state1
	 *            first set of integers
	 * @param state2
	 *            second set of integers
	 * @return the Jaccard Coefficient of the two arguments.
	 */
	private double getJaccardCoefficient(int[] state1, int[] state2) {
		// Remove any duplicates hashes from the ints by using sets
		Set<Integer> setOfFirstArg = new HashSet<Integer>(state1.length);
		Set<Integer> setOfSecondArg = new HashSet<Integer>(state2.length);
		for (int state : state1) {
			setOfFirstArg.add(state);
		}
		for (int state : state2) {
			setOfSecondArg.add(state);
		}
		// Do the Jaccard index calculation: intersect(A,B)/union(A,B)
		double unionCount = Sets.union(setOfFirstArg, setOfSecondArg).size();
		double intersectionCount = Sets.intersection(setOfFirstArg, setOfSecondArg).size();
		return intersectionCount / unionCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!getClass().isInstance(obj))
			return false;
		BroderFingerprint other = (BroderFingerprint) obj;
		if (!Arrays.equals(hashes, other.hashes))
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
		return "BroderFingerprint [defaultThreshold=" + defaultThreshold + ", hashes="
		        + Arrays.toString(hashes) + "]";
	}
}
