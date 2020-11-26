package donnees;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import application.InterfaceGraphique;

public class DeserialisationPreposes {
		
	public void Deserialiser() {
		 ArrayList<Prepose> t= new ArrayList<Prepose>();
        
        try
        {
            FileInputStream fis = new FileInputStream("Preposes.ser");
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
        
        for (Prepose prepose : t) {
            InterfaceGraphique.getTablePreposes().getItems().add(prepose);
        }

 	}
}
		 

