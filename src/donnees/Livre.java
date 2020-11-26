package donnees;

import java.io.Serializable;
import java.time.LocalDate;

public class Livre extends Document {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String auteur;

	public Livre(String noDoc, String titre, LocalDate dateParution, String disponible, String motsCles, String auteur, int intNbPrets, String strEmprunteur) {

		super(noDoc, titre, dateParution, disponible,intNbPrets, strEmprunteur);
		this.auteur = auteur;
	}

	@Override
	public String toString() {
		return "Livre [" + super.toString() + "auteur=" + auteur + "]";
	}

	public String getAuteur() {
		return auteur;
	}

}
