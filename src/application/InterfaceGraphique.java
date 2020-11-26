package application;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import donnees.Adherent;
import donnees.Catalogue;
import donnees.DVD;
import donnees.DeserialisationAdherents;
import donnees.DeserialisationPreposes;
import donnees.Document;
import donnees.IdentifiantsPrepose;
import donnees.Livre;
import donnees.Periodique;
import donnees.Prepose;
import donnees.SerialisationAdherents;
import donnees.SerialisationCatalogue;
import donnees.SerialisationPreposes;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InterfaceGraphique extends Application {
	private final static TableView<Document> tableDocuments = new TableView<Document>();
	private final static TableView<Livre> tableLivre = new TableView<Livre>();
	private final static TableView<DVD> tableDVD = new TableView<DVD>();
	private final static TableView<Periodique> tablePeriodique = new TableView<Periodique>();
	private final static TableView<Prepose> tablePreposes = new TableView<Prepose>();
	
	private final static TableView<Document> tableDocumentsPrepose = new TableView<Document>();
	private final static TableView<Livre> tableLivrePrepose = new TableView<Livre>();
	private final static TableView<DVD> tableDVDPrepose = new TableView<DVD>();
	private final static TableView<Periodique> tablePeriodiquePrepose = new TableView<Periodique>();
	private final static TableView<Adherent> tableAdherent = new TableView<Adherent>();
	
	ArrayList<IdentifiantsPrepose> lstIdentifiants = new ArrayList<IdentifiantsPrepose>();
	static ArrayList<Prepose> lstPrepose = new ArrayList<Prepose>();
	static ArrayList<Adherent> lstAdherents = new ArrayList<Adherent>();
	
	String strIdentifiant = "";
	String strMotDePasse = "";
	
	int intNbEmploye = 1;
	static int intNbLivre = 0;
	static int intNbDVD = 0;
	static int intNbPeriodique = 0;
	static int intNbAdherent = 0;
	
	File serPrepose = new File("Données sérialisées/Preposes.ser");
	File serAdherent = new File("Données sérialisées/Adherents.ser");
	
	@Override
	public void start(Stage arg0) throws Exception {
		
		//lecture du fichier d'identifiants et fichier Prepose.ser
		try {
			if (serPrepose.exists()) {
				DeserialisationPreposes dP = new DeserialisationPreposes();
	        	dP.Deserialiser();
			}
			
			if (serAdherent.exists()) {
				DeserialisationAdherents dA = new DeserialisationAdherents();
				dA.Deserialiser();
			}
			
			Scanner scFichierIdentifiant = new Scanner(new File("Identifiants préposé.txt")); //Début de la lecture du fichier
			
			while (scFichierIdentifiant.hasNextLine()) { //Lecture du fichier
				String strTypeUtilisateur = scFichierIdentifiant.nextLine().substring(3); //Caractère bizzare pour aucune raison. substring pour les enlever.
				String strLigneIdentifiant = scFichierIdentifiant.nextLine().replaceAll("\\s+","").split(":")[1];
				String strLigneMotDePasse = scFichierIdentifiant.nextLine().replaceAll("\\s+","").split(":")[1];
				scFichierIdentifiant.nextLine();
				
				//Création d'un objet contenant les identifiants, ajout d'un ArrayList pour vérification et ajout au TableView
				IdentifiantsPrepose identifiants = new IdentifiantsPrepose(strTypeUtilisateur, strLigneIdentifiant, strLigneMotDePasse);
				lstIdentifiants.add(identifiants);
				
			}
			
			scFichierIdentifiant.close(); //Fin de la lecture du fichier identifiant
			
		} catch (FileNotFoundException exception) {
			
			exception.printStackTrace();
		}
		
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
		
		arg0.getIcons().add(new Image("icon-mediatheque.png"));
		
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
//		TextField txtfldNoEmploye = new TextField("admin"); //Mettre vide à fin***
		TextField txtfldNoEmploye = new TextField("EMP0"); //Mettre vide à fin***

		
		panneauConnexion.add(infoNoEmploye, 1, 0);
		panneauConnexion.add(txtfldNoEmploye, 2, 0);
		
		//Rangée #2
		Label infoMotDePasse = new Label("Mot de passe :");
		PasswordField pwfldMotDePasse = new PasswordField();
//		pwfldMotDePasse.setText("79251367"); //Mettre vide à fin***
		pwfldMotDePasse.setText("EMP0"); //Mettre vide à fin***
		
		panneauConnexion.add(infoMotDePasse, 1, 2);
		panneauConnexion.add(pwfldMotDePasse, 2, 2);
		
		//Rangée #3
		Text txtPreposeAdmin = new Text("Type d'utilisateur:");
		ToggleGroup tglgrpPreposeAdmin = new ToggleGroup();
		RadioButton rbPrepose = new RadioButton("Préposé");
		rbPrepose.setSelected(true);
		RadioButton rbAdmin = new RadioButton("Administrateur");
		tglgrpPreposeAdmin.getToggles().addAll(rbPrepose,rbAdmin);
		
		panneauConnexion.add(txtPreposeAdmin, 1, 4);
		panneauConnexion.add(rbPrepose, 2, 4);
		panneauConnexion.add(rbAdmin, 2, 5);
		
		//Rangée #4
		HBox hboxConnexion = new HBox();
		Button btnConnexion = new Button("Connexion");
		
		hboxConnexion.getChildren().add(btnConnexion);
		hboxConnexion.setAlignment(Pos.CENTER);
		panneauConnexion.add(hboxConnexion, 1, 6,2,2);
		
        rootIdentification.getPanes().addAll(paneOptionsAdherents,paneConnexion);
		
		//Scene Catalogue adhérent
        TabPane rootCatalogue = new TabPane();
		HBox hboxOptionsRecherche = new HBox();
		VBox vboxCatalogue = new VBox(hboxOptionsRecherche, rootCatalogue);
		HBox hboxRootCatalogue = new HBox(vboxCatalogue);
		Scene sceneCatalogue = new Scene(hboxRootCatalogue);
		
		GridPane panneauDossierAdherent = new GridPane();
		VBox vboxDossierAdherent = new VBox(panneauDossierAdherent);
		vboxDossierAdherent.setSpacing(5);
		hboxRootCatalogue.getChildren().add(vboxDossierAdherent);
		panneauDossierAdherent.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(1))));
		panneauDossierAdherent.setPadding(new Insets(5, 10, 5, 0));
		panneauDossierAdherent.setHgap(20); //**Ne pas modifier svp**
		panneauDossierAdherent.setVgap(2);
		
		Label infoNomAdherent = new Label("Nom :");
		TextField txtfldNomAdherent = new TextField();
		
		panneauDossierAdherent.add(infoNomAdherent, 1, 4);
		panneauDossierAdherent.add(txtfldNomAdherent, 2, 4);
		
		Label infoPrenomAdherent = new Label("Prénom :");
		TextField txtfldPrenomAdherent = new TextField();
		
		panneauDossierAdherent.add(infoPrenomAdherent, 1, 8);
		panneauDossierAdherent.add(txtfldPrenomAdherent, 2, 8);
		
		HBox hboxDossierAdherent = new HBox();
		Button btnConsulterDossierAdherent = new Button("Consulter mon dossier");
		hboxDossierAdherent.getChildren().add(btnConsulterDossierAdherent);
		hboxDossierAdherent.setAlignment(Pos.CENTER);
		panneauDossierAdherent.add(hboxDossierAdherent, 1, 10,2,2);
		
		Label infoIdentifierAdherent = new Label("Identification par :");
		ToggleGroup togglegroup2 = new ToggleGroup();
		RadioButton rbNomPrenomAdherent = new RadioButton("Nom et Prénom");
		RadioButton rbNoTelephoneAdherent = new RadioButton("Numéro de téléphone");
		togglegroup2.getToggles().addAll(rbNomPrenomAdherent,rbNoTelephoneAdherent);
				
		panneauDossierAdherent.add(infoIdentifierAdherent, 1, 0);
		panneauDossierAdherent.add(rbNomPrenomAdherent, 2, 0);
		panneauDossierAdherent.add(rbNoTelephoneAdherent, 2, 1);

		Button btnQuitterCatalogue = new Button("Quitter");
		vboxDossierAdherent.getChildren().add(btnQuitterCatalogue);
		vboxDossierAdherent.setAlignment(Pos.TOP_CENTER);
		
		hboxOptionsRecherche.setPadding(new Insets(7,10,7,10));
		hboxOptionsRecherche.setSpacing(10);
		
		Text txtRecherche = new Text("Rechercher par: ");
		
		ToggleGroup togglegroupOptionsRechercheAdherent = new ToggleGroup();
		RadioButton rbAuteurRealisateur = new RadioButton("Auteur/Réalisateur");
		RadioButton rbMotsCles = new RadioButton("Mots Clés");
		togglegroupOptionsRechercheAdherent.getToggles().addAll(rbAuteurRealisateur,rbMotsCles);
		
		TextField txtfldRecherche = new TextField();
		Button btnEffacer = new Button("Effacer");
		
		hboxOptionsRecherche.getChildren().addAll(txtRecherche,rbAuteurRealisateur,rbMotsCles,txtfldRecherche,btnEffacer);
		Catalogue catalogue = Catalogue.getInstance();
		
		Tab tabCatalogue = new Tab("Catalogue");
		tabCatalogue.setClosable(false);
		tabCatalogue.setGraphic(new ImageView("icon-collection.png"));
		tabCatalogue.setContent(tableDocuments);
		
		TableColumn<Document, String> ColonneNoDoc = new TableColumn<Document, String>("Numéro Document");
		ColonneNoDoc.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getNoDoc())); //Changer à comme les autres
        ColonneNoDoc.setPrefWidth(120);
        TableColumn<Document, String> ColonneTitreDoc = new TableColumn<Document, String>("Titre du Document");
        ColonneTitreDoc.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        ColonneTitreDoc.setPrefWidth(400);
        TableColumn<Document, LocalDate> ColonneDateParutionDoc = new TableColumn<Document, LocalDate>("Date de parution");
        ColonneDateParutionDoc.setCellValueFactory(new PropertyValueFactory<>("DateParution"));
        ColonneDateParutionDoc.setPrefWidth(120);
        TableColumn<Document, String> ColonneDocDispo = new TableColumn<Document, String>("Disponibilité");
        ColonneDocDispo.setCellValueFactory(new PropertyValueFactory<>("Disponible"));
        ColonneDocDispo.setPrefWidth(120);
		
        tableDocuments.getColumns().addAll(ColonneNoDoc,ColonneTitreDoc,ColonneDateParutionDoc,ColonneDocDispo);
        
        for (Document doc : Catalogue.getLstDocuments()) {
        	tableDocuments.getItems().add(doc);
        }
        
        Tab tabLivres = new Tab("Livres");
        tabLivres.setClosable(false);
        tabLivres.setGraphic(new ImageView("icon-livre.png"));
        tabLivres.setContent(tableLivre);
        
        TableColumn<Livre, String> ColonneNoDocLivre = new TableColumn<Livre, String>("Numéro Document");
        ColonneNoDocLivre.setCellValueFactory(new PropertyValueFactory<>("NoDoc"));
        ColonneNoDocLivre.setPrefWidth(120);
        TableColumn<Livre, String> ColonneTitreLivre = new TableColumn<Livre, String>("Titre du livre");
        ColonneTitreLivre.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        ColonneTitreLivre.setPrefWidth(120);
        TableColumn<Livre, LocalDate> ColonneDateParutionLivre = new TableColumn<Livre, LocalDate>("Date de parution");
        ColonneDateParutionLivre.setCellValueFactory(new PropertyValueFactory<>("DateParution"));
        ColonneDateParutionLivre.setPrefWidth(120);
        TableColumn<Livre, String> ColonneLivreDispo = new TableColumn<Livre, String>("Disponibilité");
        ColonneLivreDispo.setCellValueFactory(new PropertyValueFactory<>("Disponible"));
        ColonneLivreDispo.setPrefWidth(120);
        TableColumn<Livre, String> ColonneAuteurLivre = new TableColumn<Livre, String>("Auteur");
        ColonneAuteurLivre.setCellValueFactory(new PropertyValueFactory<>("Auteur"));
        ColonneAuteurLivre.setPrefWidth(120);
        
        tableLivre.getColumns().addAll(ColonneNoDocLivre,ColonneTitreLivre,ColonneDateParutionLivre,ColonneLivreDispo,ColonneAuteurLivre);
        
        for (Livre livre : Catalogue.getLstLivres()) {
        	tableLivre.getItems().add(livre);
        }
        
        Tab tabDVD = new Tab("DVD");
        tabDVD.setClosable(false);
        tabDVD.setGraphic(new ImageView("icon-dvd.png"));
        tabDVD.setContent(tableDVD);
        
        TableColumn<DVD, String> ColonneNoDocDVD = new TableColumn<DVD, String>("Numéro Document");
        ColonneNoDocDVD.setCellValueFactory(new PropertyValueFactory<>("NoDoc"));
        ColonneNoDocDVD.setPrefWidth(120);
        TableColumn<DVD, String> ColonneTitreDVD = new TableColumn<DVD, String>("Titre du DVD");
        ColonneTitreDVD.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        ColonneTitreDVD.setPrefWidth(120);
        TableColumn<DVD, LocalDate> ColonneDateParutionDVD = new TableColumn<DVD, LocalDate>("Date de parution");
        ColonneDateParutionDVD.setCellValueFactory(new PropertyValueFactory<>("DateParution"));
        ColonneDateParutionDVD.setPrefWidth(120);
        TableColumn<DVD, String> ColonneDVDDispo = new TableColumn<DVD, String>("Disponibilité");
        ColonneDVDDispo.setCellValueFactory(new PropertyValueFactory<>("Disponible"));
        ColonneDVDDispo.setPrefWidth(120);
        TableColumn<DVD, Integer> ColonneNbDisques = new TableColumn<DVD, Integer>("Nombre de disques");
        ColonneNbDisques.setCellValueFactory(new PropertyValueFactory<>("NbDisques"));
        ColonneNbDisques.setPrefWidth(120);
        TableColumn<DVD, String> ColonneRealisateur = new TableColumn<DVD, String>("Auteur");
        ColonneRealisateur.setCellValueFactory(new PropertyValueFactory<>("StrRealisateur"));
        ColonneRealisateur.setPrefWidth(120);
        
        tableDVD.getColumns().addAll(ColonneNoDocDVD,ColonneTitreDVD,ColonneDateParutionDVD,ColonneDVDDispo,ColonneNbDisques,ColonneRealisateur);
        
        for (DVD dvd : Catalogue.getLstDvd()) {
        	tableDVD.getItems().add(dvd);
        }
        
        Tab tabPeriodique = new Tab("Périodiques");
        tabPeriodique.setClosable(false);
        tabPeriodique.setGraphic(new ImageView("icon-periodique.png"));
        tabPeriodique.setContent(tablePeriodique);
        
        TableColumn<Periodique, String> ColonneNoDocPeriodique = new TableColumn<Periodique, String>("Numéro Document");
        ColonneNoDocPeriodique.setCellValueFactory(new PropertyValueFactory<>("NoDoc"));
        ColonneNoDocPeriodique.setPrefWidth(120);
        TableColumn<Periodique, String> ColonneTitrePeriodique = new TableColumn<Periodique, String>("Titre du Périodique");
        ColonneTitrePeriodique.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        ColonneTitrePeriodique.setPrefWidth(120);
        TableColumn<Periodique, LocalDate> ColonneDateParutionPeriodique = new TableColumn<Periodique, LocalDate>("Date de parution");
        ColonneDateParutionPeriodique.setCellValueFactory(new PropertyValueFactory<>("DateParution"));
        ColonneDateParutionPeriodique.setPrefWidth(120);
        TableColumn<Periodique, String> ColonnePeriodiqueDispo = new TableColumn<Periodique, String>("Disponibilité");
        ColonnePeriodiqueDispo.setCellValueFactory(new PropertyValueFactory<>("Disponible"));
        ColonnePeriodiqueDispo.setPrefWidth(120);
        TableColumn<Periodique, Integer> ColonneNoVolume = new TableColumn<Periodique, Integer>("Numéro Volume");
        ColonneNoVolume.setCellValueFactory(new PropertyValueFactory<>("NoVolume"));
        ColonneNoVolume.setPrefWidth(120);
        TableColumn<Periodique, String> ColonneNoPeriodique = new TableColumn<Periodique, String>("Numéro Périodique");
        ColonneNoPeriodique.setCellValueFactory(new PropertyValueFactory<>("NoPeriodique"));
        ColonneNoPeriodique.setPrefWidth(120);
        
        tablePeriodique.getColumns().addAll(ColonneNoDocPeriodique,ColonneTitrePeriodique,ColonneDateParutionPeriodique,ColonnePeriodiqueDispo,ColonneNoVolume,ColonneNoPeriodique);
        
        for (Periodique periodique : Catalogue.getLstPeriodiques()) {
        	tablePeriodique.getItems().add(periodique);
        }
        
        rootCatalogue.getTabs().addAll(tabCatalogue,tabLivres,tabDVD,tabPeriodique);
        
        //Scene admin
        Group rootAdmin = new Group();
		HBox hboxRootAdmin = new HBox(rootAdmin);
		VBox vboxOptionsAdmin = new VBox();
		Scene sceneAdmin = new Scene(hboxRootAdmin);
		
		hboxRootAdmin.setSpacing(5);
		hboxRootAdmin.setPadding(new Insets(5,5,5,5));
		vboxOptionsAdmin.setSpacing(5);
		vboxOptionsAdmin.setAlignment(Pos.TOP_CENTER);
		
		TableColumn<Prepose, String> ColonneNoEmploye = new TableColumn<Prepose, String>("Numéro d'employé");
		ColonneNoEmploye.setCellValueFactory(new PropertyValueFactory<>("strNoEmploye"));
		ColonneNoEmploye.setPrefWidth(120);
		ColonneNoEmploye.setMaxWidth(120);
        TableColumn<Prepose, String> ColonneNomEmploye = new TableColumn<Prepose, String>("Nom");
        ColonneNomEmploye.setCellValueFactory(new PropertyValueFactory<>("strNom"));
        ColonneNomEmploye.setPrefWidth(120);
        ColonneNomEmploye.setMaxWidth(120);
        TableColumn<Prepose, String> ColonnePrenomEmploye = new TableColumn<Prepose, String>("Prénom");
        ColonnePrenomEmploye.setCellValueFactory(new PropertyValueFactory<>("strPrenom"));
        ColonnePrenomEmploye.setPrefWidth(120);
        ColonnePrenomEmploye.setMaxWidth(120);
        TableColumn<Prepose, String> ColonneAdresseEmploye = new TableColumn<Prepose, String>("Adresse");
        ColonneAdresseEmploye.setCellValueFactory(new PropertyValueFactory<>("strAdresse"));
        ColonneAdresseEmploye.setPrefWidth(120);
        ColonneAdresseEmploye.setMaxWidth(120);
        TableColumn<Prepose, String> ColonneNoTelephoneEmploye = new TableColumn<Prepose, String>("Téléphone");
        ColonneNoTelephoneEmploye.setCellValueFactory(new PropertyValueFactory<>("strTelephone"));
        ColonneNoTelephoneEmploye.setPrefWidth(120);
        ColonneNoTelephoneEmploye.setMaxWidth(120);
        
        tablePreposes.getColumns().addAll(ColonneNoEmploye,ColonneNomEmploye,ColonnePrenomEmploye,ColonneAdresseEmploye,ColonneNoTelephoneEmploye);
        
        for (Prepose prepose : lstPrepose) {
        	tablePreposes.getItems().add(prepose);
        }
        
        Text txtGestionPrepose = new Text("Gestion préposés");
        Button btnAjouterPrepose = new Button("Ajouter un préposé");
        
        
        Button btnSupprimerPrepose = new Button("Supprimer un préposé");
        Separator separateur = new Separator(Orientation.HORIZONTAL);
        Button btnDeconnexion = new Button("Déconnexion");
        
        btnDeconnexion.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				arg0.setScene(sceneIdentification);
				arg0.setTitle("Identification");
			}});
        
        btnAjouterPrepose.setMaxWidth(Double.MAX_VALUE);
        btnDeconnexion.setMaxWidth(Double.MAX_VALUE);
        
        vboxOptionsAdmin.getChildren().addAll(txtGestionPrepose,btnAjouterPrepose,btnSupprimerPrepose,separateur,btnDeconnexion);
        
        hboxRootAdmin.getChildren().addAll(tablePreposes,vboxOptionsAdmin);
        
        //Scene préposé
        TabPane rootPrepose= new TabPane();
		HBox hboxOptionsRecherchePrepose = new HBox();
		hboxOptionsRecherchePrepose.setPadding(new Insets(3,10,3,10));
		hboxOptionsRecherchePrepose.setSpacing(10);
		
		hboxOptionsRecherchePrepose.setSpacing(20);
		VBox vboxPrepose = new VBox(hboxOptionsRecherchePrepose, rootPrepose);
		HBox hboxRootPrepose = new HBox(vboxPrepose);
		Scene scenePrepose = new Scene(hboxRootPrepose);
		
		VBox vboxOptionsPrepose = new VBox();
		hboxRootPrepose.getChildren().add(vboxOptionsPrepose);
		
		Accordion accordionOptionsPrepose = new Accordion();
		
		VBox vboxGestionCatalogue = new VBox();
		vboxGestionCatalogue.setSpacing(5);
		vboxGestionCatalogue.setAlignment(Pos.CENTER);
		
		Button btnAjouterDocument = new Button("Ajouter un document");
		btnAjouterDocument.setMaxWidth(Double.MAX_VALUE);
		
		Button btnSupprimerDocument = new Button("Supprimer un document");
		btnSupprimerDocument.setMaxWidth(Double.MAX_VALUE);
		
		vboxGestionCatalogue.getChildren().addAll(btnAjouterDocument,btnSupprimerDocument);
		
		VBox vboxGestionAdherent = new VBox();
		vboxGestionAdherent.setSpacing(5);
		vboxGestionAdherent.setAlignment(Pos.CENTER);
		
		Button btnAjouterAdherent = new Button("Ajouter un adhérent");
		btnAjouterAdherent.setMaxWidth(Double.MAX_VALUE);
		
		Button btnModifierAdherent = new Button("Modifier un adhérent");
		btnModifierAdherent.setMaxWidth(Double.MAX_VALUE);
		
		Button btnSupprimerAdherent = new Button("Supprimer un adhérent");
		btnSupprimerAdherent.setMaxWidth(Double.MAX_VALUE);
		
		Button btnPayerSolde = new Button("Payer un solde");
		btnPayerSolde.setMaxWidth(Double.MAX_VALUE);
		
		vboxGestionAdherent.getChildren().addAll(btnAjouterAdherent,btnModifierAdherent,btnSupprimerAdherent,btnPayerSolde);
		
		VBox vboxGestionPrets = new VBox();
		vboxGestionPrets.setSpacing(5);
		vboxGestionPrets.setAlignment(Pos.CENTER);
		
		Button btnInscrirePret = new Button("Inscrire un prêt");
		btnInscrirePret.setMaxWidth(Double.MAX_VALUE);
		
		Button btnInscrireRetour = new Button("Inscrire un retour");
		btnInscrireRetour.setMaxWidth(Double.MAX_VALUE);
		
		vboxGestionPrets.getChildren().addAll(btnInscrirePret,btnInscrireRetour);
		
		TitledPane tpGestionCatalogue = new TitledPane("Gestion du catalogue" , vboxGestionCatalogue);
		
		TitledPane tpGestionAdherents = new TitledPane("Gestion des adhérents" , vboxGestionAdherent);
		
		tpGestionAdherents.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				vboxPrepose.getChildren().clear();
				hboxRootPrepose.getChildren().remove(vboxOptionsPrepose);
				
				
				TableColumn<Adherent, String> ColonneNoAdherent = new TableColumn<Adherent, String>("Numéro de l'Adhérent");
				ColonneNoAdherent.setCellValueFactory(new PropertyValueFactory<>("StrNoAdherent"));
				ColonneNoAdherent.setPrefWidth(175);
		        TableColumn<Adherent, String> ColonneNomAdherent = new TableColumn<Adherent, String>("Nom");
		        ColonneNomAdherent.setCellValueFactory(new PropertyValueFactory<>("StrNomAdherent"));
		        ColonneNomAdherent.setPrefWidth(120);
		        TableColumn<Adherent, String> ColonnePrenomAdherent = new TableColumn<Adherent, String>("Prénom");
		        ColonnePrenomAdherent.setCellValueFactory(new PropertyValueFactory<>("StrPrenomAdherent"));
		        ColonnePrenomAdherent.setPrefWidth(120);
		        TableColumn<Adherent, String> ColonneAdresseAdherent = new TableColumn<Adherent, String>("Adresse");
		        ColonneAdresseAdherent.setCellValueFactory(new PropertyValueFactory<>("StrAdresseAdherent"));
		        ColonneAdresseAdherent.setPrefWidth(120);
		        TableColumn<Adherent, String> ColonneTelephoneAdherent = new TableColumn<Adherent, String>("Téléphone");
		        ColonneTelephoneAdherent.setCellValueFactory(new PropertyValueFactory<>("StrTelephoneAdherent"));
		        ColonneTelephoneAdherent.setPrefWidth(120);
		        TableColumn<Adherent, Integer> ColonneNbPretsActifs = new TableColumn<Adherent, Integer>("Prêts Actifs");
		        ColonneNbPretsActifs.setCellValueFactory(new PropertyValueFactory<>("IntNbPretsActifs"));
		        ColonneNbPretsActifs.setPrefWidth(120);
		        TableColumn<Adherent, Double> ColonneSoldeDu = new TableColumn<Adherent, Double>("Solde dû");
		        ColonneSoldeDu.setCellValueFactory(new PropertyValueFactory<>("DblSoldeDu"));
		        ColonneSoldeDu.setPrefWidth(120);
		        
		        for (Adherent adherent : lstAdherents) {
		        	tableAdherent.getItems().add(adherent);
		        }
		        
		        tableAdherent.getColumns().clear();
		        tableAdherent.getColumns().addAll(ColonneNoAdherent,ColonneNomAdherent,ColonnePrenomAdherent,ColonneAdresseAdherent,ColonneTelephoneAdherent,ColonneNbPretsActifs,ColonneSoldeDu);
				
				vboxPrepose.getChildren().add(tableAdherent);
				hboxRootPrepose.getChildren().add(vboxOptionsPrepose);
			}
		});
		
		TitledPane tpGestionPrets = new TitledPane("Gestion des prêts" , vboxGestionPrets);
		
		Button btnQuitterPrepose = new Button("Déconnexion");
		
		tpGestionCatalogue.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				vboxPrepose.getChildren().clear();
				hboxRootPrepose.getChildren().removeAll(tableAdherent,vboxOptionsPrepose);
				
				vboxPrepose.getChildren().addAll(hboxOptionsRecherchePrepose, rootPrepose);
				hboxRootPrepose.getChildren().addAll(vboxOptionsPrepose);
			}
		});
		
		tpGestionPrets.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				vboxPrepose.getChildren().clear();
				hboxRootPrepose.getChildren().removeAll(tableAdherent,vboxOptionsPrepose);
				
				vboxPrepose.getChildren().addAll(hboxOptionsRecherchePrepose, rootPrepose);
				hboxRootPrepose.getChildren().addAll(vboxOptionsPrepose);
			}
		});
		
		vboxOptionsPrepose.setAlignment(Pos.TOP_CENTER);
		vboxOptionsPrepose.setSpacing(5);
		accordionOptionsPrepose.getPanes().addAll(tpGestionCatalogue,tpGestionAdherents,tpGestionPrets);
		vboxOptionsPrepose.getChildren().addAll(accordionOptionsPrepose,btnQuitterPrepose);
		
		
		Text txtRecherchePrepose = new Text("Rechercher par: ");
		
		ToggleGroup togglegroupPrepose = new ToggleGroup();
		RadioButton rbAuteurRealisateurPrepose = new RadioButton("Auteur/Réalisateur");
		RadioButton rbMotsClesPrepose = new RadioButton("Mots Clés");
		togglegroupPrepose.getToggles().addAll(rbAuteurRealisateur,rbMotsCles);
		
		TextField txtfldRecherchePrepose = new TextField();
		Button btnEffacerPrepose = new Button("Effacer");
		
		hboxOptionsRecherchePrepose.getChildren().addAll(txtRecherchePrepose,rbAuteurRealisateurPrepose,rbMotsClesPrepose,txtfldRecherchePrepose,btnEffacerPrepose);
		
		TableColumn<Document, String> ColonneNoDocPrepose = new TableColumn<Document, String>("Numéro Document");
		ColonneNoDocPrepose.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getNoDoc())); //Changer à comme les autres
		ColonneNoDocPrepose.setPrefWidth(120);
        TableColumn<Document, String> ColonneTitreDocPrepose = new TableColumn<Document, String>("Titre du Document");
        ColonneTitreDocPrepose.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        ColonneTitreDocPrepose.setPrefWidth(400);
        TableColumn<Document, LocalDate> ColonneDateParutionDocPrepose = new TableColumn<Document, LocalDate>("Date de parution");
        ColonneDateParutionDocPrepose.setCellValueFactory(new PropertyValueFactory<>("DateParution"));
        ColonneDateParutionDocPrepose.setPrefWidth(120);
        TableColumn<Document, String> ColonneDocDispoPrepose = new TableColumn<Document, String>("Disponibilité");
        ColonneDocDispoPrepose.setCellValueFactory(new PropertyValueFactory<>("Disponible"));
        ColonneDocDispoPrepose.setPrefWidth(120);
        TableColumn<Document, Integer> ColonneNbPretsDoc = new TableColumn<Document, Integer>("Nombre de prêts");
        ColonneNbPretsDoc.setCellValueFactory(new PropertyValueFactory<>("IntNbPrets"));
        ColonneNbPretsDoc.setPrefWidth(120);
        TableColumn<Document, String> ColonneEmprunteurDoc = new TableColumn<Document, String>("Emprunteur");
        ColonneEmprunteurDoc.setCellValueFactory(new PropertyValueFactory<>("StrEmprunteur"));
        ColonneEmprunteurDoc.setPrefWidth(120);
        
        tableDocumentsPrepose.getColumns().addAll(ColonneNoDocPrepose,ColonneTitreDocPrepose,ColonneDateParutionDocPrepose,ColonneDocDispoPrepose,ColonneNbPretsDoc,ColonneEmprunteurDoc);
		
        for (Document doc : Catalogue.getLstDocuments()) {
        	tableDocumentsPrepose.getItems().add(doc);
        }
		
        Tab tabCataloguePrepose = new Tab("Catalogue");
		tabCataloguePrepose.setClosable(false);
		tabCataloguePrepose.setGraphic(new ImageView("icon-collection.png"));
		tabCataloguePrepose.setContent(tableDocumentsPrepose);
		
		TableColumn<Livre, String> ColonneNoDocLivrePrepose = new TableColumn<Livre, String>("Numéro Document");
		ColonneNoDocLivrePrepose.setCellValueFactory(new PropertyValueFactory<>("NoDoc"));
		ColonneNoDocLivrePrepose.setPrefWidth(120);
        TableColumn<Livre, String> ColonneTitreLivrePrepose = new TableColumn<Livre, String>("Titre du livre");
        ColonneTitreLivrePrepose.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        ColonneTitreLivrePrepose.setPrefWidth(120);
        TableColumn<Livre, LocalDate> ColonneDateParutionLivrePrepose = new TableColumn<Livre, LocalDate>("Date de parution");
        ColonneDateParutionLivrePrepose.setCellValueFactory(new PropertyValueFactory<>("DateParution"));
        ColonneDateParutionLivrePrepose.setPrefWidth(120);
        TableColumn<Livre, String> ColonneLivreDispoPrepose = new TableColumn<Livre, String>("Disponibilité");
        ColonneLivreDispoPrepose.setCellValueFactory(new PropertyValueFactory<>("Disponible"));
        ColonneLivreDispoPrepose.setPrefWidth(120);
        TableColumn<Livre, String> ColonneAuteurLivrePrepose = new TableColumn<Livre, String>("Auteur");
        ColonneAuteurLivrePrepose.setCellValueFactory(new PropertyValueFactory<>("Auteur"));
        ColonneAuteurLivrePrepose.setPrefWidth(120);
        TableColumn<Livre, Integer> ColonneNbPretsLivre = new TableColumn<Livre, Integer>("Nombre de prêts");
        ColonneNbPretsLivre.setCellValueFactory(new PropertyValueFactory<>("IntNbPrets"));
        ColonneNbPretsLivre.setPrefWidth(120);
        TableColumn<Livre, String> ColonneEmprunteurLivre = new TableColumn<Livre, String>("Emprunteur");
        ColonneEmprunteurLivre.setCellValueFactory(new PropertyValueFactory<>("StrEmprunteur"));
        ColonneEmprunteurLivre.setPrefWidth(120);
        
        tableLivrePrepose.getColumns().addAll(ColonneNoDocLivrePrepose,ColonneTitreLivrePrepose,ColonneDateParutionLivrePrepose,ColonneLivreDispoPrepose,ColonneAuteurLivrePrepose,ColonneNbPretsLivre,ColonneEmprunteurLivre);
        
        for (Livre livre : Catalogue.getLstLivres()) {
        	tableLivrePrepose.getItems().add(livre);
        }
		
		Tab tabLivresPrepose = new Tab("Livres");
		tabLivresPrepose.setClosable(false);
		tabLivresPrepose.setGraphic(new ImageView("icon-livre.png"));
		tabLivresPrepose.setContent(tableLivrePrepose);
		
		TableColumn<DVD, String> ColonneNoDocDVDPrepose = new TableColumn<DVD, String>("Numéro Document");
		ColonneNoDocDVDPrepose.setCellValueFactory(new PropertyValueFactory<>("NoDoc"));
		ColonneNoDocDVDPrepose.setPrefWidth(120);
        TableColumn<DVD, String> ColonneTitreDVDPrepose = new TableColumn<DVD, String>("Titre du DVD");
        ColonneTitreDVDPrepose.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        ColonneTitreDVDPrepose.setPrefWidth(120);
        TableColumn<DVD, LocalDate> ColonneDateParutionDVDPrepose = new TableColumn<DVD, LocalDate>("Date de parution");
        ColonneDateParutionDVDPrepose.setCellValueFactory(new PropertyValueFactory<>("DateParution"));
        ColonneDateParutionDVDPrepose.setPrefWidth(120);
        TableColumn<DVD, String> ColonneDVDDispoPrepose = new TableColumn<DVD, String>("Disponibilité");
        ColonneDVDDispoPrepose.setCellValueFactory(new PropertyValueFactory<>("Disponible"));
        ColonneDVDDispoPrepose.setPrefWidth(120);
        TableColumn<DVD, Integer> ColonneNbDisquesPrepose = new TableColumn<DVD, Integer>("Nombre de disques");
        ColonneNbDisquesPrepose.setCellValueFactory(new PropertyValueFactory<>("NbDisques"));
        ColonneNbDisquesPrepose.setPrefWidth(120);
        TableColumn<DVD, String> ColonneRealisateurPrepose = new TableColumn<DVD, String>("Auteur");
        ColonneRealisateurPrepose.setCellValueFactory(new PropertyValueFactory<>("StrRealisateur"));
        ColonneRealisateurPrepose.setPrefWidth(120);
        TableColumn<DVD, Integer> ColonneNbPretsDVD = new TableColumn<DVD, Integer>("Nombre de prêts");
        ColonneNbPretsDVD.setCellValueFactory(new PropertyValueFactory<>("IntNbPrets"));
        ColonneNbPretsDVD.setPrefWidth(120);
        TableColumn<DVD, String> ColonneEmprunteurDVD = new TableColumn<DVD, String>("Emprunteur");
        ColonneEmprunteurDVD.setCellValueFactory(new PropertyValueFactory<>("StrEmprunteur"));
        ColonneEmprunteurDVD.setPrefWidth(120);
        
        tableDVDPrepose.getColumns().addAll(ColonneNoDocDVDPrepose,ColonneTitreDVDPrepose,ColonneDateParutionDVDPrepose,ColonneDVDDispoPrepose,ColonneNbDisquesPrepose,ColonneRealisateurPrepose,ColonneNbPretsDVD,ColonneEmprunteurDVD);
        
        for (DVD dvd : Catalogue.getLstDvd()) {
        	tableDVDPrepose.getItems().add(dvd);
        }
		
		Tab tabDVDPrepose = new Tab("DVD");
		tabDVDPrepose.setClosable(false);
		tabDVDPrepose.setGraphic(new ImageView("icon-dvd.png"));
		tabDVDPrepose.setContent(tableDVDPrepose);
		
		TableColumn<Periodique, String> ColonneNoDocPeriodiquePrepose = new TableColumn<Periodique, String>("Numéro Document");
		ColonneNoDocPeriodiquePrepose.setCellValueFactory(new PropertyValueFactory<>("NoDoc"));
		ColonneNoDocPeriodiquePrepose.setPrefWidth(120);
        TableColumn<Periodique, String> ColonneTitrePeriodiquePrepose = new TableColumn<Periodique, String>("Titre du Périodique");
        ColonneTitrePeriodiquePrepose.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        ColonneTitrePeriodiquePrepose.setPrefWidth(120);
        TableColumn<Periodique, LocalDate> ColonneDateParutionPeriodiquePrepose = new TableColumn<Periodique, LocalDate>("Date de parution");
        ColonneDateParutionPeriodiquePrepose.setCellValueFactory(new PropertyValueFactory<>("DateParution"));
        ColonneDateParutionPeriodiquePrepose.setPrefWidth(120);
        TableColumn<Periodique, String> ColonnePeriodiqueDispoPrepose = new TableColumn<Periodique, String>("Disponibilité");
        ColonnePeriodiqueDispoPrepose.setCellValueFactory(new PropertyValueFactory<>("Disponible"));
        ColonnePeriodiqueDispoPrepose.setPrefWidth(120);
        TableColumn<Periodique, Integer> ColonneNoVolumePrepose = new TableColumn<Periodique, Integer>("Numéro Volume");
        ColonneNoVolumePrepose.setCellValueFactory(new PropertyValueFactory<>("NoVolume"));
        ColonneNoVolumePrepose.setPrefWidth(120);
        TableColumn<Periodique, String> ColonneNoPeriodiquePrepose = new TableColumn<Periodique, String>("Numéro Périodique");
        ColonneNoPeriodiquePrepose.setCellValueFactory(new PropertyValueFactory<>("NoPeriodique"));
        ColonneNoPeriodiquePrepose.setPrefWidth(120);
        TableColumn<Periodique, Integer> ColonneNbPretsPeriodique = new TableColumn<Periodique, Integer>("Nombre de prêts");
        ColonneNbPretsPeriodique.setCellValueFactory(new PropertyValueFactory<>("IntNbPrets"));
        ColonneNbPretsPeriodique.setPrefWidth(120);
        TableColumn<Periodique, String> ColonneEmprunteurPeriodique = new TableColumn<Periodique, String>("Emprunteur");
        ColonneEmprunteurPeriodique.setCellValueFactory(new PropertyValueFactory<>("StrEmprunteur"));
        ColonneEmprunteurPeriodique.setPrefWidth(120);
        
        tablePeriodiquePrepose.getColumns().addAll(ColonneNoDocPeriodiquePrepose,ColonneTitrePeriodiquePrepose,ColonneDateParutionPeriodiquePrepose,ColonnePeriodiqueDispoPrepose,ColonneNoVolumePrepose,ColonneNoPeriodiquePrepose,ColonneNbPretsPeriodique,ColonneEmprunteurPeriodique);
        
        for (Periodique periodique : Catalogue.getLstPeriodiques()) {
        	tablePeriodiquePrepose.getItems().add(periodique);
        }
		
		Tab tabPeriodiquePrepose = new Tab("Périodiques");
		tabPeriodiquePrepose.setClosable(false);
		tabPeriodiquePrepose.setGraphic(new ImageView("icon-periodique.png"));
		tabPeriodiquePrepose.setContent(tablePeriodiquePrepose);
		
		rootPrepose.getTabs().addAll(tabCataloguePrepose,tabLivresPrepose,tabDVDPrepose,tabPeriodiquePrepose);
		
		btnAjouterDocument.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				ButtonType btnconfirmer = new ButtonType("Confirmer", ButtonData.YES);
				ButtonType btnannuler = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
				Alert fenetreAjouterDocument = new Alert(AlertType.NONE, "default Dialog",btnconfirmer,btnannuler);
				GridPane gridpaneLivre = new GridPane();
				GridPane gridpaneDVD = new GridPane();
				GridPane gridpanePeriodique = new GridPane();
				
				gridpaneLivre.setHgap(5);
				gridpaneDVD.setHgap(5);
				gridpanePeriodique.setHgap(5);
				
				//Button btConfirmer = (Button) fenetreAjouterDocument.getDialogPane().lookupButton(btnconfirmer);
				Text TypeDoc = new Text("Type de document:");
				
				ComboBox<String> cbxTypeDoc = new ComboBox<String>();
				cbxTypeDoc.getItems().addAll("Livre","DVD","Périodique");
				cbxTypeDoc.getSelectionModel().selectFirst();
				
				Text Titre = new Text("Titre:");
				TextField champsTitre = new TextField("");
				
				TextField champsDateParution = new TextField("");
				Text DateParution = new Text("Date de parution:");
				
				Text AuteurLivre = new Text("Auteur:");
				TextField champsAuteurLivre = new TextField();
				
				Text MotsClesLivre = new Text("Mots Clés:");
				TextField champsMotsClesLivre = new TextField();
				
				gridpaneLivre.add(TypeDoc, 0, 0);
				gridpaneLivre.add(cbxTypeDoc, 1, 0);
				gridpaneLivre.add(Titre, 0, 1);
				gridpaneLivre.add(champsTitre, 1, 1);
				gridpaneLivre.add(AuteurLivre, 0, 2);
				gridpaneLivre.add(champsAuteurLivre, 1, 2);
				gridpaneLivre.add(DateParution, 0, 3);
				gridpaneLivre.add(champsDateParution, 1, 3);
				gridpaneLivre.add(MotsClesLivre, 0, 4);
				gridpaneLivre.add(champsMotsClesLivre, 1, 4);
				
//				Text TitreDVD = new Text("Titre:");
//				TextField champsTitreDVD = new TextField();
//				
//				Text DateParutionDVD = new Text("Date de parution:");
//				TextField champsDateParutionDVD = new TextField("");
				
				Text NbDisques = new Text("Nombre de disques:");
				TextField champsNbDisques = new TextField();
				
				Text Realisateur = new Text("Réalisateur:");
				TextField champsRealisateur = new TextField();
				
				
//				Text TitrePeriodique = new Text("Titre:");
//				TextField champsTitrePeriodique = new TextField();
//				
//				Text DateParutionPeriodique = new Text("Date de parution:");
//				TextField champsDateParutionPeriodique = new TextField();	
				
				Text NoPeriodique = new Text("Numéro de périodique:");
				TextField champsNoPeriodique = new TextField();
				
				Text NoVolume = new Text("Numéro de volume:");
				TextField champsNoVolume = new TextField();
				
				cbxTypeDoc.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						
						if (cbxTypeDoc.getValue() == "Livre") {
							gridpaneDVD.getChildren().clear();
							gridpanePeriodique.getChildren().clear();
							gridpaneLivre.getChildren().clear();
							
							gridpaneLivre.add(TypeDoc, 0, 0);
							gridpaneLivre.add(cbxTypeDoc, 1, 0);
							gridpaneLivre.add(Titre, 0, 1);
							gridpaneLivre.add(champsTitre, 1, 1);
							gridpaneLivre.add(AuteurLivre, 0, 2);
							gridpaneLivre.add(champsAuteurLivre, 1, 2);
							gridpaneLivre.add(DateParution, 0, 3);
							gridpaneLivre.add(champsDateParution, 1, 3);
							gridpaneLivre.add(MotsClesLivre, 0, 4);
							gridpaneLivre.add(champsMotsClesLivre, 1, 4);
							
							fenetreAjouterDocument.getDialogPane().setContent(gridpaneLivre);
//							verifierEtAjouterDocument(fenetreAjouterDocument, btnconfirmer, champsTitreLivre, champsAuteurLivre, champsDateParutionLivre, champsMotsClesLivre, cbxTypeDoc, champsNbDisques, champsRealisateur, champsNoPeriodique, champsNoVolume);
							
							
						}
						else if (cbxTypeDoc.getValue() == "DVD" ) {
							gridpaneDVD.getChildren().clear();
							gridpanePeriodique.getChildren().clear();
							gridpaneLivre.getChildren().clear();
							
							gridpaneDVD.add(TypeDoc, 0, 0);
							gridpaneDVD.add(cbxTypeDoc, 1, 0);
							gridpaneDVD.add(Titre, 0, 1);
							gridpaneDVD.add(champsTitre, 1, 1);
							gridpaneDVD.add(DateParution, 0, 2);
							gridpaneDVD.add(champsDateParution, 1, 2);
							gridpaneDVD.add(NbDisques, 0, 3);
							gridpaneDVD.add(champsNbDisques, 1, 3);
							gridpaneDVD.add(Realisateur, 0, 4);
							gridpaneDVD.add(champsRealisateur, 1, 4);
							
							fenetreAjouterDocument.getDialogPane().setContent(gridpaneDVD);
//							verifierEtAjouterDocument(fenetreAjouterDocument, btnconfirmer, champsTitreDVD, champsAuteurLivre, champsDateParutionDVD, champsMotsClesLivre, cbxTypeDoc, champsNbDisques, champsRealisateur, champsNoPeriodique, champsNoVolume);
							
						}
						else if (cbxTypeDoc.getValue() == "Périodique") {
							gridpaneDVD.getChildren().clear();
							gridpanePeriodique.getChildren().clear();
							gridpaneLivre.getChildren().clear();
							
							gridpanePeriodique.add(TypeDoc, 0, 0);
							gridpanePeriodique.add(cbxTypeDoc, 1, 0);
							gridpanePeriodique.add(Titre, 0, 1);
							gridpanePeriodique.add(champsTitre, 1, 1);
							gridpanePeriodique.add(DateParution, 0, 2);
							gridpanePeriodique.add(champsDateParution, 1, 2);
							gridpanePeriodique.add(NoPeriodique, 0, 3);
							gridpanePeriodique.add(champsNoPeriodique, 1, 3);
							gridpanePeriodique.add(NoVolume, 0, 4);
							gridpanePeriodique.add(champsNoVolume, 1, 4);
							
							fenetreAjouterDocument.getDialogPane().setContent(gridpanePeriodique);
//							verifierEtAjouterDocument(fenetreAjouterDocument, btnconfirmer, champsTitrePeriodique, champsAuteurLivre, champsDateParutionPeriodique, champsMotsClesLivre, cbxTypeDoc, champsNbDisques, champsRealisateur, champsNoPeriodique, champsNoVolume );
							
						}
					}});
				
				fenetreAjouterDocument.setTitle("Ajouter un document");
				Stage stage = (Stage) fenetreAjouterDocument.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("icon-ajouterdocument.png"));
				fenetreAjouterDocument.setHeaderText(null);
				fenetreAjouterDocument.getDialogPane().setContent(gridpaneLivre);
				
				verifierEtAjouterDocument(fenetreAjouterDocument, btnconfirmer, champsTitre, champsAuteurLivre, champsDateParution, champsMotsClesLivre, cbxTypeDoc, champsNbDisques, champsRealisateur, champsNoPeriodique, champsNoVolume);
				
//				Button btConfirmer = (Button) fenetreAjouterDocument.getDialogPane().lookupButton(btnconfirmer);
//				btConfirmer.setOnMouseClicked(new EventHandler<MouseEvent>() {
//					@Override
//					public void handle(MouseEvent e) {
//						if (cbxTypeDoc.getValue() == "Livre") {
//							verifierEtAjouterDocument(fenetreAjouterDocument, btnconfirmer, champsTitreLivre, champsAuteurLivre, champsDateParutionLivre, champsMotsClesLivre, cbxTypeDoc, champsNbDisques, champsRealisateur, champsNoPeriodique, champsNoVolume);
//						}
//						else if (cbxTypeDoc.getValue() == "DVD") {
//							verifierEtAjouterDocument(fenetreAjouterDocument, btnconfirmer, champsTitreDVD, champsAuteurLivre, champsDateParutionDVD, champsMotsClesLivre, cbxTypeDoc, champsNbDisques, champsRealisateur, champsNoPeriodique, champsNoVolume);
//						}
//						else if (cbxTypeDoc.getValue() == "Périodique") {
//							verifierEtAjouterDocument(fenetreAjouterDocument, btnconfirmer, champsTitrePeriodique, champsAuteurLivre, champsDateParutionPeriodique, champsMotsClesLivre, cbxTypeDoc, champsNbDisques, champsRealisateur, champsNoPeriodique, champsNoVolume );
//
//						}
//					}});

				
				Button btAnnuler = (Button) fenetreAjouterDocument.getDialogPane().lookupButton(btnannuler); 
				btAnnuler.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						fenetreAjouterDocument.close();
					}});
				
				fenetreAjouterDocument.showAndWait();
			}});
		
		//Fenêtre d'ajout de préposé(s)
        btnAjouterPrepose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				ButtonType btnconfirmer = new ButtonType("Confirmer", ButtonData.YES);
				ButtonType btnannuler = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
				Alert fenetreAjouterPrepose = new Alert(AlertType.NONE, "default Dialog",btnconfirmer,btnannuler);
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
				
				TextField champsNom = new TextField("a");
				TextField champsPrenom = new TextField("a");
				TextField champsAdresse = new TextField("a");
				TextField champsTelephone = new TextField("(123) 456-7891");
				TextField champsMotDePasse = new TextField("a");
				
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
				
				
				verifierInfosPrepose(fenetreAjouterPrepose, btnconfirmer, champsNom, champsPrenom, champsAdresse, champsTelephone, champsMotDePasse);
					
				Button btAnnuler = (Button) fenetreAjouterPrepose.getDialogPane().lookupButton(btnannuler); 
				btAnnuler.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						fenetreAjouterPrepose.close();
					}});
				
				fenetreAjouterPrepose.showAndWait();
			}});
        
        btnAjouterAdherent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				ButtonType btnconfirmer = new ButtonType("Confirmer", ButtonData.YES);
				ButtonType btnannuler = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
				Alert fenetreAjouterAdherent = new Alert(AlertType.NONE, "default Dialog",btnconfirmer,btnannuler);
				GridPane gridpane = new GridPane();
				gridpane.setHgap(5);
				
				Button btConfirmer = (Button) fenetreAjouterAdherent.getDialogPane().lookupButton(btnconfirmer);
				Button btAnnuler = (Button) fenetreAjouterAdherent.getDialogPane().lookupButton(btnannuler);
				
				Text nom = new Text("Nom:");
				Text prenom = new Text("Prénom:");
				Text adresse = new Text("Adresse:");
				Text telephone = new Text("Téléphone:");
				
				gridpane.add(nom, 0, 0);
				gridpane.add(prenom, 0, 1);
				gridpane.add(adresse, 0, 2);
				gridpane.add(telephone, 0, 3);
				
				TextField champsNom = new TextField("a");
				TextField champsPrenom = new TextField("a");
				TextField champsAdresse = new TextField("a");
				TextField champsTelephone = new TextField("(123) 456-7891");
				
				gridpane.add(champsNom, 1, 0);
				gridpane.add(champsPrenom, 1, 1);
				gridpane.add(champsAdresse, 1, 2);
				gridpane.add(champsTelephone, 1, 3);
				
				fenetreAjouterAdherent.setTitle("Ajouter un adhérent");
				Stage stage = (Stage) fenetreAjouterAdherent.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("icon-ajouteradherent.png"));
				fenetreAjouterAdherent.setHeaderText(null);
				fenetreAjouterAdherent.getDialogPane().setContent(gridpane);
				
				verifierEtAjouterAdherent(fenetreAjouterAdherent, btnconfirmer, champsNom, champsPrenom, champsAdresse, champsTelephone);
				
				fenetreAjouterAdherent.showAndWait();
				
				btAnnuler.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						fenetreAjouterAdherent.close();
					}});
				
				
			}});
        
        btnConnexion.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!rbAdmin.isSelected() && !rbPrepose.isSelected()) {
					Alert fenetreTypeUtilisateur = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					
					fenetreTypeUtilisateur.setTitle("Avertissement");
					Stage stage = (Stage) fenetreTypeUtilisateur.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-avertissement.png"));
					fenetreTypeUtilisateur.setContentText("Veuillez sélectionner le type d'utilisateur.");
					fenetreTypeUtilisateur.setHeaderText(null);
					fenetreTypeUtilisateur.showAndWait();
				}
				
				if (txtfldNoEmploye.getText().trim().isEmpty()) {
					Alert fenetreNoEmployeManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					
					fenetreNoEmployeManquant.setTitle("Erreur");
					Stage stage = (Stage) fenetreNoEmployeManquant.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-erreur.png"));
					fenetreNoEmployeManquant.setContentText("Vous avez oublié d'inscrire le numéro d'employé.");
					fenetreNoEmployeManquant.setHeaderText(null);
					fenetreNoEmployeManquant.showAndWait();
				}
				
				if (pwfldMotDePasse.getText().trim().isEmpty()) {
					Alert fenetreMotDePasseManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					
					fenetreMotDePasseManquant.setTitle("Erreur");
					Stage stage = (Stage) fenetreMotDePasseManquant.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-erreur.png"));
					fenetreMotDePasseManquant.setContentText("Vous avez oublié d'inscrire le mot de passe.");
					fenetreMotDePasseManquant.setHeaderText(null);
					fenetreMotDePasseManquant.showAndWait();
				}
				
				if (rbAdmin.isSelected()) {
					if (txtfldNoEmploye.getText().equals(lstIdentifiants.get(0).getStrIdentifiant()) && pwfldMotDePasse.getText().equals(lstIdentifiants.get(0).getStrMotDePasse())) {
						arg0.setScene(sceneAdmin);
						arg0.setTitle("Gérer les préposés");
					}
					else {
						Alert fenetreIdentifiantManquantsIncorrects = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
						
						fenetreIdentifiantManquantsIncorrects.setTitle("Avertissement");
						Stage stage = (Stage) fenetreIdentifiantManquantsIncorrects.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("icon-avertissement.png"));
						fenetreIdentifiantManquantsIncorrects.setContentText("Les identifiants que vous avez entrés ne correspondent pas à un administrateur de la médiathèque.");
						fenetreIdentifiantManquantsIncorrects.setHeaderText(null);
						fenetreIdentifiantManquantsIncorrects.showAndWait();
					}
				}
				else if(rbPrepose.isSelected()) {
					int intNbIdentifiant = lstIdentifiants.size();
					
					for (int i=lstIdentifiants.size(); i>0; i--) {
						intNbIdentifiant--;
						if (txtfldNoEmploye.getText().equals(lstIdentifiants.get(i-1).getStrIdentifiant())) {
							if (pwfldMotDePasse.getText().equals(lstIdentifiants.get(i-1).getStrMotDePasse())) {
								arg0.setScene(scenePrepose);
								arg0.setTitle("Gèrer les adhérents");
								break;
							}
							else {
								Alert fenetreMotDePasseManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
								
								fenetreMotDePasseManquant.setTitle("Erreur");
								Stage stage = (Stage) fenetreMotDePasseManquant.getDialogPane().getScene().getWindow();
								stage.getIcons().add(new Image("icon-erreur.png"));
								fenetreMotDePasseManquant.setContentText("Le mot de passe ou l'identifiant entré sont incorrects.");
								fenetreMotDePasseManquant.setHeaderText(null);
								fenetreMotDePasseManquant.showAndWait();
							}
						}
					}
					if (intNbIdentifiant <= 0) {
						Alert fenetreIdentifiantManquantsIncorrects = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
						
						fenetreIdentifiantManquantsIncorrects.setTitle("Avertissement");
						Stage stage = (Stage) fenetreIdentifiantManquantsIncorrects.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("icon-avertissement.png"));
						fenetreIdentifiantManquantsIncorrects.setContentText("Les identifiants que vous avez entrés ne correspondent à aucun employés.");
						fenetreIdentifiantManquantsIncorrects.setHeaderText(null);
						fenetreIdentifiantManquantsIncorrects.showAndWait();
					}
				}
				
			}});
        
        btnConsulterCatalogue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				arg0.setTitle("Médiathèque");
				arg0.setScene(sceneCatalogue);
			}});
        
        arg0.setOnCloseRequest(event ->{
        	SerialisationPreposes sP = new SerialisationPreposes();
        	sP.Serialiser();
        	
        	SerialisationCatalogue sC = new SerialisationCatalogue();
        	sC.SerialiserDocuments();
        	sC.SerialiserDVD();
        	sC.SerialiserLivres();
        	sC.SerialiserPeriodiques();
        	
        	SerialisationAdherents sA = new SerialisationAdherents();
        	sA.Serialiser();
        });
        
        btnQuitterCatalogue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				arg0.setTitle("Identification");
				arg0.setScene(sceneIdentification);
			}});
        
        btnQuitterPrepose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				arg0.setTitle("Identification");
				arg0.setScene(sceneIdentification);
			}});
		
		arg0.setResizable(false);
		arg0.setScene(sceneIdentification);
		arg0.show();
		
	}
	
	//Ajout du nouveau preposé au fichier des identifiants et dans le TableView
	private void ajouterPrepose(Prepose prepose, IdentifiantsPrepose identifiants) { 
		try {
			File file = new File("Identifiants préposé.txt");
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"UTF-8"));
			
			bw.write("Préposé");
			bw.newLine();
			bw.write("Identifiant: " + identifiants.getStrIdentifiant());
			bw.newLine();
			bw.write("Mot de passe: " + identifiants.getStrMotDePasse());
			bw.newLine();
			bw.write("\r\n");
			bw.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void supprimerPrepose(Prepose prepose, String strMotDePasse) {
		try {
			
			tablePreposes.getItems().add(prepose);
			File inputFile = new File("Identifiants préposé.txt");
			File tempFile = new File("préposéTemp.txt");

			Scanner sc1 = new Scanner(inputFile);
			
			//Encodage en UTF-8 sinon les caractères accentués ne sont pas lus correctement
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8")); 
			
			while (sc1.nextLine()!= null) {
				String strLigneS = sc1.nextLine();
				if (strLigneS == "Identifiant: " + prepose.getStrNoEmploye()) {
					System.out.println("test");
				}
			}
			
			writer.close();
			sc1.close();
				
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	private void verifierEtAjouterAdherent(Alert fenetreAjouterAdherent, ButtonType btnconfirmer,TextField champsNom,TextField champsPrenom,TextField champsAdresse,TextField champsTelephone) {
		
		Button btConfirmer = (Button) fenetreAjouterAdherent.getDialogPane().lookupButton(btnconfirmer);
		btConfirmer.addEventFilter(ActionEvent.ACTION, event ->{
			if (champsNom.getText().trim().isEmpty()) {
				Alert fenetreNomManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreNomManquant.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreNomManquant.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreNomManquant.setContentText("Vous avez oublié d'entrer le nom.");
				fenetreNomManquant.setHeaderText(null);
				fenetreNomManquant.showAndWait();
				event.consume();
			}
			else if (champsPrenom.getText().trim().isEmpty()) {
				Alert fenetrePrenomManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetrePrenomManquant.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetrePrenomManquant.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetrePrenomManquant.setContentText("Vous avez oublié d'entrer le prénom.");
				fenetrePrenomManquant.setHeaderText(null);
				fenetrePrenomManquant.showAndWait();
				event.consume();
			}
			else if (champsAdresse.getText().trim().isEmpty())  {
				Alert fenetreAdresseManquante = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreAdresseManquante.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreAdresseManquante.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreAdresseManquante.setContentText("Vous avez oublié d'entrer l'adresse.");
				fenetreAdresseManquante.setHeaderText(null);
				fenetreAdresseManquante.showAndWait();
				event.consume();
			}
			else if (champsTelephone.getText().trim().isEmpty()) {
				Alert fenetreTelephoneManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreTelephoneManquant.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreTelephoneManquant.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreTelephoneManquant.setContentText("Vous avez oublié d'entrer le numéro de téléphone.");
				fenetreTelephoneManquant.setHeaderText(null);
				fenetreTelephoneManquant.showAndWait();
				event.consume();
			}
			else if (!champsTelephone.getText().matches("^\\(\\d{3}\\)\\s\\d{3}-\\d{4}")) {
				Alert fenetreTelephoneIncorrect = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreTelephoneIncorrect.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreTelephoneIncorrect.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreTelephoneIncorrect.setContentText("Le format du numéro de téléphone entré est incorrect. Le format est \"(###) ###-####\"."  );
				fenetreTelephoneIncorrect.setHeaderText(null);
				fenetreTelephoneIncorrect.showAndWait();
				event.consume();
			}
			else {
				Adherent adherent = new Adherent("ADH"+intNbAdherent, champsNom.getText().trim(), champsPrenom.getText().trim(), champsAdresse.getText(), champsTelephone.getText().trim(), 0, 0.00);
				lstAdherents.add(adherent);
				tableAdherent.getItems().add(adherent);
				
				intNbAdherent++;
			}
			
		});
	}
	
	private void verifierEtAjouterDocument(Alert fenetreAjouterDocument, ButtonType btnconfirmer, TextField champsTitre,TextField champsAuteur,TextField champsDateParution,TextField champsMotsCles, ComboBox<String> cbxTypeDoc,TextField champsNbDisques,TextField champsRealisateur,TextField champsNoPeriodique,TextField champsNoVolume) {
		Button btConfirmer = (Button) fenetreAjouterDocument.getDialogPane().lookupButton(btnconfirmer);
		btConfirmer.addEventFilter(ActionEvent.ACTION, event->{
			if (champsTitre.getText().trim().isEmpty()) {
				Alert fenetreTitreManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreTitreManquant.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreTitreManquant.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreTitreManquant.setContentText("Veuillez entrer le titre du document.");
				fenetreTitreManquant.setHeaderText(null);
				fenetreTitreManquant.showAndWait();
				event.consume();
			}
			else if (champsAuteur.getText().trim().isEmpty()) {
				Alert fenetreAuteurManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreAuteurManquant.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreAuteurManquant.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreAuteurManquant.setContentText("Veuillez entrer l'auteur du document.");
				fenetreAuteurManquant.setHeaderText(null);
				fenetreAuteurManquant.showAndWait();
				event.consume();
			}
			else if (champsDateParution.getText().trim().isEmpty()) {
				Alert fenetreDateParutionManquante = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreDateParutionManquante.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreDateParutionManquante.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreDateParutionManquante.setContentText("Vous avez oublié d'entrer l'adresse.");
				fenetreDateParutionManquante.setHeaderText(null);
				fenetreDateParutionManquante.showAndWait();
				event.consume();
			}
			else if (!champsDateParution.getText().trim().matches("^(?:(?:31(-)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(-)(?:0?[13-9]|1[0-2])\\2))"
					+ "(?:(?:1[6-9]|[2-9]\\d)\\d{2})$|^(?:29(-)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)(?:0[48]|[2468][048]|[13579][26])|"
					+ "(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(-)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)\\d{2})$")) 
			{
				Alert fenetreTelephoneIncorrect = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreTelephoneIncorrect.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreTelephoneIncorrect.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreTelephoneIncorrect.setContentText("La date que vous avez entré ou son format sont incorrects. Le format est \"JJ-MM-AAAA\"."  );
				fenetreTelephoneIncorrect.setHeaderText(null);
				fenetreTelephoneIncorrect.showAndWait();
				event.consume();
			}
			else if (champsMotsCles.getText().trim().isEmpty()) {
				Alert fenetreMotsClesManquants = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreMotsClesManquants.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreMotsClesManquants.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreMotsClesManquants.setContentText("Vous avez oublié d'entrer le numéro de téléphone.");
				fenetreMotsClesManquants.setHeaderText(null);
				fenetreMotsClesManquants.showAndWait();
				event.consume();
			}
			else {
				if (cbxTypeDoc.getValue() == "Livre") {
					
					Livre livre = new Livre("Liv"+intNbLivre, champsTitre.getText(), LocalDate.parse(champsDateParution.getText().trim(), Catalogue.getDf()), "Disponible", champsMotsCles.getText(), champsAuteur.getText().trim(), 0, "");
					Catalogue.getLstLivres().add(livre);
					Catalogue.getLstDocuments().add(livre);
					
					tableLivre.getItems().add(livre);
					tableLivrePrepose.getItems().add(livre);
					
					tableDocuments.getItems().add(livre);
					tableDocumentsPrepose.getItems().add(livre);
					
					intNbLivre++;
					
					try {
						File file = new File("Livres.txt");
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"UTF-8"));
						
						bw.write("Liv"+intNbLivre+ "," + champsTitre.getText() + "," + champsDateParution.getText().trim() + "," + champsAuteur.getText().trim());
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
				}
				else if (cbxTypeDoc.getValue() == "DVD") {
					
					DVD dvd = new DVD("DVD"+intNbDVD, champsTitre.getText(), LocalDate.parse(champsDateParution.getText().trim(), Catalogue.getDf()), "Disponible", Integer.parseInt(champsNbDisques.getText().trim()), champsRealisateur.getText().trim(), 0, "");
					Catalogue.getLstDvd().add(dvd);
					Catalogue.getLstDocuments().add(dvd);
					
					tableDVD.getItems().add(dvd);
					tableDVDPrepose.getItems().add(dvd);
					
					tableDocuments.getItems().add(dvd);
					tableDocumentsPrepose.getItems().add(dvd);
					
					intNbDVD++;
					
					try {
						File file = new File("DVD.txt");
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"UTF-8"));
						
						bw.write("DVD"+intNbDVD+ "," + champsTitre.getText() + "," + champsDateParution.getText().trim() + "," + champsNbDisques.getText() + "," + champsRealisateur.getText());
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				else if (cbxTypeDoc.getValue() == "Périodique") {
					Periodique periodique = new Periodique("Per"+ intNbPeriodique, champsTitre.getText(), LocalDate.parse(champsDateParution.getText().trim(), Catalogue.getDf()), "Disponible", Integer.parseInt(champsNoVolume.getText().trim()), Integer.parseInt(champsNoPeriodique.getText().trim()), 0, "");
					Catalogue.getLstPeriodiques().add(periodique);
					Catalogue.getLstDocuments().add(periodique);
					
					tablePeriodique.getItems().add(periodique);
					tablePeriodiquePrepose.getItems().add(periodique);
					
					tableDocuments.getItems().add(periodique);
					tableDocumentsPrepose.getItems().add(periodique);
					
					intNbPeriodique++;
					
					try {
						File file = new File("Periodiques.txt");
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"UTF-8"));
						
						bw.write("DVD"+intNbPeriodique+ "," + champsTitre.getText() + "," + champsDateParution.getText().trim() + "," + champsNoVolume.getText().trim() + "," + champsNoPeriodique.getText().trim());
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void verifierInfosPrepose(Alert fenetreAjouterPrepose, ButtonType btnconfirmer,TextField champsNom,TextField champsPrenom,TextField champsAdresse,TextField champsTelephone,TextField champsMotDePasse ) {
		//Vérification si tout les éléments sont entrés. EventFilter et consommation de l'événement en lambda sinon la fenêtre en arrière-plan se ferme.
		Button btConfirmer = (Button) fenetreAjouterPrepose.getDialogPane().lookupButton(btnconfirmer);
		btConfirmer.addEventFilter(ActionEvent.ACTION, event->{
			if (champsNom.getText().trim().isEmpty()) {
				Alert fenetreNomManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreNomManquant.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreNomManquant.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreNomManquant.setContentText("Vous avez oublié d'entrer le nom.");
				fenetreNomManquant.setHeaderText(null);
				fenetreNomManquant.showAndWait();
				event.consume();
			}
			else if (champsPrenom.getText().trim().isEmpty()) {
				Alert fenetrePrenomManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetrePrenomManquant.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetrePrenomManquant.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetrePrenomManquant.setContentText("Vous avez oublié d'entrer le prénom.");
				fenetrePrenomManquant.setHeaderText(null);
				fenetrePrenomManquant.showAndWait();
				event.consume();
			}
			else if (champsAdresse.getText().trim().isEmpty()) {
				Alert fenetreAdresseManquante = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreAdresseManquante.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreAdresseManquante.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreAdresseManquante.setContentText("Vous avez oublié d'entrer l'adresse.");
				fenetreAdresseManquante.setHeaderText(null);
				fenetreAdresseManquante.showAndWait();
				event.consume();
			}
			else if (champsTelephone.getText().trim().isEmpty()) {
				Alert fenetreTelephoneManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreTelephoneManquant.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreTelephoneManquant.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreTelephoneManquant.setContentText("Vous avez oublié d'entrer le numéro de téléphone.");
				fenetreTelephoneManquant.setHeaderText(null);
				fenetreTelephoneManquant.showAndWait();
				event.consume();
			}
			else if (!champsTelephone.getText().matches("^\\(\\d{3}\\)\\s\\d{3}-\\d{4}")) {
				Alert fenetreTelephoneIncorrect = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreTelephoneIncorrect.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreTelephoneIncorrect.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreTelephoneIncorrect.setContentText("Le format du numéro de téléphone entré est incorrect. Le format est \"(###) ###-####\"."  );
				fenetreTelephoneIncorrect.setHeaderText(null);
				fenetreTelephoneIncorrect.showAndWait();
				event.consume();
			}
			else if (champsMotDePasse.getText().trim().isEmpty()) {
				Alert fenetreMotdePasseManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreMotdePasseManquant.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreMotdePasseManquant.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreMotdePasseManquant.setContentText("Vous avez oublié d'entrer le mot de passe.");
				fenetreMotdePasseManquant.setHeaderText(null);
				fenetreMotdePasseManquant.showAndWait();
				event.consume();
			}
			else {
				Prepose prepose = new Prepose("EMP" + intNbEmploye, champsNom.getText(), champsPrenom.getText(), champsAdresse.getText(), champsTelephone.getText());
				intNbEmploye++;
				IdentifiantsPrepose identifiants = new IdentifiantsPrepose("Préposé", prepose.getStrNoEmploye(), champsMotDePasse.getText());
				lstIdentifiants.add(identifiants);
				lstPrepose.add(prepose);
				tablePreposes.getItems().add(prepose);
				ajouterPrepose(prepose, identifiants);
				
			}
			
		});
	}

	public static ArrayList<Prepose> getLstPrepose() {
		return lstPrepose;
	}

	public static TableView<Prepose> getTablePreposes() {
		return tablePreposes;
	}

	public static TableView<Document> getTableDocuments() {
		return tableDocuments;
	}

	public static TableView<Livre> getTableLivre() {
		return tableLivre;
	}

	public static TableView<DVD> getTableDVD() {
		return tableDVD;
	}

	public static TableView<Periodique> getTablePeriodique() {
		return tablePeriodique;
	}
	
	public static ArrayList<Adherent> getLstAdherents() {
		return lstAdherents;
	}

	public static void setIntNbLivre(int intNbLivre) {
		InterfaceGraphique.intNbLivre = intNbLivre;
	}

	public static void setIntNbDVD(int intNbDVD) {
		InterfaceGraphique.intNbDVD = intNbDVD;
	}

	public static void setIntNbPeriodique(int intNbPeriodique) {
		InterfaceGraphique.intNbPeriodique = intNbPeriodique;
	}

	public static void setIntNbAdherent(int intNbAdherent) {
		InterfaceGraphique.intNbAdherent = intNbAdherent;
	}

	
	
}
