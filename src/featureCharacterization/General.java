package featureCharacterization;

import java.util.HashMap;
import java.util.Map;

import weka.core.Attribute;
import weka.core.Instances;

public class General extends FeatureCharacterizer {

	protected final String[] ids = new String[] { "NumMissingValues", "PercentageOfMissingValues" };

	@Override
	public String[] getIDs() {
		return ids;
	}

	@Override
	public Map<String, Double> characterize(Instances dataset, Attribute target) {
		int count = 0;

		for (int i = 0; i < dataset.numInstances(); i++) {
			if (dataset.instance(i).isMissing(target.index())) {
				count++;
			}
		}

		Map<String, Double> qualities = new HashMap<String, Double>();
		qualities.put(ids[0], (double) count);
		qualities.put(ids[1], 1.0 * count / (dataset.numInstances()));
		return qualities;
	}
}
