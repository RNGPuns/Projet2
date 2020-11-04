package donnees;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import application.InterfaceGraphique;
import javafx.application.Application;
import javafx.stage.Stage;

public final class Catalogue implements Serializable {

	// Complétez pour programmer la classe comme singleton
	// Le constructeur permet de remplir les listes suivantes à partir des fichiers
	// textes Livres.txt et periodiques.txt et DVD.txt

	private static final long serialVersionUID = 1L;
	private static Catalogue instance;
	private ArrayList<Document> lstDocuments = new ArrayList<>();
	private ArrayList<Livre> lstLivres = new ArrayList<>();
	private ArrayList<Periodique> lstPeriodiques = new ArrayList<>();
	private ArrayList<DVD> lstDvd = new ArrayList<>();
	DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	
	//Private pour singleton
	private Catalogue (String strNomFichier1, String strNomFichier2, String strNomFichier3) {
		File serLivres = new File("livres.ser");
		File serDVD = new File("DVD.ser");
		File serPeriodique = new File("periodiques.ser");
		if (!(serLivres.exists() && serDVD.exists() && serPeriodique.exists())) {
			try {
				serLivres.createNewFile();
				serDVD.createNewFile();
				serPeriodique.createNewFile();
				
				//Temporaire
				serLivres.delete(); 
				serDVD.delete();
				serPeriodique.delete();
				
				try {
					Scanner scFichier1 = new Scanner(new File(strNomFichier1)); //Début de la lecture du fichier
					String strLigne1 = "";
					
					while (scFichier1.hasNextLine()) { //Lecture du fichier
						strLigne1 = scFichier1.nextLine();
						Periodique periodique = new Periodique(strLigne1.split(",")[0], strLigne1.split(",")[1], LocalDate.parse(strLigne1.split(",")[2].replaceAll("\\s+",""),df), "oui ", Integer.parseInt(strLigne1.split(",")[3].replaceAll("\\s+","")), Integer.parseInt(strLigne1.split(",")[4].replaceAll("\\s+","")));
						lstPeriodiques.add(periodique);
						lstDocuments.add(periodique);
					}
					
					scFichier1.close(); //Fin de la lecture du fichier
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				}
				
				try {
					Scanner scFichier2 = new Scanner(new File(strNomFichier2)); //Début de la lecture du fichier
					String strLigne2 = "";
					
					while (scFichier2.hasNextLine()) { //Lecture du fichier
						strLigne2 = scFichier2.nextLine();
						Livre livre = new Livre(strLigne2.split(",")[0], strLigne2.split(",")[1], LocalDate.parse(strLigne2.split(",")[2].replaceAll("\\s+",""),df), "oui", null, strLigne2.split(",")[3]);
						lstLivres.add(livre);
						lstDocuments.add(livre);
					}
					
					scFichier2.close(); //Fin de la lecture du fichier
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				}
				
				
				try {
					Scanner scFichier3 = new Scanner(new File(strNomFichier3)); //Début de la lecture du fichier
					String strLigne3 = "";
					
					while (scFichier3.hasNextLine()) { //Lecture du fichier
						strLigne3 = scFichier3.nextLine();
						DVD dvd = new DVD(strLigne3.split(",")[0], strLigne3.split(",")[1], LocalDate.parse(strLigne3.split(",")[2].replaceAll("\\s+",""),df), "oui", Integer.parseInt(strLigne3.split(",")[3]), strLigne3.split(",")[4]);
						lstDvd.add(dvd);
						lstDocuments.add(dvd);
					}
					
					scFichier3.close(); //Fin de la lecture du fichier
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	//Singleton
	public static Catalogue getInstance() {
		
		if (instance == null) {
			instance = new Catalogue("Periodiques.txt", "Livres.txt", "DVD.txt");
		}
		
		return instance;
	}
	
	
}
