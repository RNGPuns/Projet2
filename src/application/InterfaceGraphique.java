package application;


import donnees.Catalogue;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InterfaceGraphique extends Application {

	@Override
	public void start(Stage arg0) throws Exception {
		Group root = new Group();
		
		Catalogue catalogue = Catalogue.getInstance();
		
		Scene scene = new Scene(root,500,450);
		arg0.setTitle("Médiathèque");
		arg0.setScene(scene);
		arg0.show();
	}
}
