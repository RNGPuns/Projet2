package application;


import java.time.LocalDate;

import donnees.Catalogue;
import donnees.DVD;
import donnees.Document;
import donnees.Livre;
import donnees.Periodique;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InterfaceGraphique extends Application {
	private final TableView<Document> tableDocuments = new TableView<Document>();
	private final TableView<Livre> tableLivre = new TableView<Livre>();
	private final TableView<DVD> tableDVD = new TableView<DVD>();
	private final TableView<Periodique> tablePeriodique = new TableView<Periodique>();

	@Override
	public void start(Stage arg0) throws Exception {
		TabPane root = new TabPane();
		VBox vbox = new VBox(root);
		Catalogue catalogue = Catalogue.getInstance();
		
		
		
		
		

		Tab tabCatalogue = new Tab("Catalogue");
		tabCatalogue.setGraphic(new ImageView("icon-collection.png"));
		tabCatalogue.setContent(tableDocuments);
		
		TableColumn<Document, String> ColonneNoDoc = new TableColumn<Document, String>("Numéro Document");
        ColonneNoDoc.setPrefWidth(120);
        TableColumn<Document, String> ColonneTitreDoc = new TableColumn<Document, String>("Titre du Document");
        ColonneTitreDoc.setPrefWidth(400);
        TableColumn<Document, LocalDate> ColonneDateParutionDoc = new TableColumn<Document, LocalDate>("Date de parution");
        ColonneDateParutionDoc.setPrefWidth(120);
        TableColumn<Document, String> ColonneDocDispo = new TableColumn<Document, String>("Disponibilité");
        ColonneDocDispo.setPrefWidth(120);
		
        tableDocuments.getColumns().addAll(ColonneNoDoc,ColonneTitreDoc,ColonneDateParutionDoc,ColonneDocDispo);
        
        Tab tabLivres = new Tab("Livres");
        tabLivres.setGraphic(new ImageView("icon-livre.png"));
        tabLivres.setContent(tableLivre);
        
        TableColumn<Livre, String> ColonneNoDocLivre = new TableColumn<Livre, String>("Numéro Document");
        ColonneNoDocLivre.setPrefWidth(120);
        TableColumn<Livre, String> ColonneTitreLivre = new TableColumn<Livre, String>("Titre du livre");
        ColonneTitreLivre.setPrefWidth(120);
        TableColumn<Livre, LocalDate> ColonneDateParutionLivre = new TableColumn<Livre, LocalDate>("Date de parution");
        ColonneDateParutionLivre.setPrefWidth(120);
        TableColumn<Livre, String> ColonneLivreDispo = new TableColumn<Livre, String>("Disponibilité");
        ColonneLivreDispo.setPrefWidth(120);
        TableColumn<Livre, String> ColonneMotsClesLivre = new TableColumn<Livre, String>("Mots Clés");
        ColonneMotsClesLivre.setPrefWidth(120);
        TableColumn<Livre, String> ColonneAuteurLivre = new TableColumn<Livre, String>("Auteur");
        ColonneAuteurLivre.setPrefWidth(120);
        
        tableLivre.getColumns().addAll(ColonneNoDocLivre,ColonneTitreLivre,ColonneDateParutionLivre,ColonneLivreDispo,ColonneMotsClesLivre,ColonneAuteurLivre);
        
        Tab tabDVD = new Tab("DVD");
        tabDVD.setGraphic(new ImageView("icon-dvd.png"));
        tabDVD.setContent(tableDVD);
        
        TableColumn<DVD, String> ColonneNoDocDVD = new TableColumn<DVD, String>("Numéro Document");
        ColonneNoDocDVD.setPrefWidth(120);
        TableColumn<DVD, String> ColonneTitreDVD = new TableColumn<DVD, String>("Titre du DVD");
        ColonneTitreDVD.setPrefWidth(120);
        TableColumn<DVD, LocalDate> ColonneDateParutionDVD = new TableColumn<DVD, LocalDate>("Date de parution");
        ColonneDateParutionDVD.setPrefWidth(120);
        TableColumn<DVD, String> ColonneDVDDispo = new TableColumn<DVD, String>("Disponibilité");
        ColonneDVDDispo.setPrefWidth(120);
        TableColumn<DVD, Integer> ColonneNbDisques = new TableColumn<DVD, Integer>("Nombre de disques");
        ColonneNbDisques.setPrefWidth(120);
        TableColumn<DVD, String> ColonneRealisateur = new TableColumn<DVD, String>("Auteur");
        ColonneRealisateur.setPrefWidth(120);
        
        tableDVD.getColumns().addAll(ColonneNoDocDVD,ColonneTitreDVD,ColonneDateParutionDVD,ColonneDVDDispo,ColonneNbDisques,ColonneRealisateur);
        
        Tab tabPeriodique = new Tab("Périodiques");
        tabPeriodique.setGraphic(new ImageView("icon-periodique.png"));
        tabPeriodique.setContent(tablePeriodique);
        
        TableColumn<Periodique, String> ColonneNoDocPeriodique = new TableColumn<Periodique, String>("Numéro Document");
        ColonneNoDocPeriodique.setPrefWidth(120);
        TableColumn<Periodique, String> ColonneTitrePeriodique = new TableColumn<Periodique, String>("Titre du Périodique");
        ColonneTitrePeriodique.setPrefWidth(120);
        TableColumn<Periodique, LocalDate> ColonneDateParutionPeriodique = new TableColumn<Periodique, LocalDate>("Date de parution");
        ColonneDateParutionPeriodique.setPrefWidth(120);
        TableColumn<Periodique, String> ColonnePeriodiqueDispo = new TableColumn<Periodique, String>("Disponibilité");
        ColonnePeriodiqueDispo.setPrefWidth(120);
        TableColumn<Periodique, Integer> ColonneNoVolume = new TableColumn<Periodique, Integer>("Numéro Volume");
        ColonneNoVolume.setPrefWidth(120);
        TableColumn<Periodique, String> ColonneNoPeriodique = new TableColumn<Periodique, String>("Numéro Périodique");
        ColonneNoPeriodique.setPrefWidth(120);
        
        tablePeriodique.getColumns().addAll(ColonneNoDocPeriodique,ColonneTitrePeriodique,ColonneDateParutionPeriodique,ColonnePeriodiqueDispo,ColonneNoVolume,ColonneNoPeriodique);
        
        root.getTabs().addAll(tabCatalogue);
        root.getTabs().add(tabLivres);
        root.getTabs().add(tabDVD);
        root.getTabs().add(tabPeriodique);
        
		Scene scene = new Scene(vbox);
		arg0.setResizable(false);
		arg0.setTitle("Médiathèque");
		arg0.setScene(scene);
		arg0.show();
	}
}
