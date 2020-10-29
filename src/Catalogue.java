import java.io.Serializable;
import java.util.ArrayList;

public final class Catalogue implements Serializable {
	
	
  //  Complétez  pour programmer las classe comme singleton 
  //  Le constructeur permet de remplir les listes suivantes à partir des fichiers textes Livres.txt et periodiques.txt et DVD.txt
	
	private static final long serialVersionUID = 1L;
	private static Catalogue instance;
	private ArrayList<Document> lstDocuments = new ArrayList<>();
	private ArrayList<Livre> lstLivres = new ArrayList<>();
	private ArrayList<Periodique> lstPeriodiques = new ArrayList<>();
	private ArrayList<DVD> lstDvd = new ArrayList<>();

}
