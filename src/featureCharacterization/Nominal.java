package featureCharacterization;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class Nominal extends FeatureCharacterizer {

	protected final String[] ids = new String[] { "NbDistinctValues", "InfoGain", "Entropy", "NoiseToSignalRatio", "EquivalentNumberOfAtts", "MinRelSize",
			"MaxRelSize" };

	@Override
	public String[] getIDs() {
		return ids;
	}

	@Override
	public Map<String, Double> characterize(Instances dataset, Attribute target) throws Exception {

		if (!target.isNominal()) {
			throw new TypeException("Non nominal att : skip nominal mfs");
		}

		// calcul des valeurs utiles
		double[] classCounts = new double[dataset.numClasses()];
		for (Enumeration<Instance> instEnum = dataset.enumerateInstances(); instEnum.hasMoreElements();) {
			Instance inst = instEnum.nextElement();
			classCounts[(int) inst.classValue()]++;
		}

		double classEntropy = 0;
		for (int j = 0; j < dataset.numClasses(); j++) {
			if (classCounts[j] > 0) {
				classEntropy -= (classCounts[j] / dataset.numInstances()) * Utils.log2((classCounts[j] / dataset.numInstances()));
			}
		}

		double infoGain = classEntropy;
		Instances[] splitData = new Instances[target.numValues()];
		for (int j = 0; j < target.numValues(); j++) {
			splitData[j] = new Instances(dataset, dataset.numInstances());
		}
		for (Enumeration<Instance> instEnum = dataset.enumerateInstances(); instEnum.hasMoreElements();) {
			Instance inst = instEnum.nextElement();
			splitData[(int) inst.value(target)].add(inst);
		}
		for (Instances split : splitData) {
			split.compactify();
		}

		for (int j = 0; j < target.numValues(); j++) {
			if (splitData[j].numInstances() > 0) {
				double[] splitClassCounts = new double[splitData[j].numClasses()];
				for (Enumeration<Instance> instEnum = splitData[j].enumerateInstances(); instEnum.hasMoreElements();) {
					Instance inst = instEnum.nextElement();
					splitClassCounts[(int) inst.classValue()]++;
				}

				double splitEntropy = 0;
				for (int k = 0; k < splitData[j].numClasses(); k++) {
					if (splitClassCounts[k] > 0) {
						splitEntropy -= (splitClassCounts[k] / splitData[j].numInstances()) * Utils.log2((splitClassCounts[k] / splitData[j].numInstances()));
					}
				}

				infoGain -= ((double) splitData[j].numInstances() / (double) dataset.numInstances()) * splitEntropy;
			}
		}

		double[] attValueCounts = new double[target.numValues()];
		for (int i = 0; i < dataset.numInstances(); i++) {
			Instance inst = dataset.instance(i);
			attValueCounts[(int) inst.value(target)]++;
		}
		double attEntropy = 0;
		for (int c = 0; c < target.numValues(); c++) {
			if (attValueCounts[c] > 0) {
				attEntropy -= (attValueCounts[c] / dataset.numInstances()) * (weka.core.Utils.log2(attValueCounts[c] / dataset.numInstances()));
			}
		}

		double minRelSize = Double.MAX_VALUE;
		double maxRelSize = Double.MIN_VALUE;
		for (double c : attValueCounts) {
			if (c > 0 && c < minRelSize) {
				minRelSize = c;
			} else if (c > 0 && c > maxRelSize) {
				maxRelSize = c;
			}
		}

		// puis des qualités
		Map<String, Double> qualities = new HashMap<String, Double>();

		// NbDistinctValues
		qualities.put(ids[0], (double) dataset.numDistinctValues(target));

		// InfoGain
		qualities.put(ids[1], infoGain);

		// Entropy
		qualities.put(ids[2], attEntropy);

		// NoiseToSignalRatio
		double noiseSignalRatio = (attEntropy - infoGain) / infoGain;
		qualities.put(ids[3], noiseSignalRatio);

		// EquivalentNumberOfAtts
		double ena = classEntropy / infoGain;
		qualities.put(ids[4], ena);

		// MinRelSize ( = nb d'instance portant la valeur d'att cible la moins représentée / nb d'instances)
		minRelSize /= dataset.numInstances();
		qualities.put(ids[5], minRelSize);

		// MaxRelSize ( = nb d'instance portant la valeur d'att cible la plus représentée / nb d'instances)
		maxRelSize /= dataset.numInstances();
		qualities.put(ids[6], maxRelSize);

		return qualities;
	}

}
