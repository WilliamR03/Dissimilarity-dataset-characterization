package featureCharacterization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class FeatureCharacterizers {

	private static ArrayList<FeatureCharacterizer> characterizerList = null;
	private static ArrayList<String> mfList = null;

	public static ArrayList<FeatureCharacterizer> getCharacterizers() {
		if (characterizerList == null) {
			// instantier les mfs
			characterizerList = new ArrayList<FeatureCharacterizer>();

			// classic
			characterizerList.add(new General());
			characterizerList.add(new Nominal());
			characterizerList.add(new Numeric());

			// landmarkers
			characterizerList.add(new LandMarker("NaiveBayes", "weka.classifiers.bayes.NaiveBayes", 2, null));
			characterizerList.add(new LandMarker("DecisionStump", "weka.classifiers.trees.DecisionStump", 2, null));

			TreeMap<String, String[]> REPOptions = new TreeMap<String, String[]>();
			TreeMap<String, String[]> J48Options = new TreeMap<String, String[]>();
			TreeMap<String, String[]> RandomTreeOptions = new TreeMap<String, String[]>();
			String zeros = "0";
			for (int i = 1; i <= 3; ++i) {
				String[] repOption = { "-L", "" + i };
				REPOptions.put("Depth" + i, repOption);

				zeros += "0";
				String[] j48Option = { "-C", "." + zeros + "1" };
				J48Options.put("." + zeros + "1.", j48Option);

				String[] randomtreeOption = { "-depth", "" + i };
				RandomTreeOptions.put("Depth" + i, randomtreeOption);
			}
			characterizerList.add(new LandMarker("REPTree", "weka.classifiers.trees.REPTree", 2, REPOptions));
			characterizerList.add(new LandMarker("J48", "weka.classifiers.trees.J48", 2, J48Options));
			characterizerList.add(new LandMarker("RandomTree", "weka.classifiers.trees.RandomTree", 2, RandomTreeOptions));
			
			mfList = new ArrayList<String>();
			for (FeatureCharacterizer fc : characterizerList) {
				mfList.addAll(Arrays.asList(fc.getIDs()));
			}
		}
		return characterizerList;
	}

	public static int nbMetaFeatures() {
		if (characterizerList == null) {
			getCharacterizers();
		}
		return mfList.size();
	}
	
	public static ArrayList<String> getMfList() {
		if (characterizerList == null) {
			getCharacterizers();
		}
		return mfList;
	}
}
