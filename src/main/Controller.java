package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import metaPb.ModelPerf;
import metaPb.SimpleClassif;
import weka.core.Capabilities.Capability;

/**
 * Creates the different learning tasks sequentially and submits them for parallel execution
 */
public class Controller {

	public static final File mainDir = new File("/users/sig/wraynaut/java/");
	public static final File metaDataFile = new File(mainDir, "data/Data_Criteres.xlsm");
	public static final File metaInstancesFile = new File(mainDir, "data/Data_DMF.xlsm");
	public static final File elementsFile = new File(mainDir, "data/Elements.xlsx");
	public static final File dissimFile = new File(mainDir, "data/dissimilarity.ser");
	public static final File dmfsDir = new File(mainDir, "data/featuresDmfs");

	public static Connection connexion() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://co2-ni02.irit.fr:3306/mlexp", "wraynaut", "077007");
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {

			// 0 metaPb a instancier SimpleClassif=0 ModelPerf=1 ModelApp=2 ModelPaire=3
			int metaPb = Integer.parseInt(args[0]);
			// 1 nombre de jobs a créer (0 pour autant que possible)
			int maxNewJobs = Integer.parseInt(args[1]);
			// 2 maximum time for a job to complete (en minute)
			int jobTimer = Integer.parseInt(args[2]);
			// 3 instancier ibk sur dissim (1 pour oui)
			boolean dissim = args[3].equals("1");

			// compteur job courant
			int jobId = 0;
			
			// delai minimum entre deux soumissions de jobs (en ms)
			int delay = 5000;

			// initialisation du jobManager
			JobManager jobManager = new JobManager();

			// Chargement du driver JDBC pour MySQL
			Class.forName("com.mysql.jdbc.Driver");

			// Connexion à la base de données
			Connection connexion = connexion();
			Statement statement;
			ResultSet rset;

			// *************************************************************
			// construire listes meta algos, mf selection, criteres & DMF
			// *************************************************************

			// construire la table des classifieurs
			ArrayList<Learner> classifiers = Learner.init(dissim);

			// construire la table des caracterisations
			ArrayList<FeatureSelector> featureSelectors = FeatureSelector.init();

			// open workbook elements
			FileInputStream streamElems = new FileInputStream(elementsFile);
			OPCPackage opcElems = OPCPackage.open(streamElems);
			Workbook wbElems = WorkbookFactory.create(opcElems);

			// lire criteres
			ArrayList<Critere> critList = new ArrayList<Critere>();
			Sheet sheetCrit = wbElems.getSheet("Criteres");
			for (Row row : sheetCrit) {
				if (row.getRowNum() != 0 && row.getCell(0) != null && row.getCell(3) != null && row.getCell(4) != null && row.getCell(5) != null
						&& row.getCell(6) != null && row.getCell(7) != null) {
					double min = 0;
					double max = 0;
					if (row.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC) {
						min = row.getCell(4).getNumericCellValue();
					} else if (row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING && row.getCell(4).getStringCellValue().equals("min")) {
						min = Double.MIN_VALUE;
					} else {
						throw new Exception("Fail lecture criteres in " + elementsFile);
					}
					if (row.getCell(5).getCellType() == Cell.CELL_TYPE_NUMERIC) {
						max = row.getCell(5).getNumericCellValue();
					} else if (row.getCell(5).getCellType() == Cell.CELL_TYPE_STRING && row.getCell(5).getStringCellValue().equals("max")) {
						max = Double.MAX_VALUE;
					} else {
						throw new Exception("Fail lecture criteres in " + elementsFile);
					}
					critList.add(new Critere(row.getCell(0).getStringCellValue(), (row.getCell(3).getNumericCellValue() == 1), min, max,
							(row.getCell(7).getNumericCellValue() == 1), row.getCell(6).getNumericCellValue()));
				}
			}

			// lire DMFs
			ArrayList<Dmf> dmfList = new ArrayList<Dmf>();
			Sheet sheetDmf = wbElems.getSheet("Data meta-features");
			for (Row row : sheetDmf) {
				if (row.getRowNum() != 0 && row.getCell(0) != null && row.getCell(1) != null) {
					dmfList.add(new Dmf(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue()));
				}
			}

			// close workbook elements
			opcElems.close();
			streamElems.close();

			// serialiser criteres , DMFs et datasets
			File critFile = new File(mainDir, "data/criteres.ser");
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(critFile));
			oos.writeObject(critList);
			oos.close();
			File dmfFile = new File(mainDir, "data/Dmfs.ser");
			oos = new ObjectOutputStream(new FileOutputStream(dmfFile));
			oos.writeObject(dmfList);
			oos.close();

			System.out.println("*******************");
			System.out.println("Meta-learners : " + classifiers.size());
			System.out.println("Meta-features selection methods : " + featureSelectors.size());
			System.out.println("Resulting jobs per meta-problem : " + classifiers.size() * featureSelectors.size());
			System.out.println("*******************");
			System.out.println("**** Main loop ****");
			System.out.println("*******************");

			// itérer sur les classifiers
			for (int i = 0; i < classifiers.size(); i++) {
				Learner learner = classifiers.get(i);

				// obtenir l'id de l'algo courant
				int idAlgo;
				statement = connexion.createStatement();
				rset = statement.executeQuery("SELECT * FROM `algorithm` WHERE name=\"" + learner.getNom().replaceAll("[^-a-zA-Z_0-9 /.]", "") + "\"");
				if (rset.next()) {
					idAlgo = rset.getInt("id");
				} else {
					statement.executeUpdate("INSERT INTO `algorithm` (name) VALUES ('" + learner.getNom().replaceAll("[^-a-zA-Z_0-9 /.]", "") + "')",
							Statement.RETURN_GENERATED_KEYS);
					ResultSet keySet = statement.getGeneratedKeys();
					keySet.next();
					idAlgo = keySet.getInt(1);
					keySet.close();
				}
				rset.close();
				statement.close();

				// itérer sur les feature selectors
				for (FeatureSelector featureSelector : featureSelectors) {

					// obtenir l'id de la fs search courante
					int idFsSearch;
					statement = connexion.createStatement();
					rset = statement.executeQuery("SELECT * FROM `fsearch` WHERE name=\"" + featureSelector.getNomSearch() + "\"");
					if (rset.next()) {
						idFsSearch = rset.getInt("id");
					} else {
						statement.executeUpdate("INSERT INTO `fsearch` (name) VALUES ('" + featureSelector.getNomSearch() + "')",
								Statement.RETURN_GENERATED_KEYS);
						ResultSet keySet = statement.getGeneratedKeys();
						keySet.next();
						idFsSearch = keySet.getInt(1);
						keySet.close();
					}
					rset.close();
					statement.close();

					// obtenir l'id de la fs eval courante
					int idFsEval;
					statement = connexion.createStatement();
					rset = statement.executeQuery("SELECT * FROM `feval` WHERE name=\"" + featureSelector.getNomEval() + "\"");
					if (rset.next()) {
						idFsEval = rset.getInt("id");
					} else {
						statement.executeUpdate("INSERT INTO `feval` (name) VALUES ('" + featureSelector.getNomEval() + "')", Statement.RETURN_GENERATED_KEYS);
						ResultSet keySet = statement.getGeneratedKeys();
						keySet.next();
						idFsEval = keySet.getInt(1);
						keySet.close();
					}
					rset.close();
					statement.close();

					// tester si on arrete (assez de jobs generes)
					if (maxNewJobs != 0 && jobId > maxNewJobs) {
						return;
					}

					try {

						// puis generer les meta problemes
						switch (metaPb) {
						case 0:
							// *********** SimpleClassif ********************
							// tester si le learner peut resoudre le meta pb (ie, le job a lieu d'exister)
							if (learner.getClassifier().getCapabilities().handles(Capability.MISSING_VALUES)
									&& learner.getClassifier().getCapabilities().handles(Capability.NOMINAL_CLASS)
									&& learner.getClassifier().getCapabilities().handles(Capability.NUMERIC_ATTRIBUTES)
									&& featureSelector.getFilter().getCapabilities().handles(Capability.MISSING_VALUES)
									&& featureSelector.getFilter().getCapabilities().handles(Capability.NOMINAL_CLASS)
									&& featureSelector.getFilter().getCapabilities().handles(Capability.NUMERIC_ATTRIBUTES)) {

								// tester si le job est deja effectue
								statement = connexion.createStatement();
								rset = statement.executeQuery("SELECT * FROM `runs` WHERE metaproblem=\"" + SimpleClassif.class.getSimpleName()
										+ "\" AND algorithm=\"" + idAlgo + "\" AND fsearch=\"" + idFsSearch + "\" AND feval=\"" + idFsEval + "\"");
								if (!rset.next()) {
									// si échoué ou jamais lancé, créer le job
									System.out.println("********* Creating job : " + jobId + " ************");
									System.out.println(
											"SimpleClassif " + featureSelector.getNomSearch() + " " + featureSelector.getNomEval() + " " + learner.getNom());

									SimpleClassif scCurrent = new SimpleClassif(jobId, idAlgo, idFsSearch, idFsEval, learner, featureSelector, critFile,
											dmfFile, metaDataFile, metaInstancesFile);

									// et lancer sur le cluster
									scCurrent.create();
									scCurrent = null;
									jobManager.run(jobId, jobTimer);
									jobId++;
								}
								rset.close();
								statement.close();
							}
							break;

						case 1:
							// *********** ModelPerf ********************
							// tester si le learner peut resoudre le meta pb
							if (learner.getClassifier().getCapabilities().handles(Capability.MISSING_VALUES)
									&& learner.getClassifier().getCapabilities().handles(Capability.NUMERIC_CLASS)
									&& learner.getClassifier().getCapabilities().handles(Capability.NUMERIC_ATTRIBUTES)
									&& featureSelector.getFilter().getCapabilities().handles(Capability.MISSING_VALUES)
									&& featureSelector.getFilter().getCapabilities().handles(Capability.NUMERIC_CLASS)
									&& featureSelector.getFilter().getCapabilities().handles(Capability.NUMERIC_ATTRIBUTES)) {

								// tester si le job est deja effectue
								statement = connexion.createStatement();
								rset = statement.executeQuery("SELECT * FROM `runs` WHERE metaproblem=\"" + ModelPerf.class.getSimpleName()
										+ "\" AND algorithm=\"" + idAlgo + "\" AND fsearch=\"" + idFsSearch + "\" AND feval=\"" + idFsEval + "\"");
								if (!rset.next()) {
									// si échoué ou jamais lancé, créer le job
									System.out.println("********* Creating job : " + jobId + " ************");
									System.out.println(
											"ModelPerf " + featureSelector.getNomSearch() + " " + featureSelector.getNomEval() + " " + learner.getNom());

									ModelPerf mpCurrent = new ModelPerf(jobId, idAlgo, idFsSearch, idFsEval, learner, featureSelector, critFile, dmfFile,
											metaDataFile, metaInstancesFile);

									// et lancer sur le cluster
									mpCurrent.create();
									mpCurrent = null;
									jobManager.run(jobId, jobTimer);
									jobId++;
								}
								rset.close();
								statement.close();
							}
							break;

						case 2:
							// *********** ModelApp ********************
							// tester si le learner peut resoudre le meta pb
							break;

						case 3:
							// *********** ModelPaire ********************
							// tester si le learner peut resoudre le meta pb
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					// *********** Synchro ********************
					int pendings = 1;
					try {
						
						Thread.sleep(delay);
						// attendre que plus de pendings avant de reprendre
						while (pendings > 0) {
							Process squeue = Runtime.getRuntime().exec(new File(mainDir, "getPendings.sh").getAbsolutePath());
							squeue.waitFor();
							BufferedReader res = new BufferedReader(new InputStreamReader(squeue.getInputStream()));
							pendings = Integer.parseInt(res.readLine());
							res.close();
							
							if(pendings > 0) {
								System.out.println("Waiting for pendings : " + pendings);
								Thread.sleep(300000);
							}
						}
					} catch (InterruptedException ex) {
						System.err.println("Interrupted while waiting for " + pendings + " jobs.");
						ex.printStackTrace();
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
