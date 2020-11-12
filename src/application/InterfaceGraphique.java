package application;


import java.time.LocalDate;

import donnees.Catalogue;
import donnees.DVD;
import donnees.Document;
import donnees.Livre;
import donnees.Periodique;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class InterfaceGraphique extends Application {
	private final TableView<Document> tableDocuments = new TableView<Document>();
	private final TableView<Livre> tableLivre = new TableView<Livre>();
	private final TableView<DVD> tableDVD = new TableView<DVD>();
	private final TableView<Periodique> tablePeriodique = new TableView<Periodique>();

	@Override
	public void start(Stage arg0) throws Exception {
		//Identification
		Accordion rootIdentification = new Accordion();
		
		VBox vboxOptionsAdherents = new VBox();
		vboxOptionsAdherents.setSpacing(10);
		vboxOptionsAdherents.setPadding(new Insets(20, 0, 0, 0));
		
		GridPane panneauConnexion = new GridPane();
		panneauConnexion.setHgap(15);
		panneauConnexion.setVgap(5);
		
		Scene sceneIdentification = new Scene(rootIdentification,300,300);
		arg0.setTitle("Identification");
		
		TitledPane paneOptionsAdherents = new TitledPane("Options Adhérent", vboxOptionsAdherents);
		
		GridPane panneauInfosAdherent = new GridPane();
		panneauInfosAdherent.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(1))));
		panneauInfosAdherent.setPadding(new Insets(5, -5, 5, -5));
		panneauInfosAdherent.setHgap(24); //**Ne pas modifier svp**
		panneauInfosAdherent.setVgap(5);
		TitledPane paneConnexion = new TitledPane("Connexion", panneauConnexion);
		
		//Layout Options Adhérent
		//Rangée #1
		Label infoIdentifier = new Label("Identification par :");
		ToggleGroup togglegroup = new ToggleGroup();
		RadioButton rbNomPrenom = new RadioButton("Nom et Prénom");
		RadioButton rbNoTelephone = new RadioButton("Numéro de téléphone");
		togglegroup.getToggles().addAll(rbNomPrenom,rbNoTelephone);
				
		panneauInfosAdherent.add(infoIdentifier, 1, 0);
		panneauInfosAdherent.add(rbNomPrenom, 2, 0);
		panneauInfosAdherent.add(rbNoTelephone, 2, 1);
				
		//Rangée #2
		Label infoNom = new Label("Nom :");
		TextField txtfldNom = new TextField();
		
		panneauInfosAdherent.add(infoNom, 1, 4);
		panneauInfosAdherent.add(txtfldNom, 2, 4);
		
		//Rangée #3
		Label infoPrenom = new Label("Prénom :");
		TextField txtfldPrenom = new TextField();
		
		panneauInfosAdherent.add(infoPrenom, 1, 8);
		panneauInfosAdherent.add(txtfldPrenom, 2, 8);
				
		//Rangée #4
		HBox hboxDossier = new HBox();
		Button btnConsulterDossier = new Button("Consulter mon dossier");
		hboxDossier.getChildren().add(btnConsulterDossier);
		hboxDossier.setAlignment(Pos.CENTER);
		panneauInfosAdherent.add(hboxDossier, 1, 10,2,2);
				
		HBox hboxbtnCatalogue = new HBox();
		Button btnConsulterCatalogue = new Button("Consulter le catalogue");
		
		
		hboxbtnCatalogue.getChildren().add(btnConsulterCatalogue);
		hboxbtnCatalogue.setAlignment(Pos.CENTER);
		//btnConsulterCatalogue.setVisible(false);
		vboxOptionsAdherents.getChildren().addAll(panneauInfosAdherent,hboxbtnCatalogue);
		
		//Layout Connexion
		//Rangée #1
		Label infoNoEmploye = new Label("Numéro d'employé :");
		TextField txtfldNoEmploye = new TextField();
		
		panneauConnexion.add(infoNoEmploye, 1, 0);
		panneauConnexion.add(txtfldNoEmploye, 2, 0);
		
		//Rangée #2
		Label infoMotDePasse = new Label("Mot de passe :");
		TextField txtfldMotDePasse = new TextField();
		
		panneauConnexion.add(infoMotDePasse, 1, 2);
		panneauConnexion.add(txtfldMotDePasse, 2, 2);
		
		//Rangée #3
		HBox hboxConnexion = new HBox();
		Button btnConnexion = new Button("Connexion");
		hboxConnexion.getChildren().add(btnConnexion);
		hboxConnexion.setAlignment(Pos.CENTER);
		panneauConnexion.add(hboxConnexion, 1, 4,2,2);
		
        rootIdentification.getPanes().addAll(paneOptionsAdherents,paneConnexion);
		

		//Catalogue
		TabPane rootCatalogue = new TabPane();
		VBox vboxCatalogue = new VBox(rootCatalogue);
		Scene sceneCatalogue = new Scene(vboxCatalogue);
		
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
        
        rootCatalogue.getTabs().addAll(tabCatalogue);
        rootCatalogue.getTabs().add(tabLivres);
        rootCatalogue.getTabs().add(tabDVD);
        rootCatalogue.getTabs().add(tabPeriodique);
        
        btnConsulterCatalogue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				arg0.setTitle("Médiathèque");
				arg0.setScene(sceneCatalogue);
			}});
		
		arg0.setResizable(false);
		arg0.setScene(sceneIdentification);
		arg0.show();
		
	}
}
