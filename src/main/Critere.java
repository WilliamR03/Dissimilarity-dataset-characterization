package main;

import java.io.Serializable;

import weka.classifiers.Evaluation;

public class Critere implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1323947463899416204L;
	private String nom;
	private boolean higherIsBetter;
	private double min;
	private double max;
	private boolean useDef;
	private double def;

	public Critere(String nom, boolean higherIsBetter, double min, double max, boolean useDef, double def) {
		super();
		this.nom = nom;
		this.higherIsBetter = higherIsBetter;
		this.min = min;
		this.max = max;
		this.useDef = useDef;
		this.def = def;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public boolean isHigherIsBetter() {
		return higherIsBetter;
	}

	public void setHigherIsBetter(boolean higherIsBetter) {
		this.higherIsBetter = higherIsBetter;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public boolean isUseDef() {
		return useDef;
	}

	public void setUseDef(boolean useDef) {
		this.useDef = useDef;
	}

	public double getDef() {
		return def;
	}

	public void setDef(double def) {
		this.def = def;
	}

	public boolean test(double value) {
		return (value >= min && value <= max);
	}

	@Override
	public String toString() {
		return "Critere [nom=" + nom + ", higherIsBetter=" + higherIsBetter + ", min=" + min + ", max=" + max + ", useDef=" + useDef + ", def=" + def + "]";
	}

	public double evaluate(Evaluation evaluation) throws Exception {
		switch (this.nom) {
		case "area_under_roc_curve":
			return evaluation.weightedAreaUnderROC();
		case "predictive_accuracy":
			return evaluation.pctCorrect();
		case "precision":
			return evaluation.weightedPrecision();
		case "recall":
			return evaluation.weightedRecall();
		case "kappa":
			return evaluation.kappa();
		case "f_measure":
			return evaluation.weightedFMeasure();
		case "root_mean_squared_error":
			return evaluation.rootMeanSquaredError();
		case "root_relative_squared_error":
			return evaluation.rootRelativeSquaredError();
		case "mean_absolute_error":
			return evaluation.meanAbsoluteError();
		case "relative_absolute_error":
			return evaluation.relativeAbsoluteError();
		case "kb_relative_information_score":
			return evaluation.KBRelativeInformation();
		default:
			throw new Exception("Critere non present dans weka");
		}
	}

}
