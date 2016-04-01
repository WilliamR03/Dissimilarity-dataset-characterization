package main;
import java.util.ArrayList;

import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.core.OptionHandler;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class FeatureSelector {

	private String nomSearch;
	private String nomEval;
	private Filter filter;

	public FeatureSelector(String nomSearch, String nomEval, Filter filter) {
		super();
		this.nomSearch = nomSearch;
		this.nomEval = nomEval;
		this.filter = filter;
	}

	public String getNomSearch() {
		return nomSearch;
	}

	public void setNomSearch(String nomSearch) {
		this.nomSearch = nomSearch;
	}

	public String getNomEval() {
		return nomEval;
	}

	public void setNomEval(String nomEval) {
		this.nomEval = nomEval;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	@Override
	public String toString() {
		return "FeatureSelector [nomSearch=" + nomSearch + ", nomEval=" + nomEval + ", filter=" + filter + "]";
	}

	public static ArrayList<FeatureSelector> initTest() throws Exception {

		String[] options;
		ArrayList<FeatureSelector> list = new ArrayList<FeatureSelector>();
		FeatureSelector fSel;
		AttributeSelection filter;
		ASEvaluation eval;
		ASSearch search;
		String nomEval;
		String nomSearch;

		filter = new AttributeSelection();
		
		search = new weka.attributeSelection.Ranker();
		options = new String[2];
		options[0] = "-N";
		options[1] = "20";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -N 20";

		eval = new weka.attributeSelection.ReliefFAttributeEval();
		options = new String[2];
		options[0] = "-K";
		options[1] = "40";
		((OptionHandler) eval).setOptions(options);
		nomEval = eval.getClass().getName() + " -K 40";

		filter.setEvaluator(eval);
		filter.setSearch(search);
		
		fSel = new FeatureSelector(nomSearch, nomEval, filter);
		list.add(fSel);
	
		return list;
	}

	public static ArrayList<FeatureSelector> init() throws Exception {

		String[] options;

		// **********************************
		// liste des methodes de recherche pour subset
		// **********************************
		ArrayList<ASSearch> searchList = new ArrayList<ASSearch>();
		ArrayList<String> searchNames = new ArrayList<String>();
		ASSearch search;
		String nomSearch;

		// BestFirst
		search = new weka.attributeSelection.BestFirst();
		options = new String[2];
		options[0] = "-D";
		options[1] = "0";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -D 0";
		searchList.add(search);
		searchNames.add(nomSearch);

		search = new weka.attributeSelection.BestFirst();
		options = new String[2];
		options[0] = "-D";
		options[1] = "1";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -D 1";
		searchList.add(search);
		searchNames.add(nomSearch);
		//******************

		// GeneticSearch
		search = new weka.attributeSelection.GeneticSearch();
		nomSearch = search.getClass().getName();
		searchList.add(search);
		searchNames.add(nomSearch);

		search = new weka.attributeSelection.GeneticSearch();
		options = new String[8];
		options[0] = "-Z";
		options[1] = "20";
		options[2] = "-G";
		options[3] = "20";
		options[4] = "-C";
		options[5] = "0.6";
		options[6] = "-M";
		options[7] = "0.1";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -Z 100 -G 100 -C 0.4 -M 0.1";
		searchList.add(search);
		searchNames.add(nomSearch);

		search = new weka.attributeSelection.GeneticSearch();
		options = new String[8];
		options[0] = "-Z";
		options[1] = "200";
		options[2] = "-G";
		options[3] = "200";
		options[4] = "-C";
		options[5] = "0.6";
		options[6] = "-M";
		options[7] = "0.1";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -Z 200 -G 200 -C 0.6 -M 0.1";
		searchList.add(search);
		searchNames.add(nomSearch);
		//******************

		// GreedyStepwise
		search = new weka.attributeSelection.GreedyStepwise();
		nomSearch = search.getClass().getName();
		searchList.add(search);
		searchNames.add(nomSearch);

		search = new weka.attributeSelection.GreedyStepwise();
		options = new String[1];
		options[0] = "-B";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -B";
		searchList.add(search);
		searchNames.add(nomSearch);
		//******************

		// LinearForwardSelection
		search = new weka.attributeSelection.LinearForwardSelection();
		nomSearch = search.getClass().getName();
		searchList.add(search);
		searchNames.add(nomSearch);

//		search = new weka.attributeSelection.LinearForwardSelection();
//		options = new String[5];
//		options[0] = "-D";
//		options[1] = "0";
//		options[2] = "-T";
//		options[3] = "0";
//		options[4] = "-I";
//		((OptionHandler) search).setOptions(options);
//		nomSearch = search.getClass().getName() + " -D 0 -T 0 -I";
//		searchList.add(search);
//		searchNames.add(nomSearch);
//		searchList.add(search);
//		searchNames.add(nomSearch);
//
//		search = new weka.attributeSelection.LinearForwardSelection();
//		options = new String[5];
//		options[0] = "-D";
//		options[1] = "1";
//		options[2] = "-T";
//		options[3] = "0";
//		options[4] = "-I";
//		((OptionHandler) search).setOptions(options);
//		nomSearch = search.getClass().getName() + " -D 1 -T 0 -I";
//		searchList.add(search);
//		searchNames.add(nomSearch);

		search = new weka.attributeSelection.LinearForwardSelection();
		options = new String[5];
		options[0] = "-D";
		options[1] = "1";
		options[2] = "-T";
		options[3] = "1";
		options[4] = "-I";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -D 1 -T 1 -I";
		searchList.add(search);
		searchNames.add(nomSearch);

//		search = new weka.attributeSelection.LinearForwardSelection();
//		options = new String[5];
//		options[0] = "-D";
//		options[1] = "0";
//		options[2] = "-T";
//		options[3] = "1";
//		options[4] = "-I";
//		((OptionHandler) search).setOptions(options);
//		nomSearch = search.getClass().getName() + " -D 0 -T 1 -I";
//		searchList.add(search);
//		searchNames.add(nomSearch);
//
//		search = new weka.attributeSelection.LinearForwardSelection();
//		options = new String[4];
//		options[0] = "-D";
//		options[1] = "0";
//		options[2] = "-T";
//		options[3] = "0";
//		((OptionHandler) search).setOptions(options);
//		nomSearch = search.getClass().getName() + " -D 0 -T 0";
//		searchList.add(search);
//		searchNames.add(nomSearch);
//
//		search = new weka.attributeSelection.LinearForwardSelection();
//		options = new String[4];
//		options[0] = "-D";
//		options[1] = "1";
//		options[2] = "-T";
//		options[3] = "0";
//		((OptionHandler) search).setOptions(options);
//		nomSearch = search.getClass().getName() + " -D 1 -T 0";
//		searchList.add(search);
//		searchNames.add(nomSearch);
//
//		search = new weka.attributeSelection.LinearForwardSelection();
//		options = new String[4];
//		options[0] = "-D";
//		options[1] = "0";
//		options[2] = "-T";
//		options[3] = "1";
//		((OptionHandler) search).setOptions(options);
//		nomSearch = search.getClass().getName() + " -D 0 -T 1";
//		searchList.add(search);
//		searchNames.add(nomSearch);

		search = new weka.attributeSelection.LinearForwardSelection();
		options = new String[4];
		options[0] = "-D";
		options[1] = "1";
		options[2] = "-T";
		options[3] = "1";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -D 1 -T 1";
		searchList.add(search);
		searchNames.add(nomSearch);
		//******************
		
		// ScatterSearchV1
		search = new weka.attributeSelection.ScatterSearchV1();
		options = new String[2];
		options[0] = "-R";
		options[1] = "0";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -R 0";
		searchList.add(search);
		searchNames.add(nomSearch);

		search = new weka.attributeSelection.ScatterSearchV1();
		options = new String[2];
		options[0] = "-R";
		options[1] = "1";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -R 1";
		searchList.add(search);
		searchNames.add(nomSearch);
		//******************

		// SubsetSizeForwardSelection
		search = new weka.attributeSelection.SubsetSizeForwardSelection();
		nomSearch = search.getClass().getName();
		searchList.add(search);
		searchNames.add(nomSearch);
		
//		search = new weka.attributeSelection.SubsetSizeForwardSelection();
//		options = new String[1];
//		options[0] = "-I";
//		((OptionHandler) search).setOptions(options);
//		nomSearch = search.getClass().getName() + " -I";
//		searchList.add(search);
//		searchNames.add(nomSearch);
//
//		search = new weka.attributeSelection.SubsetSizeForwardSelection();
//		options = new String[2];
//		options[0] = "-T";
//		options[1] = "1";
//		((OptionHandler) search).setOptions(options);
//		nomSearch = search.getClass().getName() + " -T 1";
//		searchList.add(search);
//		searchNames.add(nomSearch);

		search = new weka.attributeSelection.SubsetSizeForwardSelection();
		options = new String[3];
		options[0] = "-T";
		options[1] = "1";
		options[2] = "-I";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -T 1 -I";
		searchList.add(search);
		searchNames.add(nomSearch);
		//******************
		
		// TabuSearch
//		search = new weka.attributeSelection.TabuSearch();
//		nomSearch = search.getClass().getName();
//		searchList.add(search);
//		searchNames.add(nomSearch);
		//******************

		// **********************************
		// liste des evaluateurs de subsets
		// **********************************
		ArrayList<ASEvaluation> evalList = new ArrayList<ASEvaluation>();
		ArrayList<String> evalNames = new ArrayList<String>();
		ASEvaluation eval;
		String nomEval;
		
		// CfsSubsetEval
		eval = new weka.attributeSelection.CfsSubsetEval();
		nomEval = eval.getClass().getName();
		evalList.add(eval);
		evalNames.add(nomEval);

		eval = new weka.attributeSelection.CfsSubsetEval();
		options = new String[2];
		options[0] = "-M";
		options[1] = "-L";
		((OptionHandler) eval).setOptions(options);
		nomEval = eval.getClass().getName() + " -M -L";
		evalList.add(eval);
		evalNames.add(nomEval);
		//******************

		// ConsistencySubsetEval
		eval = new weka.attributeSelection.ConsistencySubsetEval();
		nomEval = eval.getClass().getName();
		evalList.add(eval);
		evalNames.add(nomEval);
		//******************
		
		// **********************************
		// assemblage en methodes de feature selection
		// **********************************
		ArrayList<FeatureSelector> list = new ArrayList<FeatureSelector>();
		FeatureSelector fSel;
		AttributeSelection filter;

		for (int i = 0; i < evalList.size(); i++) {
			for (int j = 0; j < searchList.size(); j++) {
				filter = new AttributeSelection();
				filter.setEvaluator(evalList.get(i));
				filter.setSearch(searchList.get(j));
				fSel = new FeatureSelector(searchNames.get(j), evalNames.get(i), filter);
				list.add(fSel);
			}
		}

		// **********************************
		// liste des methodes de recherche single attribute (ranking)
		// **********************************
		searchList = new ArrayList<ASSearch>();
		searchNames = new ArrayList<String>();
		
//		search = new weka.attributeSelection.Ranker();
//		options = new String[2];
//		options[0] = "-N";
//		options[1] = "2";
//		((OptionHandler) search).setOptions(options);
//		nomSearch = search.getClass().getName() + " -N 2";
//		searchList.add(search);
//		searchNames.add(nomSearch);

		search = new weka.attributeSelection.Ranker();
		options = new String[2];
		options[0] = "-N";
		options[1] = "5";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -N 5";
		searchList.add(search);
		searchNames.add(nomSearch);
		
		search = new weka.attributeSelection.Ranker();
		options = new String[2];
		options[0] = "-N";
		options[1] = "10";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -N 10";
		searchList.add(search);
		searchNames.add(nomSearch);
		
		search = new weka.attributeSelection.Ranker();
		options = new String[2];
		options[0] = "-N";
		options[1] = "20";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -N 20";
		searchList.add(search);
		searchNames.add(nomSearch);
		
		search = new weka.attributeSelection.Ranker();
		options = new String[2];
		options[0] = "-N";
		options[1] = "50";
		((OptionHandler) search).setOptions(options);
		nomSearch = search.getClass().getName() + " -N 50";
		searchList.add(search);
		searchNames.add(nomSearch);

		// **********************************
		// liste des evaluateurs de feature
		// **********************************
		evalList = new ArrayList<ASEvaluation>();
		evalNames = new ArrayList<String>();

		// ChiSquaredAttributeEval
		eval = new weka.attributeSelection.ChiSquaredAttributeEval();
		nomEval = eval.getClass().getName();
		evalList.add(eval);
		evalNames.add(nomEval);
		//******************

		// GainRatioAttributeEval
		eval = new weka.attributeSelection.GainRatioAttributeEval();
		nomEval = eval.getClass().getName();
		evalList.add(eval);
		evalNames.add(nomEval);
		//******************

		// InfoGainAttributeEval
		eval = new weka.attributeSelection.InfoGainAttributeEval();
		nomEval = eval.getClass().getName();
		evalList.add(eval);
		evalNames.add(nomEval);
		//******************

		// ReliefFAttributeEval
		eval = new weka.attributeSelection.ReliefFAttributeEval();
		nomEval = eval.getClass().getName();
		evalList.add(eval);
		evalNames.add(nomEval);

//		eval = new weka.attributeSelection.ReliefFAttributeEval();
//		options = new String[3];
//		options[0] = "-W";
//		options[1] = "-A";
//		options[2] = "2";
//		((OptionHandler) eval).setOptions(options);
//		nomEval = eval.getClass().getName() + " -W -A 2";
//		evalList.add(eval);
//		evalNames.add(nomEval);
//
//		eval = new weka.attributeSelection.ReliefFAttributeEval();
//		options = new String[3];
//		options[0] = "-W";
//		options[1] = "-A";
//		options[2] = "1";
//		((OptionHandler) eval).setOptions(options);
//		nomEval = eval.getClass().getName() + " -W -A 1";
//		evalList.add(eval);
//		evalNames.add(nomEval);

		eval = new weka.attributeSelection.ReliefFAttributeEval();
		options = new String[5];
		options[0] = "-W";
		options[1] = "-A";
		options[2] = "4";
		options[3] = "-K";
		options[4] = "40";
		((OptionHandler) eval).setOptions(options);
		nomEval = eval.getClass().getName() + " -W -A 4 -K 40";
		evalList.add(eval);
		evalNames.add(nomEval);

		eval = new weka.attributeSelection.ReliefFAttributeEval();
		options = new String[5];
		options[0] = "-W";
		options[1] = "-A";
		options[2] = "8";
		options[3] = "-K";
		options[4] = "40";
		((OptionHandler) eval).setOptions(options);
		nomEval = eval.getClass().getName() + " -W -A 8 -K 40";
		evalList.add(eval);
		evalNames.add(nomEval);

		eval = new weka.attributeSelection.ReliefFAttributeEval();
		options = new String[2];
		options[0] = "-K";
		options[1] = "40";
		((OptionHandler) eval).setOptions(options);
		nomEval = eval.getClass().getName() + " -K 40";
		evalList.add(eval);
		evalNames.add(nomEval);
		//******************

		// SVMAttributeEval
//		eval = new weka.attributeSelection.SVMAttributeEval();
//		nomEval = eval.getClass().getName();
//		evalList.add(eval);
//		evalNames.add(nomEval);
//
//		eval = new weka.attributeSelection.SVMAttributeEval();
//		options = new String[2];
//		options[0] = "-N";
//		options[1] = "1";
//		((OptionHandler) eval).setOptions(options);
//		nomEval = eval.getClass().getName() + " -N 1";
//		evalList.add(eval);
//		evalNames.add(nomEval);
		//******************

		// SymmetricalUncertAttributeEval
		eval = new weka.attributeSelection.SymmetricalUncertAttributeEval();
		nomEval = eval.getClass().getName();
		evalList.add(eval);
		evalNames.add(nomEval);
		//******************

		// **********************************
		// assemblage en methodes de feature selection et ajout
		// **********************************
		for (int i = 0; i < evalList.size(); i++) {
			for (int j = 0; j < searchList.size(); j++) {
				filter = new AttributeSelection();
				filter.setEvaluator(evalList.get(i));
				filter.setSearch(searchList.get(j));
				fSel = new FeatureSelector(searchNames.get(j), evalNames.get(i), filter);
				list.add(fSel);
			}
		}

		//full set
		Filter filter2 = new weka.filters.unsupervised.attribute.RemoveType();
		options = new String[2];
		options[0] = "-T";
		options[1] = "string";
		((OptionHandler) filter2).setOptions(options);
		fSel = new FeatureSelector("null", "fullSet", filter2);
		list.add(fSel);

		return list;
	}

}
