package featureCharacterization;

import java.util.Map;

import weka.core.Attribute;
import weka.core.Instances;

public abstract class FeatureCharacterizer {

	public abstract Map<String, Double> characterize(Instances instances, Attribute target) throws Exception;

	public abstract String[] getIDs();

	public int getNumMetaFeatures() {
		return getIDs().length;
	}

}
