package dissim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openml.apiconnector.io.OpenmlConnector;
import org.openml.apiconnector.xml.DataSetDescription;

import featureCharacterization.FeatureCharacterizers;
import main.Controller;
import test.testController;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class FeaturesDissimilarity implements Serializable {

	private static final long serialVersionUID = 1L;
	private static FeaturesDissimilarity featuresDissimilarity = null;

	public static FeaturesDissimilarity get() throws Exception {
		if (featuresDissimilarity == null) {
			// TODO testController -> Controller
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(testController.dissimFile));
			featuresDissimilarity = (FeaturesDissimilarity) ois.readObject();
			ois.close();
		}
		return featuresDissimilarity;
	}

	private ArrayList<Integer> datasetsList;
	// valeurs normalisees dissimF(X1,X2) / max dissimF
	private double[][] values;

	public double dissimF(int id1, int id2) throws Exception {
		// i et j indexs de id1 et 2
		int i = datasetsList.indexOf(id1);
		int j = datasetsList.indexOf(id2);

		if (i == -1 || j == -1)
			throw new Exception();

		// return dissimF(X1,X2) / max dissimF
		if (i > j) {
			return values[j][i];
		} else if (j > i) {
			return values[i][j];
		} else {
			return 0;
		}
	}

	public static void main(String[] args) throws Exception {

		ObjectOutputStream oos;
		FeaturesDissimilarity fd;

		switch (args[0]) {
		case "1":

			fd = new FeaturesDissimilarity();

			// ouvrir le connecteur openML
			OpenmlConnector openML = new OpenmlConnector("ad6244a6f01a5c9fc4985a0875b30b97");

			// open workbook elements
			FileInputStream streamElems = new FileInputStream(testController.elementsFile);
			OPCPackage opcElems = OPCPackage.open(streamElems);
			Workbook wbElems = WorkbookFactory.create(opcElems);

			// lire et download datasets
			ArrayList<File> datasetsFiles = new ArrayList<File>();
			fd.datasetsList = new ArrayList<Integer>();
			Sheet sheetDatasets = wbElems.getSheet("Datasets");
			for (Row row : sheetDatasets) {
				if (row.getRowNum() != 0 && row.getCell(1) != null) {
					int idOml = (int) row.getCell(1).getNumericCellValue();
					fd.datasetsList.add(idOml);

					File dataFile = new File(testController.dataDir, "dataset_" + idOml + ".arff");
					if (!dataFile.exists()) {
						System.out.println("dataset_" + idOml + ".arff missing, downloading...");
						DataSetDescription description = openML.dataGet(idOml);
						String url = openML.getOpenmlFileUrl(description.getFile_id(), dataFile.getName()).toString();
						dataFile = OpenmlConnector.getFileFromUrl(url, dataFile.getAbsolutePath());
					}
					datasetsFiles.add(dataFile);
				}
			}

			// close workbook elements
			opcElems.close();
			streamElems.close();

			// calcul des MF des features des datasets
			for (int i = 0; i < fd.datasetsList.size(); i++) {
				// creer et nettoyer le dataset
				File dmfsFile = new File(testController.dmfsDir, "fdmfs_dataset_" + fd.datasetsList.get(i));
				if (!dmfsFile.exists()) {
					try {
						Instances dataset = DataSource.read(datasetsFiles.get(i).getAbsolutePath());
						DataSetDescription description = null;

						try {
							description = openML.dataGet(fd.datasetsList.get(i));
						} catch (Exception e) {
						}

						if (description == null || description.getDefault_target_attribute() == null) {
							dataset.setClass(dataset.attribute("class"));
						} else {
							dataset.setClass(dataset.attribute(description.getDefault_target_attribute()));
							String[] delList = description.getIgnore_attribute();
							if (delList != null) {
								for (String del : delList) {
									dataset.deleteAttributeAt(dataset.attribute(del).index());
								}
							}
						}

						// calculer et enregistrement les MF des features
						oos = new ObjectOutputStream(new FileOutputStream(dmfsFile));
						oos.writeObject(new FeaturesDmfs(dataset));
						oos.close();
					} catch (Exception e) {
						System.out.println("fail on dataset " + fd.datasetsList.get(i));
						e.printStackTrace();
					}
				}
			}

			// test
			// Thread.sleep(1000);
			// System.out.println("Done : calcul des MF des features des datasets");
			// FeaturesDmfs featuresDmfs = datasetsFeaturesDmfs.get(0);
			// System.out.println("******* Dataset 0 : ");
			// for (int mf = 0; mf < FeatureCharacterizers.nbMetaFeatures(); mf++) {
			// String mfKey = FeatureCharacterizers.getMfList().get(mf);
			// System.out.println("************** MF : " + mfKey);
			// System.out.println("class = " + featuresDmfs.getTargetDmf(mfKey));
			// for (int f1 : featuresDmfs.getFeatureIndexes()) {
			// featuresDmfs.getFeatureDmf(f1, mfKey);
			// System.out.println("att " + f1 + " = " + featuresDmfs.getFeatureDmf(f1, mfKey));
			// }
			// }

			// enregistrement
			oos = new ObjectOutputStream(new FileOutputStream(testController.dissimFile));
			oos.writeObject(fd);
			oos.close();
			break;

		case "2":

			// reprise
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(testController.dissimFile));
			fd = (FeaturesDissimilarity) ois.readObject();
			ois.close();

			ArrayList<FeaturesDmfs> datasetsFeaturesDmfs = new ArrayList<FeaturesDmfs>();
			for (int i = 0; i < fd.datasetsList.size(); i++) {
				// récupérer les MF des features
				ois = new ObjectInputStream(new FileInputStream(new File(testController.dmfsDir, "fdmfs_dataset_" + fd.datasetsList.get(i))));
				datasetsFeaturesDmfs.add((FeaturesDmfs) ois.readObject());
				ois.close();
			}

			System.out.println("Step 1 : pour chaque mf, calculer son écart max parmis tous les features de tous les datasets");

			// pour chaque mf, calculer son écart max parmis tous les features de tous les datasets
			double pbSize = (fd.datasetsList.size() * (fd.datasetsList.size() - 1)) / 2;
			double[] maxMfDissims = new double[FeatureCharacterizers.nbMetaFeatures()];
			for (int mf = 0; mf < FeatureCharacterizers.nbMetaFeatures(); mf++) {
				String mfKey = FeatureCharacterizers.getMfList().get(mf);
				System.out.println("*** mf " + mf + " / " + FeatureCharacterizers.nbMetaFeatures() + " : " + mfKey);
				maxMfDissims[mf] = 0;
				int progress = 0;
				int progressPercent = 0;
				// pour chaque paire de datasets
				for (int i = 0; i < fd.datasetsList.size(); i++) {
					for (int j = i + 1; j < fd.datasetsList.size(); j++) {
						// System.out.println("------ datasets = " + fd.datasetsList.get(i) + ", " + fd.datasetsList.get(j));
						if (progressPercent < (int) Math.floor((progress++) / pbSize)) {
							progressPercent = (int) Math.floor((progress) / pbSize);
							System.out.println(progressPercent + " ");
						}
						FeaturesDmfs dmfs1 = datasetsFeaturesDmfs.get(i);
						FeaturesDmfs dmfs2 = datasetsFeaturesDmfs.get(j);
						// pour chaque paire de features
						for (int f1 : dmfs1.getFeatureIndexes()) {
							for (int f2 : dmfs2.getFeatureIndexes()) {
								double mfDissim = FeatureDmfComparator.compare(mfKey, dmfs1.getFeatureDmf(f1, mfKey), dmfs2.getFeatureDmf(f2, mfKey));
								if (Double.isFinite(mfDissim) && mfDissim > maxMfDissims[mf]) {
									// System.out.println("new max : " + mfDissim);
									maxMfDissims[mf] = mfDissim;
								}
							}
						}
						// et pour les cibles
						double mfDissim = FeatureDmfComparator.compare(mfKey, dmfs1.getTargetDmf(mfKey), dmfs2.getTargetDmf(mfKey));
						if (Double.isFinite(mfDissim) && mfDissim > maxMfDissims[mf]) {
							maxMfDissims[mf] = mfDissim;
						}
					}
				}
			}

			System.out.println("Done 1 : pour chaque mf, calculer son écart max parmis tous les features de tous les datasets");
			System.out.println();
			System.out.println("Step 2 : calculer la dissim selon les features entre chaque paire de datasets de la liste");

			// calculer la dissim selon les features entre chaque paire de datasets de la liste
			double maxValue = 0;
			double[][] tmpValues = new double[fd.datasetsList.size()][fd.datasetsList.size()];
			for (int i = 0; i < fd.datasetsList.size(); i++) {
				for (int j = 0; j < fd.datasetsList.size(); j++) {
					if (i >= j) {
						// dissim symetrique
						tmpValues[i][j] = 0;
					} else {
						System.out.println("*** datasets = " + fd.datasetsList.get(i) + ", " + fd.datasetsList.get(j));
						// dissim selon les features entre les datasets i et j
						// System.out.println("*********** dist " + i + " ," + j);
						FeaturesDmfs dmfs1;
						FeaturesDmfs dmfs2;
						// on ordonne les datasets : d1.nbFeatures >= d2.nbFeatures
						if (datasetsFeaturesDmfs.get(j).getFeatureIndexes().size() < datasetsFeaturesDmfs.get(i).getFeatureIndexes().size()) {
							dmfs1 = datasetsFeaturesDmfs.get(i);
							dmfs2 = datasetsFeaturesDmfs.get(j);
						} else {
							dmfs1 = datasetsFeaturesDmfs.get(j);
							dmfs2 = datasetsFeaturesDmfs.get(i);
						}

						// min sur les permutations des features ? plutot en greedy pour le moment...

						double sum = 0;
						// somme sur les mf pour la cible
						for (int mf = 0; mf < FeatureCharacterizers.nbMetaFeatures(); mf++) {
							String mfKey = FeatureCharacterizers.getMfList().get(mf);
							double tmp = (FeatureDmfComparator.compare(mfKey, dmfs1.getTargetDmf(mfKey), dmfs2.getTargetDmf(mfKey))) / maxMfDissims[mf];
							if (!Double.isFinite(tmp)) {
								tmp = 1;
							}
							sum += tmp;
						}

						// somme sur les features
						int progressPercent = 0;
						int progress = 0;
						Set<Integer> featureSet1 = dmfs1.getFeatureIndexes();
						// List<Integer> featureSet2 = Arrays.asList(dmfs2.getFeatureIndexes().toArray(new Integer[0]));
						ArrayList<Integer> featureSet2 = new ArrayList<Integer>(dmfs2.getFeatureIndexes());
						for (int f1 : featureSet1) {

							// System.out.println("------ features(d1) : " + progress++ + " / " + featureSet1.size());
							if (progressPercent < (int) Math.floor((progress++) / featureSet1.size())) {
								progressPercent = (int) Math.floor((progress) / featureSet1.size());
								System.out.println(progressPercent + " ");
							}

							if (featureSet2.isEmpty()) {
								// si f1 surnuméraire, dist au null
								for (int mf = 0; mf < FeatureCharacterizers.nbMetaFeatures(); mf++) {
									String mfKey = FeatureCharacterizers.getMfList().get(mf);
									double tmp = (FeatureDmfComparator.compareNull(mfKey, dmfs1.getFeatureDmf(f1, mfKey))) / maxMfDissims[mf];
									if (!Double.isFinite(tmp)) {
										tmp = 1;
									}
									sum += tmp;
								}
							} else {
								// sinon parmis les features restants de 2, trouver le plus proche
								double distMin = Double.MAX_VALUE;
								int indMin = -1;

								for (int f2 : featureSet2) {
									// somme sur les meta features
									double distf1f2 = 0;
									for (int mf = 0; mf < FeatureCharacterizers.nbMetaFeatures(); mf++) {
										String mfKey = FeatureCharacterizers.getMfList().get(mf);
										double tmp = (FeatureDmfComparator.compare(mfKey, dmfs1.getFeatureDmf(f1, mfKey), dmfs2.getFeatureDmf(f2, mfKey)))
												/ maxMfDissims[mf];
										if (!Double.isFinite(tmp)) {
											tmp = 1;
										}
										distf1f2 += tmp;
									}
									if (distf1f2 < distMin) {
										distMin = distf1f2;
										indMin = f2;
									}
								}

								// System.out.println("remove " + indMin);
								if (featureSet2.remove((Integer) indMin)) {
									sum += distMin;
								} else {
									throw new Exception();
								}
							}
						}

						// TODO a check : normalisation par le nombre de features
						sum /= featureSet1.size();

						tmpValues[i][j] = sum;
						if (sum > maxValue) {
							maxValue = sum;
							// System.out.println("maxValue = " + maxValue);
						}
						// System.out.println("dist = " + sum);
						// Thread.sleep(1000);
					}
				}
			}

			System.out.println("Done 2 : calculer la dissim selon les features entre chaque paire de datasets de la liste");

			// normaliser et enregistrer
			fd.values = new double[fd.datasetsList.size()][fd.datasetsList.size()];
			for (int i = 0; i < fd.datasetsList.size(); i++) {
				for (int j = 0; j < fd.datasetsList.size(); j++) {
					fd.values[i][j] = tmpValues[i][j] / maxValue;
				}
			}

			// et serialiser le tout
			oos = new ObjectOutputStream(new FileOutputStream(testController.dissimFile));
			oos.writeObject(fd);
			oos.close();

			System.out.println("Done : normaliser et serialiser");
			break;
		default:
			System.out.println("Fail : indiquer l'étape de clacul");
		}
	}
}
