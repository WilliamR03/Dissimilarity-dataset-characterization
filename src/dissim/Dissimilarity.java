package dissim;

import java.io.Serializable;
import java.util.Enumeration;

import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.neighboursearch.PerformanceStats;

public class Dissimilarity implements DistanceFunction, Serializable {

	private static final long serialVersionUID = 1L;
	private Instances instances = null;
	private double[] maxGi;
	private double maxG;

	public Dissimilarity() {
	}


	@Override
	public double distance(Instance first, Instance second, double cutOffValue, PerformanceStats stats) {
		return dissimG(first, second) + dissimF(first, second);
	}

	public double distance(int id1, Instance first, int id2, Instance second, double cutOffValue, PerformanceStats stats) {
		return dissimG(first, second) + dissimF(id1, id2);
	}

	public double dissimF(Instance first, Instance second) {
		// DMFs de description des features
		// F = min sum (...)
		try {
			//double fNorm = FeaturesDissimilarity.get().dissimF(first.getDatasetId(), second.getDatasetId());
			//return fNorm;
			return 0;
		} catch (Exception e) {
			System.err.println("dissimF fail");
			return 0;
		}
	}
	
	public double dissimF(int id1, int id2) {
		// DMFs de description des features
		// F = min sum (...)
		try {
			double fNorm = FeaturesDissimilarity.get().dissimF(id1, id2);
			return fNorm;
		} catch (Exception e) {
			System.err.println("dissimF fail");
			return 0;
		}
	}

	public double dissimG(Instance first, Instance second) {
		try {
			// DMFs generaux
			// G = sum( gi(i1,i2) / max gi(instances) )
			double G = 0;
			double Gi = 0;
			for (int att = 0; att < instances.numAttributes() - 1; att++) {
				Gi = GeneralDmfComparator.compare(instances.attribute(att).name(), first.value(att), second.value(att));
				// System.out.println("Gi= "+Gi);
				if (!Double.isFinite(Gi)) {
					// || Gi > maxGi[att]
					G += 1;
				} else if (maxGi[att] != 0) {
					G += Gi / (maxGi[att]);
				}
			}

			// gNorm = G / max G
			double gNorm = 0;
			if (!Double.isFinite(G)) {
				// || G > maxG
				gNorm = 1;
			} else {
				gNorm = G / maxG;
			}
			return gNorm;
		} catch (Exception e) {
			System.err.println("dissimG fail");
			return 0;
		}
	}

	@Override
	public void setInstances(Instances insts) {
		try {
			instances = insts;

			// calcul de dissim max sur chaque MF general (sauf la classe)
			maxGi = new double[instances.numAttributes() - 1];
			double g = 0;
			for (int att = 0; att < instances.numAttributes() - 1; att++) {
				maxGi[att] = 0;
				for (int i = 0; i < instances.numInstances(); i++) {
					for (int j = i + 1; j < instances.numInstances(); j++) {
						// g = Math.abs(instances.get(i).value(att) - instances.get(j).value(att));
						g = GeneralDmfComparator.compare(instances.attribute(att).name(), instances.get(i).value(att), instances.get(j).value(att));
						if (Double.isFinite(g) && g > maxGi[att]) {
							maxGi[att] = g;
						}
					}
				}
			}

			// calcul de dissim max sur les DMF generaux
			maxG = 0;
			double G = 0;
			for (int i = 0; i < instances.numInstances(); i++) {
				for (int j = i + 1; j < instances.numInstances(); j++) {
					G = 0;
					double Gi = 0;
					for (int att = 0; att < instances.numAttributes() - 1; att++) {
						Gi = GeneralDmfComparator.compare(instances.attribute(att).name(), instances.get(i).value(att), instances.get(j).value(att));
						if (!Double.isFinite(Gi)) {
							G += 1;
						} else if (maxGi[att] != 0) {
							G += Gi / (maxGi[att]);
						}
					}
					if (Double.isFinite(G) && G > maxG) {
						maxG = G;
					}
				}
			}

		} catch (Exception e) {
			System.err.println("Fail on Dissimilarity init : setInstances");
			e.printStackTrace();
		}
	}

	@Override
	public void update(Instance ins) {
		try {
			// calcul de dissim max sur chaque MF general (sauf la classe)
			double g = 0;
			for (int att = 0; att < instances.numAttributes() - 1; att++) {
				for (int i = 0; i < instances.numInstances(); i++) {
					g = GeneralDmfComparator.compare(instances.attribute(att).name(), instances.get(i).value(att), ins.value(att));
					if (Double.isFinite(g) && g > maxGi[att]) {
						maxGi[att] = g;
					}
				}
			}

			// calcul de dissim max sur les DMF generaux
			double G = 0;
			for (int i = 0; i < instances.numInstances(); i++) {
				G = 0;
				double Gi = 0;
				for (int att = 0; att < instances.numAttributes() - 1; att++) {
					Gi = GeneralDmfComparator.compare(instances.attribute(att).name(), instances.get(i).value(att), ins.value(att));
					if (!Double.isFinite(Gi)) {
						G += 1;
					} else if (maxGi[att] != 0) {
						G += Gi / (maxGi[att]);
					}
				}
				if (Double.isFinite(G) && G > maxG) {
					maxG = G;
				}
			}

		} catch (Exception e) {
			System.err.println("Fail on Dissimilarity update : " + ins);
			e.printStackTrace();
		}
	}

	@Override
	public Instances getInstances() {
		return instances;
	}

	@Override
	public void clean() {
		instances = new Instances(instances, 0);
	}

	// ************************
	// Fonctions inutiles de l'interface
	// ************************

	@Override
	public double distance(Instance first, Instance second) {
		return distance(first, second, Double.POSITIVE_INFINITY, null);
	}

	@Override
	public double distance(Instance first, Instance second, PerformanceStats stats) {
		return distance(first, second, Double.POSITIVE_INFINITY, stats);
	}

	@Override
	public double distance(Instance first, Instance second, double cutOffValue) {
		return distance(first, second, cutOffValue, null);
	}

	@Override
	public void postProcessDistances(double[] distances) {
	}

	@Override
	public Enumeration<Option> listOptions() {
		// System.err.println("Appel methode inutile : listOptions");
		return null;
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		// System.err.println("Appel methode inutile : setOptions");
	}

	@Override
	public String[] getOptions() {
		return new String[0];
	}

	@Override
	public void setAttributeIndices(String value) {
	}

	@Override
	public String getAttributeIndices() {
		// System.err.println("Appel methode inutile : getAttributeIndices");
		return null;
	}

	@Override
	public void setInvertSelection(boolean value) {
	}

	@Override
	public boolean getInvertSelection() {
		// System.err.println("Appel methode inutile : getInvertSelection");
		return false;
	}

}
