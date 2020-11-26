package donnees;
import java.io.Serializable;
import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Document implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String noDoc;
	private String titre;
	private LocalDate dateParution;
	private String disponible;
	private int intNbPrets = 0;
	private String strEmprunteur = "";

	public Document(String noDoc, String titre, LocalDate dateParution, String disponible, int intNbPrets, String strEmprunteur) {
		super();
		this.noDoc = noDoc;
		this.titre = titre;
		this.dateParution = dateParution;
		this.disponible = disponible;
		this.intNbPrets = intNbPrets;
		this.strEmprunteur = strEmprunteur;
	}

	@Override
	public String toString() {
		return "noDoc=" + noDoc + ", titre=" + titre + ", dateParution=" + dateParution + ", disponible=" + disponible;
	}
	

	public String getNoDoc() {
		return noDoc;
	}

	public String getTitre() {
		return titre;
	}

	public LocalDate getDateParution() {
		return dateParution;
	}

	public String getDisponible() {
		return disponible;
	}

	public int getIntNbPrets() {
		return intNbPrets;
	}

	public String getStrEmprunteur() {
		return strEmprunteur;
	}

}