package donnees;

import java.io.Serializable;
import java.util.ArrayList;

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
	double dblSoldeDu;
	ArrayList<Pret> lstPrets;
	ArrayList<Document> lstDoc;
	
	public Adherent(String strNoAdherent, String strNomAdherent, String strPrenomAdherent, String strAdresseAdherent,
			String strTelephoneAdherent, int intNbPretsActifs, double dblSoldeDu ,ArrayList<Pret> lstPrets, ArrayList<Document> lstDoc) {
			
			this.strNoAdherent = strNoAdherent;
			this.strNomAdherent = strNomAdherent;
			this.strPrenomAdherent = strPrenomAdherent;
			this.strAdresseAdherent = strAdresseAdherent;
			this.strTelephoneAdherent = strTelephoneAdherent;
			this.intNbPretsActifs = intNbPretsActifs;
			this.dblSoldeDu = dblSoldeDu;
			this.lstPrets = lstPrets;
			this.lstDoc = lstDoc;
		}
	
	public ArrayList<Document> getLstDoc() {
		return lstDoc;
	}
	public ArrayList<Pret> getLstPrets() {
		return lstPrets;
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

	public double getDblSoldeDu() {
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

	public void setLstPrets(ArrayList<Pret> lstPrets) {
		this.lstPrets = lstPrets;
	}
	


}
