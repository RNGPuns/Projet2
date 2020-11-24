package donnees;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerialisationCatalogue {
	
	// Sérialisation des données de la médiathèque
	public void SerialiserDocuments() {
		try {
            FileOutputStream fos = new FileOutputStream("Documents.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Catalogue.getLstDocuments());
            oos.close();
            fos.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        }
	}
	
	public void SerialiserLivres() {
		try {
            FileOutputStream fos = new FileOutputStream("Livres.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Catalogue.getLstLivres());
            oos.close();
            fos.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        }
	}
	
	public void SerialiserDVD() {
		try {
            FileOutputStream fos = new FileOutputStream("DVD.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Catalogue.getLstDvd());
            oos.close();
            fos.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        }
	}
	
	public void SerialiserPeriodiques() {
		try {
            FileOutputStream fos = new FileOutputStream("Periodiques.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Catalogue.getLstPeriodiques());
            oos.close();
            fos.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        }
	}

}
