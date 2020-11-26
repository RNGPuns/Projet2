package donnees;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import application.InterfaceGraphique;

public class DeserialisationCatalogue {
	int intNbLivres = 1;
	int intNbDVD = 1;
	int intNbPeriodiques = 1;

	public void DeserialiserDocuments() {
		 ArrayList<Document> doc = new ArrayList<Document>();
       
       try
       {
           FileInputStream fis = new FileInputStream("Donn�es s�rialis�es/Documents.ser");
           ObjectInputStream ois = new ObjectInputStream(fis);

           doc = (ArrayList) ois.readObject();

           ois.close();
           fis.close();
       } 
       catch (IOException ioe) 
       {
           ioe.printStackTrace();
           return;
       } 
       catch (ClassNotFoundException c) 
       {
           System.out.println("Class not found");
           c.printStackTrace();
           return;
       }
       
       for (Document document : doc) {
           Catalogue.getLstDocuments().add(document);
       }

	}
	
	public void DeserialiserLivres() {
		 ArrayList<Livre> livres = new ArrayList<Livre>();
      
      try
      {
          FileInputStream fis = new FileInputStream("Donn�es s�rialis�es/Livres.ser");
          ObjectInputStream ois = new ObjectInputStream(fis);

          livres = (ArrayList) ois.readObject();

          ois.close();
          fis.close();
      } 
      catch (IOException ioe) 
      {
          ioe.printStackTrace();
          return;
      } 
      catch (ClassNotFoundException c) 
      {
          System.out.println("Class not found");
          c.printStackTrace();
          return;
      }
      
      for (Livre livre : livres) {
    	  intNbLivres++;
          Catalogue.getLstLivres().add(livre);
          InterfaceGraphique.setIntNbLivre(intNbLivres);
      }

	}
	
	public void DeserialiserDVD() {
		 ArrayList<DVD> dvd = new ArrayList<DVD>();
     
     try
     {
         FileInputStream fis = new FileInputStream("Donn�es s�rialis�es/DVD.ser");
         ObjectInputStream ois = new ObjectInputStream(fis);

         dvd= (ArrayList) ois.readObject();

         ois.close();
         fis.close();
     } 
     catch (IOException ioe) 
     {
         ioe.printStackTrace();
         return;
     } 
     catch (ClassNotFoundException c) 
     {
         System.out.println("Class not found");
         c.printStackTrace();
         return;
     }
     
     for (DVD DVD : dvd) {
    	 intNbDVD++;
         Catalogue.getLstDvd().add(DVD);
         InterfaceGraphique.setIntNbDVD(intNbDVD);
     }

	}
	
	public void DeserialiserPeriodiques() {
		 ArrayList<Periodique> periodiques = new ArrayList<Periodique>();
     
     try
     {
         FileInputStream fis = new FileInputStream("Donn�es s�rialis�es/Periodiques.ser");
         ObjectInputStream ois = new ObjectInputStream(fis);

         periodiques= (ArrayList) ois.readObject();

         ois.close();
         fis.close();
     } 
     catch (IOException ioe) 
     {
         ioe.printStackTrace();
         return;
     } 
     catch (ClassNotFoundException c) 
     {
         System.out.println("Class not found");
         c.printStackTrace();
         return;
     }
     
     for (Periodique periodique : periodiques) {
    	 intNbPeriodiques++;
         Catalogue.getLstPeriodiques().add(periodique);
         InterfaceGraphique.setIntNbPeriodique(intNbPeriodiques);
     }

	}
	
}
