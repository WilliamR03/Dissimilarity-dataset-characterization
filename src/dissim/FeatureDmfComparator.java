package dissim;

import java.util.Arrays;

public class FeatureDmfComparator {

	protected static String[] zeroSet = new String[] { "NbDistinctValues", "InfoGain", "Entropy", "NoiseToSignalRatio", "StdDev", "Skewness" };
	protected static String[] oneSet = new String[] { "PercentageOfMissingValues", "MinRelSize", "MaxRelSize" };

	public static double compareNull(String dmf, double v) throws Exception {
		if (!Double.isFinite(v)) {
			return 0;
		} else if (dmf.equals("NumMissingValues")) {
			return Double.NaN;
		} else if (dmf.equals("EquivalentNumberOfAtts")) {
			return Double.POSITIVE_INFINITY;
		} else if (dmf.equals("Mean")) {
			return Double.NaN;
		} else if (dmf.equals("Kurtosis")) {
			return Math.abs(v + 1.2);
		} else if (Arrays.asList(zeroSet).contains(dmf) || dmf.endsWith("Kappa")) {
			return Math.abs(v);
		} else if (Arrays.asList(oneSet).contains(dmf) || dmf.endsWith("ErrRate")) {
			return Math.abs(v - 1);
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
