package donnees;

import java.time.LocalDate;

public class DVD extends Document {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int nbDisques;
	private String strRealisateur;

	public DVD(String noDoc, String titre, LocalDate dateParution, String disponible, int nbDisques, String strRealisateur, 
			int intNbPrets, String strEmprunteur, String motsCles) {

		super(noDoc, titre, dateParution, disponible, intNbPrets, strEmprunteur,motsCles);
		this.nbDisques = nbDisques;
		this.strRealisateur = strRealisateur;
	}

	public String toString() {
		return " DVD :" + super.toString() + "nbDisques: " + this.nbDisques + " Réalisateur: " + this.strRealisateur;
	}

	public int getNbDisques() {
		return nbDisques;
	}

	public String getStrRealisateur() {
		return strRealisateur;
	}
}
