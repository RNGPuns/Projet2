package application;


import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;

import javax.swing.GroupLayout.Alignment;

import donnees.Catalogue;
import donnees.DVD;
import donnees.Document;
import donnees.Livre;
import donnees.Periodique;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class InterfaceGraphique extends Application {
	private final TableView<Document> tableDocuments = new TableView<Document>();
	private final TableView<Livre> tableLivre = new TableView<Livre>();
	private final TableView<DVD> tableDVD = new TableView<DVD>();
	private final TableView<Periodique> tablePeriodique = new TableView<Periodique>();
	private final TableView<String> tablePreposes = new TableView<String>();
	
	String strIdentifiantAdmin = "";
	String strMotDePasseAdmin = "";

	@Override
	public void start(Stage arg0) throws Exception {
		//Scene Identification
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
		vboxOptionsAdherents.getChildren().addAll(panneauInfosAdherent,hboxbtnCatalogue);
		
		//Layout Connexion
		//Rangée #1
		Label infoNoEmploye = new Label("Numéro d'employé :");
		TextField txtfldNoEmploye = new TextField("admin");
		
		panneauConnexion.add(infoNoEmploye, 1, 0);
		panneauConnexion.add(txtfldNoEmploye, 2, 0);
		
		//Rangée #2
		Label infoMotDePasse = new Label("Mot de passe :");
		TextField txtfldMotDePasse = new TextField("79251367");
		
		panneauConnexion.add(infoMotDePasse, 1, 2);
		panneauConnexion.add(txtfldMotDePasse, 2, 2);
		
		//Rangée #3
		HBox hboxConnexion = new HBox();
		Button btnConnexion = new Button("Connexion");
		
		hboxConnexion.getChildren().add(btnConnexion);
		hboxConnexion.setAlignment(Pos.CENTER);
		panneauConnexion.add(hboxConnexion, 1, 4,2,2);
		
        rootIdentification.getPanes().addAll(paneOptionsAdherents,paneConnexion);
		
		//Scene Catalogue adhérent
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
        
        //Scene admin
        Group rootAdmin = new Group();
		HBox hboxRootAdmin = new HBox(rootAdmin);
		VBox vboxOptionsAdmin = new VBox();
		Scene sceneAdmin = new Scene(hboxRootAdmin);
		
		hboxRootAdmin.setSpacing(5);
		hboxRootAdmin.setPadding(new Insets(5,5,5,5));
		vboxOptionsAdmin.setSpacing(5);
		vboxOptionsAdmin.setAlignment(Pos.TOP_CENTER);
		
		TableColumn<String, String> ColonneNoEmploye = new TableColumn<String, String>("Numéro d'employé");
		ColonneNoEmploye.setPrefWidth(120);
		ColonneNoEmploye.setMaxWidth(120);
        TableColumn<String, String> ColonneNomEmploye = new TableColumn<String, String>("Nom");
        ColonneNomEmploye.setPrefWidth(120);
        ColonneNomEmploye.setMaxWidth(120);
        TableColumn<String, LocalDate> ColonnePrenomEmploye = new TableColumn<String, LocalDate>("Prénom");
        ColonnePrenomEmploye.setPrefWidth(120);
        ColonnePrenomEmploye.setMaxWidth(120);
        TableColumn<String, String> ColonneAdresseEmploye = new TableColumn<String, String>("Adresse");
        ColonneAdresseEmploye.setPrefWidth(120);
        ColonneAdresseEmploye.setMaxWidth(120);
        TableColumn<String, String> ColonneNoTelephoneEmploye = new TableColumn<String, String>("Téléphone");
        ColonneNoTelephoneEmploye.setPrefWidth(120);
        ColonneNoTelephoneEmploye.setMaxWidth(120);
        
        tablePreposes.getColumns().addAll(ColonneNoEmploye,ColonneNomEmploye,ColonnePrenomEmploye,ColonneAdresseEmploye,ColonneNoTelephoneEmploye);
        
        Text txtGestionPrepose = new Text("Gestion préposés");
        Button btnAjouterPrepose = new Button("Ajouter un préposé");
        
        btnAjouterPrepose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				ButtonType confirmer = new ButtonType("Confirmer", ButtonData.OK_DONE);
				ButtonType annuler = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
				Alert fenetreAjouterPrepose = new Alert(AlertType.NONE, "default Dialog",confirmer,annuler);
				GridPane gridpane = new GridPane();
				gridpane.setHgap(5);
				
				Text nom = new Text("Nom:");
				Text prenom = new Text("Prénom:");
				Text adresse = new Text("Adresse:");
				Text telephone = new Text("Téléphone:");
				Text motdepasse = new Text("Mot de passe:");
				
				gridpane.add(nom, 0, 0);
				gridpane.add(prenom, 0, 1);
				gridpane.add(adresse, 0, 2);
				gridpane.add(telephone, 0, 3);
				gridpane.add(motdepasse, 0, 4);
				
				TextField champsNom = new TextField();
				TextField champsPrenom = new TextField();
				TextField champsAdresse = new TextField();
				TextField champsTelephone = new TextField();
				TextField champsMotDePasse = new TextField();
				
				gridpane.add(champsNom, 1, 0);
				gridpane.add(champsPrenom, 1, 1);
				gridpane.add(champsAdresse, 1, 2);
				gridpane.add(champsTelephone, 1, 3);
				gridpane.add(champsMotDePasse, 1, 4);
				
				fenetreAjouterPrepose.setTitle("Ajouter un préposé");
				Stage stage = (Stage) fenetreAjouterPrepose.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("icon-utilisateur.png"));
				fenetreAjouterPrepose.setHeaderText(null);
				fenetreAjouterPrepose.getDialogPane().setContent(gridpane);
				fenetreAjouterPrepose.showAndWait();
			}});
        
        Button btnSupprimerPrepose = new Button("Supprimer un préposé");
        Separator separateur = new Separator(Orientation.HORIZONTAL);
        Button btnDeconnexion = new Button("Déconnexion");
        
        btnAjouterPrepose.setMaxWidth(Double.MAX_VALUE);
        btnDeconnexion.setMaxWidth(Double.MAX_VALUE);
        
        vboxOptionsAdmin.getChildren().addAll(txtGestionPrepose,btnAjouterPrepose,btnSupprimerPrepose,separateur,btnDeconnexion);
        
        hboxRootAdmin.getChildren().addAll(tablePreposes,vboxOptionsAdmin);
        
        btnConnexion.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					Scanner scFichierIdentifiant = new Scanner(new File("Identifiants préposé.txt")); //Début de la lecture du fichier
					String strLigneIdentifiant = scFichierIdentifiant.nextLine().replaceAll("\\s+","");
					String strLigneMotDePasse = scFichierIdentifiant.nextLine().replaceAll("\\s+","");
					
					strIdentifiantAdmin = strLigneIdentifiant.split(":")[1];
					strMotDePasseAdmin = strLigneMotDePasse.split(":")[1];
					
					scFichierIdentifiant.close(); //Fin de la lecture du fichier identifiant
					
				} catch (FileNotFoundException exception) {
					
					exception.printStackTrace();
				}
				
				if (txtfldNoEmploye.getText().equals(strIdentifiantAdmin) && txtfldMotDePasse.getText().equals(strMotDePasseAdmin)) {
					arg0.setScene(sceneAdmin);
				}
				
			}});
        
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
