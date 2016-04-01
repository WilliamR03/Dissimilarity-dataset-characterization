package main;
import java.io.Serializable;

public class Dmf implements Serializable {

	private static final long serialVersionUID = -866648733823425960L;
	private String nom;
	private String type;
	
	public Dmf(String nom, String type) {
		super();
		this.nom = nom;
		this.type = type;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Dmf [nom=" + nom + ", type=" + type + "]";
	}

}
