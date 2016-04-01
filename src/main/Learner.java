package main;

import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.core.OptionHandler;
import weka.core.neighboursearch.FilteredNeighbourSearch;
import weka.core.neighboursearch.NearestNeighbourSearch;
import weka.core.Capabilities.Capability;
import weka.core.DistanceFunction;

public class Learner {

	private String nom;
	private Classifier classifier;

	public Learner(String nom, Classifier classifier) {
		super();
		this.nom = nom;
		this.classifier = classifier;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

	@Override
	public String toString() {
		return "Learner [nom=" + nom + ", classifier=" + classifier + "]";
	}

	public static ArrayList<Learner> initTest() throws Exception {

		ArrayList<Learner> list = new ArrayList<Learner>();
		Learner learner;
		Classifier classifier;
		String nom;
		String[] options;

		classifier = new weka.classifiers.bayes.AODE();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);

		return list;
	}

	public static ArrayList<Learner> init(boolean dissim) throws Exception {

		ArrayList<Learner> list = new ArrayList<Learner>();
		Learner learner;
		Classifier classifier;
		String[] options;
		String nom;

//		if (dissim) {
//
//			String[] kVals = { "1", "3", "5", "10", "20" };
//			String[] wVals = { "", "-I", "-F" };
//
//			for (String w : wVals) {
//				for (String k : kVals) {
//
//					classifier = new IBkWrapper();
//					nom = classifier.getClass().getName();
//					options = new String[3];
//					options[0] = "-K";
//					options[1] = k;
//					options[2] = w;
//
//					NearestNeighbourSearch nnSearch;
//					nnSearch = new NNSearchWrapper();
//
//					DistanceFunction nnDist;
//					nnDist = new Dissimilarity();
//
//					nnSearch.setDistanceFunction(nnDist);
//
//					for (int i = 0; i < options.length; i++) {
//						nom += " " + options[i];
//					}
//					((OptionHandler) classifier).setOptions(options);
//					((weka.classifiers.lazy.IBk) classifier).setNearestNeighbourSearchAlgorithm(nnSearch);
//
//					nom += " " + nnSearch.getClass().getName();
//					options = ((OptionHandler) nnSearch).getOptions();
//					for (int i = 0; i < options.length; i++) {
//						nom += " " + options[i];
//					}
//
//					learner = new Learner(nom, classifier);
//					list.add(learner);
//
//				}
//			}
//
//		}

		// AODE
		classifier = new weka.classifiers.bayes.AODE();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// BayesNet
		classifier = new weka.classifiers.bayes.BayesNet();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.bayes.BayesNet();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-Q";
		options[1] = "weka.classifiers.bayes.net.search.fixed.NaiveBayes";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);

		// classifier = new weka.classifiers.bayes.BayesNet();
		// nom = classifier.getClass().getName();
		// options = new String[2];
		// options[0] = "-Q";
		// options[1] = "weka.classifiers.bayes.net.search.global.RepeatedHillClimber";
		// for (int i = 0; i < options.length; i++) {
		// nom += " " + options[i];
		// }
		// ((OptionHandler) classifier).setOptions(options);
		// learner = new Learner(nom, classifier);
		// list.add(learner);

		classifier = new weka.classifiers.bayes.BayesNet();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-Q";
		options[1] = "weka.classifiers.bayes.net.search.global.TAN";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// NaiveBayes
		classifier = new weka.classifiers.bayes.NaiveBayes();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.bayes.NaiveBayes();
		nom = classifier.getClass().getName();
		options = new String[1];
		options[0] = "-K";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);

		// classifier = new weka.classifiers.bayes.NaiveBayes();
		// nom = classifier.getClass().getName();
		// options = new String[1];
		// options[0] = "-D";
		// for (int i = 0; i < options.length; i++) {
		// nom += " " + options[i];
		// }
		// ((OptionHandler) classifier).setOptions(options);
		// learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// GaussianProcesses
		classifier = new weka.classifiers.functions.GaussianProcesses();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// LinearRegression
		classifier = new weka.classifiers.functions.LinearRegression();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.functions.LinearRegression();
		nom = classifier.getClass().getName();
		options = new String[1];
		options[0] = "-C";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// Logistic
		classifier = new weka.classifiers.functions.Logistic();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		/// MLPClassifier
		classifier = new weka.classifiers.functions.MLPClassifier();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.functions.MLPClassifier();
		nom = classifier.getClass().getName();
		options = new String[1];
		options[0] = "-G";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// MLPRegressor
		classifier = new weka.classifiers.functions.MLPRegressor();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.functions.MLPRegressor();
		nom = classifier.getClass().getName();
		options = new String[1];
		options[0] = "-G";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// MultilayerPerceptron
		classifier = new weka.classifiers.functions.MultilayerPerceptron();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// RBFClassifier
		classifier = new weka.classifiers.functions.RBFClassifier();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// RBFRegressor
		classifier = new weka.classifiers.functions.RBFRegressor();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.functions.RBFRegressor();
		nom = classifier.getClass().getName();
		options = new String[1];
		options[0] = "-G";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.functions.RBFRegressor();
		nom = classifier.getClass().getName();
		options = new String[1];
		options[0] = "-O";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.functions.RBFRegressor();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-C";
		options[1] = "1";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// SimpleLogistic
		classifier = new weka.classifiers.functions.SimpleLogistic();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// SMO
		classifier = new weka.classifiers.functions.SMO();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-K";
		options[1] = "weka.classifiers.functions.supportVector.PolyKernel";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.functions.SMO();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-K";
		options[1] = "weka.classifiers.functions.supportVector.Puk";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.functions.SMO();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-K";
		options[1] = "weka.classifiers.functions.supportVector.RBFKernel";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// SMOreg
		classifier = new weka.classifiers.functions.SMOreg();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-K";
		options[1] = "weka.classifiers.functions.supportVector.PolyKernel";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.functions.SMOreg();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-K";
		options[1] = "weka.classifiers.functions.supportVector.Puk";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.functions.SMOreg();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-K";
		options[1] = "weka.classifiers.functions.supportVector.RBFKernel";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// TODO SPegasos
		classifier = new weka.classifiers.functions.SPegasos();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// TODO VotedPerceptron
		classifier = new weka.classifiers.functions.VotedPerceptron();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// TODO Winnow
		classifier = new weka.classifiers.functions.Winnow();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// TODO ADTree
		classifier = new weka.classifiers.trees.ADTree();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// BFTree
		classifier = new weka.classifiers.trees.BFTree();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// DecisionStump
		classifier = new weka.classifiers.trees.DecisionStump();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// FT
		classifier = new weka.classifiers.trees.FT();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// J48
		classifier = new weka.classifiers.trees.J48();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// J48graft
		classifier = new weka.classifiers.trees.J48graft();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// LADTree
		classifier = new weka.classifiers.trees.LADTree();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// LMT
		classifier = new weka.classifiers.trees.LMT();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// M5P
		classifier = new weka.classifiers.trees.M5P();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// NBTree
		classifier = new weka.classifiers.trees.NBTree();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// RandomForest
		classifier = new weka.classifiers.trees.RandomForest();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// REPTree
		classifier = new weka.classifiers.trees.REPTree();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// SimpleCart
		classifier = new weka.classifiers.trees.SimpleCart();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// IBk
		ArrayList<NearestNeighbourSearch> nnSearchList = new ArrayList<NearestNeighbourSearch>();
		NearestNeighbourSearch nnSearch;
		// nnSearch = new weka.core.neighboursearch.BallTree();
		// options = new String[2];
		// options[0] = "-C";
		// options[1] = "weka.core.neighboursearch.balltrees.TopDownConstructor";
		// ((OptionHandler) nnSearch).setOptions(options);
		// nnSearchList.add(nnSearch);
		// nnSearch = new weka.core.neighboursearch.BallTree();
		// options = new String[2];
		// options[0] = "-C";
		// options[1] = "weka.core.neighboursearch.balltrees.BottomUpConstructor";
		// ((OptionHandler) nnSearch).setOptions(options);
		// nnSearchList.add(nnSearch);
		nnSearch = new weka.core.neighboursearch.CoverTree();
		nnSearchList.add(nnSearch);
		nnSearch = new weka.core.neighboursearch.LinearNNSearch();
		nnSearchList.add(nnSearch);
		nnSearch = new weka.core.neighboursearch.KDTree();
		options = new String[2];
		options[0] = "-S";
		options[1] = "weka.core.neighboursearch.kdtrees.KMeansInpiredMethod";
		((OptionHandler) nnSearch).setOptions(options);
		nnSearchList.add(nnSearch);
		// nnSearch = new weka.core.neighboursearch.KDTree();
		// options = new String[2];
		// options[0] = "-S";
		// options[1] = "weka.core.neighboursearch.kdtrees.MedianOfWidestDimension";
		// ((OptionHandler) nnSearch).setOptions(options);
		// nnSearchList.add(nnSearch);
		// nnSearch = new weka.core.neighboursearch.KDTree();
		// options = new String[2];
		// options[0] = "-S";
		// options[1] = "weka.core.neighboursearch.kdtrees.MidPointOfWidestDimension";
		// ((OptionHandler) nnSearch).setOptions(options);
		// nnSearchList.add(nnSearch);
		nnSearch = new weka.core.neighboursearch.KDTree();
		options = new String[2];
		options[0] = "-S";
		options[1] = "weka.core.neighboursearch.kdtrees.SlidingMidPointOfWidestSide";
		((OptionHandler) nnSearch).setOptions(options);
		nnSearchList.add(nnSearch);

		ArrayList<DistanceFunction> nnDistList = new ArrayList<DistanceFunction>();
		DistanceFunction nnDist;
		nnDist = new weka.core.ChebyshevDistance();
		nnDistList.add(nnDist);
		nnDist = new weka.core.EuclideanDistance();
		nnDistList.add(nnDist);
		nnDist = new weka.core.ManhattanDistance();
		nnDistList.add(nnDist);
		nnDist = new weka.core.MinkowskiDistance();
		options = new String[2];
		options[0] = "-P";
		options[1] = "3";
		((OptionHandler) nnDist).setOptions(options);
		nnDistList.add(nnDist);
		nnDist = new weka.core.MinkowskiDistance();
		options = new String[2];
		options[0] = "-P";
		options[1] = "5";
		((OptionHandler) nnDist).setOptions(options);
		nnDistList.add(nnDist);
		nnDist = new weka.core.MinkowskiDistance();
		options = new String[2];
		options[0] = "-P";
		options[1] = "10";
		((OptionHandler) nnDist).setOptions(options);
		nnDistList.add(nnDist);

		String[] kVals = { "1", "3", "5", "10", "20" };
		String[] wVals = { "", "-I", "-F" };

		for (String w : wVals) {
			for (String k : kVals) {
				for (NearestNeighbourSearch nnSearch1 : nnSearchList) {
					for (DistanceFunction nnDist1 : nnDistList) {
						try {
							classifier = new weka.classifiers.lazy.IBk();
							nom = classifier.getClass().getName();
							options = new String[3];
							options[0] = "-K";
							options[1] = k;
							options[2] = w;

							nnSearch1.setDistanceFunction(nnDist1);
							FilteredNeighbourSearch filterSearch = new FilteredNeighbourSearch();
							filterSearch.setFilter(new weka.filters.unsupervised.attribute.ReplaceMissingValues());
							filterSearch.setSearchMethod(nnSearch1);

							for (int i = 0; i < options.length; i++) {
								nom += " " + options[i];
							}
							((OptionHandler) classifier).setOptions(options);
							((weka.classifiers.lazy.IBk) classifier).setNearestNeighbourSearchAlgorithm(filterSearch);

							nom += " " + nnSearch1.getClass().getName();
							options = ((OptionHandler) nnSearch1).getOptions();
							for (int i = 0; i < options.length; i++) {
								nom += " " + options[i];
							}

							learner = new Learner(nom, classifier);
							list.add(learner);
						} catch (Exception e) {
						}
					}
				}
			}
		}
		// ******************

		// Kstar
		classifier = new weka.classifiers.lazy.KStar();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-M";
		options[1] = "a";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.lazy.KStar();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-M";
		options[1] = "d";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.lazy.KStar();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-M";
		options[1] = "m";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);

		classifier = new weka.classifiers.lazy.KStar();
		nom = classifier.getClass().getName();
		options = new String[2];
		options[0] = "-M";
		options[1] = "n";
		for (int i = 0; i < options.length; i++) {
			nom += " " + options[i];
		}
		((OptionHandler) classifier).setOptions(options);
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// LBR
		classifier = new weka.classifiers.lazy.LBR();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// ConjunctiveRule
		classifier = new weka.classifiers.rules.ConjunctiveRule();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// DecisionTable
		classifier = new weka.classifiers.rules.DecisionTable();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// DTNB
		classifier = new weka.classifiers.rules.DTNB();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// JRip
		classifier = new weka.classifiers.rules.JRip();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);

		// M5Rules
		classifier = new weka.classifiers.rules.M5Rules();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// NNge
		classifier = new weka.classifiers.rules.NNge();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// OneR
		classifier = new weka.classifiers.rules.OneR();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// PART
		classifier = new weka.classifiers.rules.PART();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// Ridor
		classifier = new weka.classifiers.rules.Ridor();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// ZeroR
		classifier = new weka.classifiers.rules.ZeroR();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// HyperPipes
		classifier = new weka.classifiers.misc.HyperPipes();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// VFI
		classifier = new weka.classifiers.misc.VFI();
		nom = classifier.getClass().getName();
		learner = new Learner(nom, classifier);
		list.add(learner);
		// ******************

		// ****************
		// meta classifiers
		// ****************
		@SuppressWarnings("unchecked")
		ArrayList<Learner> base = (ArrayList<Learner>) list.clone();

		for (Learner baseLearner : base) {

			// AdaBoostM1
			if (baseLearner.getClassifier().getCapabilities().handles(Capability.NOMINAL_CLASS)) {
				classifier = new weka.classifiers.meta.AdaBoostM1();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);
			}
			// ******************

			// AdditiveRegression
			if (baseLearner.getClassifier().getCapabilities().handles(Capability.NUMERIC_CLASS)) {
				classifier = new weka.classifiers.meta.AdditiveRegression();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);
			}
			// ******************

			// Bagging
			classifier = new weka.classifiers.meta.Bagging();
			nom = classifier.getClass().getName();
			((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
			options = ((OptionHandler) classifier).getOptions();
			for (int i = 0; i < options.length; i++) {
				nom += " " + options[i];
			}
			learner = new Learner(nom, classifier);
			list.add(learner);
			// ******************

			// ClassificationViaRegression
			if (baseLearner.getClassifier().getCapabilities().handles(Capability.NUMERIC_CLASS)) {
				classifier = new weka.classifiers.meta.ClassificationViaRegression();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);
			}
			// ******************

			// Dagging
			// if (baseLearner.getClassifier().getCapabilities().handles(Capability.NOMINAL_CLASS)) {
			// classifier = new weka.classifiers.meta.Dagging();
			// nom = classifier.getClass().getName();
			// ((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
			// options = ((OptionHandler) classifier).getOptions();
			// for (int i = 0; i < options.length; i++) {
			// nom += " " + options[i];
			// }
			// learner = new Learner(nom, classifier);
			// list.add(learner);
			// }
			// ******************

			// Decorate
			if (baseLearner.getClassifier().getCapabilities().handles(Capability.NOMINAL_CLASS)) {
				classifier = new weka.classifiers.meta.Decorate();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);
			}
			// ******************

			// ND
			if (baseLearner.getClassifier().getCapabilities().handles(Capability.BINARY_CLASS)) {
				classifier = new weka.classifiers.meta.nestedDichotomies.ND();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);

				// ND-END
				classifier = new weka.classifiers.meta.END();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(learner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);
			}
			// ******************

			// ClassBalancedND
			if (baseLearner.getClassifier().getCapabilities().handles(Capability.BINARY_CLASS)) {
				classifier = new weka.classifiers.meta.nestedDichotomies.ClassBalancedND();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				// list.add(learner);

				// ClassBalancedND-END
				classifier = new weka.classifiers.meta.END();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(learner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);
			}
			// ******************

			// DataNearBalancedND
			if (baseLearner.getClassifier().getCapabilities().handles(Capability.BINARY_CLASS)) {
				classifier = new weka.classifiers.meta.nestedDichotomies.DataNearBalancedND();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				// list.add(learner);

				// DataNearBalancedND-END
				classifier = new weka.classifiers.meta.END();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(learner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);
			}
			// ******************

			// LogitBoost
			if (baseLearner.getClassifier().getCapabilities().handles(Capability.NUMERIC_CLASS)) {
				classifier = new weka.classifiers.meta.LogitBoost();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);
			}
			// ******************

			// MultiBoostAB
			if (baseLearner.getClassifier().getCapabilities().handles(Capability.NOMINAL_CLASS)) {
				classifier = new weka.classifiers.meta.MultiBoostAB();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);
			}
			// ******************

			// MultiClassClassifier
			// if (baseLearner.getClassifier().getCapabilities().handles(Capability.BINARY_CLASS)) {
			// classifier = new weka.classifiers.meta.MultiClassClassifier();
			// nom = classifier.getClass().getName();
			// ((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
			// options = ((OptionHandler) classifier).getOptions();
			// for (int i = 0; i < options.length; i++) {
			// nom += " " + options[i];
			// }
			// learner = new Learner(nom, classifier);
			// list.add(learner);
			// }
			// ******************

			// RandomCommittee
			// if (baseLearner.getClassifier().getCapabilities().handles(Capability.NOMINAL_CLASS)
			// && weka.core.Randomizable.class.isAssignableFrom(baseLearner.getClassifier().getClass())) {
			// classifier = new weka.classifiers.meta.RandomCommittee();
			// nom = classifier.getClass().getName();
			// ((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
			// options = ((OptionHandler) classifier).getOptions();
			// for (int i = 0; i < options.length; i++) {
			// nom += " " + options[i];
			// }
			// learner = new Learner(nom, classifier);
			// list.add(learner);
			// }
			// ******************

			// RandomSubSpace
			classifier = new weka.classifiers.meta.RandomSubSpace();
			nom = classifier.getClass().getName();
			((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
			options = ((OptionHandler) classifier).getOptions();
			for (int i = 0; i < options.length; i++) {
				nom += " " + options[i];
			}
			learner = new Learner(nom, classifier);
			list.add(learner);
			// ******************

			// RegressionByDiscretization
			if (baseLearner.getClassifier().getCapabilities().handles(Capability.NOMINAL_CLASS)) {
				classifier = new weka.classifiers.meta.RegressionByDiscretization();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);
			}
			// ******************

			// RotationForest
			if (baseLearner.getClassifier().getCapabilities().handles(Capability.NOMINAL_CLASS)) {
				classifier = new weka.classifiers.meta.RotationForest();
				nom = classifier.getClass().getName();
				((weka.classifiers.SingleClassifierEnhancer) classifier).setClassifier(baseLearner.getClassifier());
				options = ((OptionHandler) classifier).getOptions();
				for (int i = 0; i < options.length; i++) {
					nom += " " + options[i];
				}
				learner = new Learner(nom, classifier);
				list.add(learner);
			}
		}

		return list;
	}

}
