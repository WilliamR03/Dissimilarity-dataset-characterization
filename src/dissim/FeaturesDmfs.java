package dissim;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import featureCharacterization.FeatureCharacterizer;
import featureCharacterization.FeatureCharacterizers;
import featureCharacterization.LandMarkerException;
import featureCharacterization.TypeException;
import weka.core.Attribute;
import weka.core.Instances;

// calculateur et conteneur pour les dmfs des features d'un dataset
public class FeaturesDmfs implements Serializable {

	private static final long serialVersionUID = -6236218596897574048L;
	private Map<Integer, Map<String, Double>> featuresDmfs;
	private Map<String, Double> targetDmfs;

	public FeaturesDmfs(Instances dataset) throws Exception {
		featuresDmfs = new HashMap<Integer, Map<String, Double>>();

		// MF des features
		Attribute feature;
		Map<String, Double> featureDmfs;
		for (Enumeration<Attribute> features = dataset.enumerateAttributes(); features.hasMoreElements();) {
			feature = features.nextElement();
			featureDmfs = new HashMap<String, Double>();

			for (FeatureCharacterizer dc : FeatureCharacterizers.getCharacterizers()) {
				try {
					
					featureDmfs.putAll(dc.characterize(dataset, feature));		
				} catch (TypeException | LandMarkerException e) {
					for (String id : dc.getIDs()) {
						featureDmfs.put(id, Double.NaN);
					}
				}
			}

			featuresDmfs.put(feature.index(), featureDmfs);
		}

		// MF de la cible
		Attribute target = dataset.classAttribute();
		featureDmfs = new HashMap<String, Double>();
		for (FeatureCharacterizer dc : FeatureCharacterizers.getCharacterizers()) {
			try {
				featureDmfs.putAll(dc.characterize(dataset, target));
			} catch (Exception e) {
				for (String id : dc.getIDs()) {
					featureDmfs.put(id, Double.NaN);
				}
			}
		}
		targetDmfs = featureDmfs;
	}


	public Set<Integer> getFeatureIndexes() {
		return featuresDmfs.keySet();
	}

	public Double getTargetDmf(String mfKey) {
		return targetDmfs.get(mfKey);
	}

	public double getFeatureDmf(int index, String mfKey) {
		if (featuresDmfs.get(index) != null) {
			return featuresDmfs.get(index).get(mfKey);
		} else {
			return Double.NaN;
		}
	}
}
