package com.crawljax.core.state.duplicatedetection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Near-duplicate detection based on the use of a Jaccard coefficient. Given a set of features,
 * these features are first hashed. Afterwards the collections of hashes of SiteA and SiteB compared
 * using the Jaccard coefficients (intersection(SiteA,SiteB)/union(SiteA,SiteB)).
 */
@Singleton
public class NearDuplicateDetectionBroder32 implements NearDuplicateDetection {

	private List<FeatureType> features;
	private double threshold;
	private HashGenerator hashGenerator;
	private final static float THRESHOLD_UPPERLIMIT = 1;
	private final static float THRESHOLD_LOWERLIMIT = 0;

	@Inject
	public NearDuplicateDetectionBroder32(double threshold, List<FeatureType> fs, HashGenerator hg) {
		checkPreconditionsFeatures(fs);
		checkPreconditionsThreshold(threshold);
		this.hashGenerator = hg;
		this.features = fs;
		this.threshold = threshold;
	}
	
	/**
	 * Generate the hashes from the features of the string.
	 * 
	 * @param doc
	 *            The string that will be divided into features
	 * @return an array of the hashes, generated from the features, of the given string
	 */
	@Override
	public int[] generateHash(String doc) {
		// Check preconditions
		checkPreconditionsFeatures(features);
		checkPreconditionsThreshold(threshold);

		List<String> shingles = this.generateFeatures(doc);
		int length = shingles.size();

		int[] hashes = new int[length];
		for (int i = 0; i < length; i++) {
			hashes[i] = hashGenerator.generateHash(shingles.get(i));
		}
		return hashes;
	}

	/**
	 * Return true if the JaccardCoefficient is higher than the threshold.
	 */
	@Override
	public boolean isNearDuplicateHash(int[] state1, int[] state2) {
		return (this.getDistance(state1, state2) <= this.threshold);
	}

	/**
	 * Get the distance between two sets.
	 * 
	 * @return Zero if both sets contains exactly the same hashes and one if the two sets contains
	 *         all different hashes and values in between for the corresponding difference. The
	 *         closer the value is to zero, the more hashes in the sets are the same.
	 */
	@Override
	public double getDistance(int[] state1, int[] state2) {
		double jaccardCoefficient = this.getJaccardCoefficient(state1, state2);
		return 1 - jaccardCoefficient;
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

	/**
	 * Generate the features from the content of the state.
	 * 
	 * @param doc
	 *            The content of the state
	 * @return A list of strings that represent the features
	 * @throws FeatureException
	 *             if the feature size is to big or if the chosen feature type does not exist
	 */
	private List<String> generateFeatures(String doc) {
		List<String> li = new ArrayList<>();

		for (FeatureType feature : features) {
			li.addAll(feature.getFeatures(doc));
		}
		return li;
	}

	/**
	 * Checks the precondition for the feature-list, which should not be empty or null.
	 * 
	 * @param features
	 *            feature-list to be checked
	 */
	private void checkPreconditionsFeatures(List<FeatureType> features) {
		if (features == null || features.isEmpty()) {
			throw new DuplicateDetectionException(
			        "Invalid feature-list provided, feature-list cannot be null or empty. (Provided: "
			                + features + ")");
		}
	}

	/**
	 * ' Checks the precondition for the threshold, which should be within the predefined upper and
	 * lower bounds.
	 * 
	 * @param threshold
	 */
	private void checkPreconditionsThreshold(double threshold) {
		if (threshold > THRESHOLD_UPPERLIMIT || threshold < THRESHOLD_LOWERLIMIT) {
			throw new DuplicateDetectionException("Invalid threshold value " + threshold
			        + ", threshold as to be between " + THRESHOLD_LOWERLIMIT + " and "
			        + THRESHOLD_UPPERLIMIT + ".");
		}
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		checkPreconditionsThreshold(threshold);
		this.threshold = threshold;
	}

	public List<FeatureType> getFeatures() {
		return features;
	}

	public void setFeatures(List<FeatureType> features) {
		checkPreconditionsFeatures(features);
		this.features = features;
	}
}
