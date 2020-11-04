package application;


import donnees.Catalogue;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InterfaceGraphique extends Application {

	@Override
	public void start(Stage arg0) throws Exception {
		TabPane root = new TabPane();
		VBox vbox = new VBox(root);
		Catalogue catalogue = Catalogue.getInstance();
		
		Tab tabCatalogue = new Tab("Catalogue");
        Tab tabLivres = new Tab("Livres");
        Tab tabDVD = new Tab("DVD");
        Tab tabPeriodique = new Tab("Périodiques");
        
        tabCatalogue.setGraphic(new ImageView("icon-collection.png"));
        tabLivres.setGraphic(new ImageView("icon-livre.png"));
        tabDVD.setGraphic(new ImageView("icon-dvd.png"));
        tabPeriodique.setGraphic(new ImageView("icon-periodique.png"));

        root.getTabs().add(tabCatalogue);
        root.getTabs().add(tabLivres);
        root.getTabs().add(tabDVD);
        root.getTabs().add(tabPeriodique);
        
		Scene scene = new Scene(vbox,500,450);
		arg0.setTitle("Médiathèque");
		arg0.setScene(scene);
		arg0.show();
	}
}
