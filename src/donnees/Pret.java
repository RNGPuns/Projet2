package donnees;

import java.io.Serializable;
import java.time.LocalDate;

public class Pret implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int noPret;
	LocalDate datePret;
	LocalDate dateRetourPrevuPret;
	LocalDate dateRetourPret;
	Double amende;
	String strNoDocPret;
	
	public Pret(int noPret, LocalDate datePret, LocalDate dateRetourPrevuPret, LocalDate dateRetourPret,
			Double amende, String strNoDocPret) {
		this.noPret = noPret;
		this.datePret = datePret;
		this.dateRetourPrevuPret = dateRetourPrevuPret;
		this.dateRetourPret = dateRetourPret;
		this.amende = amende;
		this.strNoDocPret = strNoDocPret;
	}

	public String getStrNoDocPret() {
		return strNoDocPret;
	}

	public int getNoPret() {
		return noPret;
	}

	public LocalDate getDatePret() {
		return datePret;
	}

	public LocalDate getDateRetourPrevuPret() {
		return dateRetourPrevuPret;
	}

	public LocalDate getDateRetourPret() {
		return dateRetourPret;
	}

	public Double getAmende() {
		return amende;
	}

	public void setDateRetourPret(LocalDate dateRetourPret) {
		this.dateRetourPret = dateRetourPret;
	}

	public void setAmende(Double amende) {
		this.amende = amende;
	}

}
