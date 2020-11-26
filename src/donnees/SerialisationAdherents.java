package donnees;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import application.InterfaceGraphique;

public class SerialisationAdherents {
	
	public void Serialiser() {
	     
        try {
            FileOutputStream fos = new FileOutputStream("Données sérialisées/Adherents.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(InterfaceGraphique.getLstAdherents());
            oos.close();
            fos.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        }
	}
}
