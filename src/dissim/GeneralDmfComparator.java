package dissim;

import java.util.Arrays;

public class GeneralDmfComparator {

	protected static String[] nanSet = new String[] { "NumberOfInstancesWithMissingValues", "NumberOfMissingValues", "IncompleteInstanceCount",
			"MeanMeansOfNumericAtts", "NumMissingValues", "EquivalentNumberOfAtts" };
	protected static String[] zeroSet = new String[] { "NumberOfFeatures", "NumberOfInstances", "NumberOfNumericFeatures", "NumberOfNumericFeatures",
			"DefaultAccuracy", "MajorityClassSize", "MinorityClassSize", "NumberOfClasses", "Dimensionality", "InstanceCount", "NumAttributes", "NumBinaryAtts",
			"NumNominalAtts", "NumNumericAtts", "PercentageOfBinaryAtts", "PercentageOfNominalAtts", "PercentageOfNumericAtts", "HoeffdingAdwin.changes",
			"HoeffdingAdwin.warnings", "HoeffdingDDM.changes", "HoeffdingDDM.warnings", "NaiveBayesAdwin.changes", "NaiveBayesAdwin.warnings", "NaiveBayesDdm.changes",
			"NaiveBayesDdm.warnings", "ClassCount", "ClassEntropy", "", "" };
	protected static String[] oneSet = new String[] { "PercentageOfMissingValues", "NegativePercentage", "PositivePercentage"};
	protected static String[] idSet = new String[] { "MaxNominalAttDistinctValues", "MeanNominalAttDistinctValues", "MeanSkewnessOfNumericAtts", "MeanStdDevOfNumericAtts",
			"MinNominalAttDistinctValues", "StdvNominalAttDistinctValues", "MeanAttributeEntropy", "MeanMutualInformation", "NoiseToSignalRatio"};

	public static double compareNull(String dmf, double v) throws Exception {
		if (!Double.isFinite(v) || Arrays.asList(zeroSet).contains(dmf)) {
			return 0;
		} else if (dmf.equals("MeanKurtosisOfNumericAtts")) {
			return Math.abs(v + 1.2);
		} else if (Arrays.asList(nanSet).contains(dmf)) {
			return Double.NaN;
		} else if (Arrays.asList(oneSet).contains(dmf) || dmf.endsWith("ErrRate")) {
			return Math.abs(v - 1);
		} else if (Arrays.asList(idSet).contains(dmf) || dmf.endsWith("Kappa")) {
			return Math.abs(v);
		} else if (dmf.endsWith("AUC")) {
			return Math.abs(v - 0.5);
		} else {
			throw new Exception("Unknown dmf : " + dmf);
		}
	}

	public static double compare(String dmf, double v1, double v2) throws Exception {
		if (!Double.isFinite(v1)) {
			return compareNull(dmf, v2);
		} else if (!Double.isFinite(v2)) {
			return compareNull(dmf, v1);
		} else {
			/* No special comparison yet */;
			return Math.abs(v1 - v2);
		}
	}

}
