package donnees;

import java.io.Serializable;

public class Adherent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String strNoAdherent;
	String strNomAdherent;
	String strPrenomAdherent;
	String strAdresseAdherent;
	String strTelephoneAdherent;
	int intNbPretsActifs;
	Double dblSoldeDu;
	
	public Adherent(String strNoAdherent, String strNomAdherent, String strPrenomAdherent, String strAdresseAdherent,
			String strTelephoneAdherent, int intNbPretsActifs, Double dblSoldeDu) {
			
			this.strNoAdherent = strNoAdherent;
			this.strNomAdherent = strNomAdherent;
			this.strPrenomAdherent = strPrenomAdherent;
			this.strAdresseAdherent = strAdresseAdherent;
			this.strTelephoneAdherent = strTelephoneAdherent;
			this.intNbPretsActifs = intNbPretsActifs;
			this.dblSoldeDu = dblSoldeDu;
		}

	public String getStrNoAdherent() {
		return strNoAdherent;
	}

	public String getStrNomAdherent() {
		return strNomAdherent;
	}

	public String getStrPrenomAdherent() {
		return strPrenomAdherent;
	}

	public String getStrAdresseAdherent() {
		return strAdresseAdherent;
	}

	public String getStrTelephoneAdherent() {
		return strTelephoneAdherent;
	}

	public int getIntNbPretsActifs() {
		return intNbPretsActifs;
	}

	public Double getDblSoldeDu() {
		return dblSoldeDu;
	}

	public void setStrAdresseAdherent(String strAdresseAdherent) {
		this.strAdresseAdherent = strAdresseAdherent;
	}

	public void setStrTelephoneAdherent(String strTelephoneAdherent) {
		this.strTelephoneAdherent = strTelephoneAdherent;
	}

	public void setDblSoldeDu(Double dblSoldeDu) {
		this.dblSoldeDu = dblSoldeDu;
	}

}
