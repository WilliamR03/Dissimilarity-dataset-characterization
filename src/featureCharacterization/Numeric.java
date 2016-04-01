package featureCharacterization;

import java.util.HashMap;
import java.util.Map;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class Numeric extends FeatureCharacterizer {
	
	protected final String[] ids = new String[] { "Mean", "StdDev", "Skewness", "Kurtosis" };

	@Override
	public String[] getIDs() {
		return ids;
	}

	@Override
	public Map<String, Double> characterize(Instances dataset, Attribute target) throws Exception {

		if(!target.isNumeric()){
			throw new TypeException("Non numeric att : skip numeric mfs");
		}
		
		double mean = dataset.meanOrMode(target.index());
		double stddev = Math.sqrt(dataset.variance(target.index()));
		

		double moment3 = 0;
		double moment4 = 0;
		int nbVals = 0;
		for (int i = 0; i < dataset.numInstances(); i++) {
			Instance instance = dataset.instance(i);
			if (!instance.isMissing(target)) {
				nbVals++;
				moment3 += Math.pow(instance.value(target) - mean, 3);
				moment4 += Math.pow(instance.value(target) - mean, 4);
			}
		}
		moment3 /= nbVals;
		moment4 /= nbVals;
		
		double skewness = moment3 / Math.pow(stddev, 3);
		double kurtosis = (moment4 / Math.pow(stddev, 4)) - 3;

		Map<String, Double> qualities = new HashMap<String, Double>();
		
		//Mean
		qualities.put(ids[0], mean);
		//StdDev
		qualities.put(ids[1], stddev);
		//Skewness
		qualities.put(ids[2], skewness);
		//Kurtosis
		qualities.put(ids[3], kurtosis);
		
		return qualities;
	}

}
