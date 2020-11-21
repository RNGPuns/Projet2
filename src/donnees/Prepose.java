package donnees;

import java.io.Serializable;

public class Prepose implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String strNoEmploye;
	String strNom;
	String strPrenom;
	String strAdresse;
	String strTelephone;
	
	public Prepose(String strNoEmploye, String strNom, String strPrenom, String strAdresse, String strTelephone) {
		super();
		this.strNoEmploye = strNoEmploye;
		this.strNom = strNom;
		this.strPrenom = strPrenom;
		this.strAdresse = strAdresse;
		this.strTelephone = strTelephone;
	}


	public String getStrNoEmploye() {
		return strNoEmploye;
	}


	public String getStrNom() {
		return strNom;
	}


	public String getStrPrenom() {
		return strPrenom;
	}


	public String getStrAdresse() {
		return strAdresse;
	}


	public String getStrTelephone() {
		return strTelephone;
	}


	public void setStrNoEmploye(String strNoEmploye) {
		this.strNoEmploye = strNoEmploye;
	}


	public void setStrNom(String strNom) {
		this.strNom = strNom;
	}


	public void setStrPrenom(String strPrenom) {
		this.strPrenom = strPrenom;
	}


	public void setStrAdresse(String strAdresse) {
		this.strAdresse = strAdresse;
	}
	
	public void setStrTelephone(String strTelephone) {
		this.strTelephone = strTelephone;
	}

}
