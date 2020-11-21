package donnees;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import application.InterfaceGraphique;

public class SerialisationPreposes {
	
	public void Serialiser() {
		//InterfaceGraphique t = new InterfaceGraphique();
	     
        try {
            FileOutputStream fos = new FileOutputStream("Preposes.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(InterfaceGraphique.getLstPrepose());
            oos.close();
            fos.close();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        }
	}
}
