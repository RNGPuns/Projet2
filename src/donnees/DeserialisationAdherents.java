package donnees;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import application.InterfaceGraphique;

public class DeserialisationAdherents {
	
	public void Deserialiser() {
		 ArrayList<Adherent> t= new ArrayList<Adherent>();
		 int intNbAdherent = 1;
       
       try
       {
           FileInputStream fis = new FileInputStream("Données sérialisées/Adherents.ser");
           ObjectInputStream ois = new ObjectInputStream(fis);

           t = (ArrayList) ois.readObject();

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
       
       for (Adherent adherent : t) {
    	   intNbAdherent++;
           InterfaceGraphique.getLstAdherents().add(adherent);
           InterfaceGraphique.setIntNbAdherent(intNbAdherent);
       }

	}


}
