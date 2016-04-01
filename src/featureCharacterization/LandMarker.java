package featureCharacterization;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import weka.classifiers.AbstractClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class LandMarker extends FeatureCharacterizer {

	private static final String[] measures = { "AUC", "ErrRate", "Kappa" };
	private final int numFolds;
	private final String classifierName;
	private final String className;

	// maps id name to option list
	private final TreeMap<String, String[]> options;

	public LandMarker(String classifierName, String className, int numFolds, TreeMap<String, String[]> options) {
		this.numFolds = numFolds;
		this.classifierName = classifierName;
		this.className = className;
		if (options != null) {
			this.options = options;
		} else {
			this.options = new TreeMap<String, String[]>();
			this.options.put("", new String[0]);
		}
	}

	@Override
	public String[] getIDs() {
		String[] keys = new String[options.size() * measures.length];
		int currentIndex = 0;
		for (String option : options.keySet()) {
			for (String measure : measures) {
				keys[currentIndex++] = classifierName + option + measure;
			}
		}
		return keys;
	}

	@Override
	public Map<String, Double> characterize(Instances dataset, Attribute target) throws Exception {
		Map<String, Double> qualities = new HashMap<String, Double>();

		// réduire le dataset au feature concerné + classe
		Remove Remover = new Remove();
		int[] rem = { target.index(), dataset.classIndex() };
		Remover.setAttributeIndicesArray(rem);
		Remover.setInvertSelection(true);
		Remover.setInputFormat(dataset);
		Instances reducedDataset = Filter.useFilter(dataset, Remover);

		// et evaluer le landmarker dessus
		for (String optionId : options.keySet()) {
			try {
				weka.classifiers.Evaluation eval = new weka.classifiers.Evaluation(reducedDataset);
				AbstractClassifier cls = (AbstractClassifier) Class.forName(className).newInstance();
				cls.setOptions(options.get(optionId));

				eval.crossValidateModel(cls, reducedDataset, numFolds, new java.util.Random(1));

				qualities.put(classifierName + optionId + "AUC", eval.weightedAreaUnderROC());
				qualities.put(classifierName + optionId + "ErrRate", eval.errorRate());
				qualities.put(classifierName + optionId + "Kappa", eval.kappa());
			} catch (Exception e) {
				throw new LandMarkerException(e.getMessage());
			}
		}
		return qualities;
	}

}
