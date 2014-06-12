package com.crawljax.core.state.duplicatedetection;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

public class BroderFingerprint implements Fingerprint {
	

	private double defaultThreshold;
	private int[] hashes;
	
	public BroderFingerprint(int[] hashes, double defaultThreshold) {
		this.defaultThreshold = defaultThreshold;
		this.hashes = hashes;
	}


	/**
	 * Return true if the JaccardCoefficient is higher than the threshold.
	 */
	@Override
	public boolean isNearDuplicateHash(Fingerprint other) {
		return (this.getDistance(other) <= this.defaultThreshold);
	}
	
	/**
	 * Return true if the JaccardCoefficient is higher than the threshold.
	 */
	@Override
	public boolean isNearDuplicateHash(Fingerprint other, double threshold) {
		return (this.getDistance(other) <= threshold);
	}


	/**
	 * Get the distance between two sets.
	 * 
	 * @return Zero if both sets contains exactly the same hashes and one if the two sets contains
	 *         all different hashes and values in between for the corresponding difference. The
	 *         closer the value is to zero, the more hashes in the sets are the same.
	 */
	@Override
	public double getDistance(Fingerprint other) {
		fingerprintTypeCheck(other);
		return 1 - this.getJaccardCoefficient(this.getHashesAsIntArray(), other.getHashesAsIntArray());
	}
	
	@Override
    public int[] getHashesAsIntArray() {
	    return this.hashes;
    }
	
	private void fingerprintTypeCheck(Fingerprint other) {
		if(this.getClass().isInstance(other))
			throw new RuntimeException("Cannot compare fingerprints of different types. (this: " + this.getClass() + " vs. that: " + other.getClass() + ")");
	}

	private double getJaccardCoefficient(int[] state1, int[] state2) {
		Set<Integer> setOfFirstArg = new HashSet<Integer>(state1.length);
		Set<Integer> setOfSecondArg = new HashSet<Integer>(state2.length);
		for (int state : state1) {
			setOfFirstArg.add(state);
		}
		for (int state : state2) {
			setOfSecondArg.add(state);
		}
		double unionCount = Sets.union(setOfFirstArg, setOfSecondArg).size();
		double intersectionCount = Sets.intersection(setOfFirstArg, setOfSecondArg).size();

		return (intersectionCount / unionCount);
	}

}
