package donnees;

import java.time.LocalDate;

public class Periodique extends Document {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int noVolume;
	private int noPeriodique;
	

	public Periodique(String noDoc, String titre, LocalDate dateParution, String disponible, int noVolume, int noPeriodique, int intNbPrets, String strEmprunteur,String motCles) {
		super(noDoc, titre, dateParution, disponible, intNbPrets, strEmprunteur,motCles);
		this.noVolume = noVolume;
		this.noPeriodique = noPeriodique;
	}

	@Override
	public String toString() {
		return "Periodique [" + super.toString() + "noVolume=" + noVolume + ", noPeriodique=" + noPeriodique + "]";
	}

	public int getNoVolume() {
		return noVolume;
	}

	public int getNoPeriodique() {
		return noPeriodique;
	}

}
