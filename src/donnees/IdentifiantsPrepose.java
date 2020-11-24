package donnees;

public class IdentifiantsPrepose {
	String strType;
	String strIdentifiant;
	String strMotDePasse;
	
	public IdentifiantsPrepose(String strType, String strIdentifiant, String strMotDePasse) {
		super();
		this.strType = strType;
		this.strIdentifiant = strIdentifiant;
		this.strMotDePasse = strMotDePasse;
	}

	public String getStrType() {
		return strType;
	}

	public void setStrType(String strType) {
		this.strType = strType;
	}

	public String getStrIdentifiant() {
		return strIdentifiant;
	}

	public void setStrIdentifiant(String strIdentifiant) {
		this.strIdentifiant = strIdentifiant;
	}

	public String getStrMotDePasse() {
		return strMotDePasse;
	}

	public void setStrMotDePasse(String strMotDePasse) {
		this.strMotDePasse = strMotDePasse;
	}

}