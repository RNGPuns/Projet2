package application;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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
import donnees.Pret;
import donnees.SerialisationAdherents;
import donnees.SerialisationCatalogue;
import donnees.SerialisationPreposes;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.control.TableCell;
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

//Jerome Labrosse et Andrew Aboujaoudé

public class InterfaceGraphique extends Application {
	//Table du catalogue
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
	
	private final static TableView<Document> tableDocumentsEmpruntes = new TableView<Document>();
	private final static TableView<Pret> tablePretAdherent = new TableView<Pret>();
	
	ArrayList<String> lstLignes = new ArrayList<String>();
	ArrayList<String> lstLignesTemp = new ArrayList<String>();
	ArrayList<IdentifiantsPrepose> lstIdentifiants = new ArrayList<IdentifiantsPrepose>();
	static ArrayList<Prepose> lstPrepose = new ArrayList<Prepose>();
	static ArrayList<Adherent> lstAdherents = new ArrayList<Adherent>();
	
	String strIdentifiant = "";
	String strMotDePasse = "";
	
	int intNbEmploye = 1;
	int intNbPrets = 1;
	
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
				String strTypeUtilisateur = scFichierIdentifiant.nextLine(); //Caractère bizzare pour aucune raison. substring pour les enlever.
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
		
		Scene sceneIdentification = new Scene(rootIdentification,350,350);
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
		
		Text infoNoTelephone = new Text("Numéro de téléphone :");
		TextField noTelephone = new TextField();
		
		//Changement des textfields
		rbNoTelephone.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (rbNomPrenom.isSelected()) {
					panneauInfosAdherent.getChildren().remove(infoNom);
					panneauInfosAdherent.getChildren().remove(txtfldNom);
					panneauInfosAdherent.getChildren().remove(infoPrenom);
					panneauInfosAdherent.getChildren().remove(txtfldPrenom);
				}
				
				panneauInfosAdherent.add(infoNoTelephone, 1, 6);
				panneauInfosAdherent.add(noTelephone, 2, 6);
				
			}});
		
		//Changement des textfields
		rbNomPrenom.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (rbNoTelephone.isSelected()) {
					panneauInfosAdherent.getChildren().remove(infoNoTelephone);
					panneauInfosAdherent.getChildren().remove(noTelephone);
				}
				
				panneauInfosAdherent.add(infoNom, 1, 4);
				panneauInfosAdherent.add(txtfldNom, 2, 4);
				panneauInfosAdherent.add(infoPrenom, 1, 8);
				panneauInfosAdherent.add(txtfldPrenom, 2, 8);
				
			}});
				
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
		TextField txtfldNoEmploye = new TextField();

		
		panneauConnexion.add(infoNoEmploye, 1, 0);
		panneauConnexion.add(txtfldNoEmploye, 2, 0);
		
		//Rangée #2
		Label infoMotDePasse = new Label("Mot de passe :");
		PasswordField pwfldMotDePasse = new PasswordField();
		
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
		panneauDossierAdherent.setHgap(24); //**Ne pas modifier svp**
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
		
		Text infoNoTelephoneAdherent = new Text("Numéro de téléphone :");
		TextField noTelephoneAdherent = new TextField();
		
		//Changement des textefields
		rbNoTelephoneAdherent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (rbNomPrenomAdherent.isSelected()) {
					panneauDossierAdherent.getChildren().remove(infoNomAdherent);
					panneauDossierAdherent.getChildren().remove(txtfldNomAdherent);
					panneauDossierAdherent.getChildren().remove(infoPrenomAdherent);
					panneauDossierAdherent.getChildren().remove(txtfldPrenomAdherent);
				}
				
				panneauDossierAdherent.add(infoNoTelephoneAdherent, 1, 6);
				panneauDossierAdherent.add(noTelephoneAdherent, 2, 6);
				
			}});
		
		//Changements des textfields
		rbNomPrenomAdherent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (rbNoTelephone.isSelected()) {
					panneauDossierAdherent.getChildren().remove(infoNoTelephoneAdherent);
					panneauDossierAdherent.getChildren().remove(noTelephoneAdherent);
				}
				
				panneauDossierAdherent.add(infoNomAdherent, 1, 4);
				panneauDossierAdherent.add(txtfldNomAdherent, 2, 4);
				panneauDossierAdherent.add(infoPrenomAdherent, 1, 8);
				panneauDossierAdherent.add(txtfldPrenomAdherent, 2, 8);
				
				
			}});
		
		//Quitter vers identification
		Button btnQuitterCatalogue = new Button("Quitter");
		btnQuitterCatalogue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				arg0.setScene(sceneIdentification);
				arg0.setTitle("Identification");
			}});
		
		vboxDossierAdherent.getChildren().add(btnQuitterCatalogue);
		vboxDossierAdherent.setAlignment(Pos.TOP_CENTER);
		
		hboxOptionsRecherche.setPadding(new Insets(7,10,7,10));
		hboxOptionsRecherche.setSpacing(10);
		
		Text txtRecherche = new Text("Rechercher par: ");
		
		ToggleGroup togglegroupOptionsRechercheAdherent = new ToggleGroup();
		RadioButton rbTitre = new RadioButton("Titre");
		RadioButton rbAuteurRealisateur = new RadioButton("Auteur/Réalisateur");
		RadioButton rbMotsCles = new RadioButton("Mots Clés");
		togglegroupOptionsRechercheAdherent.getToggles().addAll(rbTitre,rbAuteurRealisateur,rbMotsCles);
		
		TextField txtfldRecherche = new TextField();
		Button btnEffacer = new Button("Effacer");
		
		hboxOptionsRecherche.getChildren().addAll(txtRecherche,rbTitre,rbAuteurRealisateur,rbMotsCles,txtfldRecherche,btnEffacer);
		Catalogue catalogue = Catalogue.getInstance();
		
		Tab tabCatalogue = new Tab("Catalogue");
		tabCatalogue.setClosable(false);
		tabCatalogue.setGraphic(new ImageView("icon-collection.png"));
		tabCatalogue.setContent(tableDocuments);
		
		//Tableaux du catalogue
		TableColumn<Document, String> ColonneNoDoc = new TableColumn<Document, String>("Numéro Document");
		ColonneNoDoc.setCellValueFactory(new PropertyValueFactory<>("NoDoc"));
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
        
        FilteredList<Document> lstDocAChercher = new FilteredList<Document>(FXCollections.observableList(Catalogue.getLstDocuments()));
        
        if (rbTitre.isSelected()) {
        	 txtfldRecherche.textProperty().addListener((observable, oldvalue, newvalue) ->{
             	lstDocAChercher.setPredicate(x -> x.getTitre().contains(newvalue));
             	tableDocuments.getItems().clear();
             	tableDocuments.getItems().addAll(lstDocAChercher);
             });
        }
        else if (rbMotsCles.isSelected()) {
        	txtfldRecherche.textProperty().addListener((observable, oldvalue, newvalue) ->{
             	lstDocAChercher.setPredicate(x -> x.getMotCles().contains(newvalue));
             	tableDocuments.getItems().clear();
             	tableDocuments.getItems().addAll(lstDocAChercher);
             });
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
        
        //Recherche
        FilteredList<Livre> lstLivreAChercher = new FilteredList<Livre>(FXCollections.observableList(Catalogue.getLstLivres()));
        if (rbTitre.isSelected()) {
       	 txtfldRecherche.textProperty().addListener((observable, oldvalue, newvalue) ->{
            	lstLivreAChercher.setPredicate(x -> x.getTitre().contains(newvalue));
            	tableLivre.getItems().clear();
            	tableLivre.getItems().addAll(lstLivreAChercher);
            });
       }
       else if (rbAuteurRealisateur.isSelected()) {
    	   txtfldRecherche.textProperty().addListener((observable, oldvalue, newvalue) ->{
           	lstLivreAChercher.setPredicate(x -> x.getAuteur().contains(newvalue));
           	tableLivre.getItems().clear();
           	tableLivre.getItems().addAll(lstLivreAChercher);
           });
       }
       else if(rbMotsCles.isSelected()) {
    	   txtfldRecherche.textProperty().addListener((observable, oldvalue, newvalue) ->{
              	lstLivreAChercher.setPredicate(x -> x.getMotCles().contains(newvalue));
              	tableLivre.getItems().clear();
              	tableLivre.getItems().addAll(lstLivreAChercher);
              });
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
        
        //Recherche
        FilteredList<DVD> lstDVDAChercher = new FilteredList<DVD>(FXCollections.observableList(Catalogue.getLstDvd()));
        if (rbTitre.isSelected()) {
       	 txtfldRecherche.textProperty().addListener((observable, oldvalue, newvalue) ->{
       		 	lstDVDAChercher.setPredicate(x -> x.getTitre().contains(newvalue));
            	tableDVD.getItems().clear();
            	tableDVD.getItems().addAll(lstDVDAChercher);
            });
       }
       else if (rbAuteurRealisateur.isSelected()) {
    	   txtfldRecherche.textProperty().addListener((observable, oldvalue, newvalue) ->{
    		   lstDVDAChercher.setPredicate(x -> x.getStrRealisateur().contains(newvalue));
           	tableDVD.getItems().clear();
           	tableDVD.getItems().addAll(lstDVDAChercher);
           });
       }
       else if(rbMotsCles.isSelected()) {
    	   txtfldRecherche.textProperty().addListener((observable, oldvalue, newvalue) ->{
    		   lstDVDAChercher.setPredicate(x -> x.getMotCles().contains(newvalue));
              	tableDVD.getItems().clear();
              	tableDVD.getItems().addAll(lstDVDAChercher);
              });
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
        
        //Recherche
        FilteredList<Periodique> lstPeriodiqueAChercher = new FilteredList<Periodique>(FXCollections.observableList(Catalogue.getLstPeriodiques()));
        if (rbTitre.isSelected()) {
       	 txtfldRecherche.textProperty().addListener((observable, oldvalue, newvalue) ->{
       		 	lstPeriodiqueAChercher.setPredicate(x -> x.getTitre().contains(newvalue));
            	tablePeriodique.getItems().clear();
            	tablePeriodique.getItems().addAll(lstPeriodiqueAChercher);
            });
       }
       else if (rbMotsCles.isSelected()) {
    	   txtfldRecherche.textProperty().addListener((observable, oldvalue, newvalue) ->{
    		   lstPeriodiqueAChercher.setPredicate(x -> x.getMotCles().contains(newvalue));
              	tablePeriodique.getItems().clear();
              	tablePeriodique.getItems().addAll(lstPeriodiqueAChercher);
              });
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
		
		//Tableau des préposées
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

        //Supprimer un préposé
        Button btnSupprimerPrepose = new Button("Supprimer un préposé");
        btnSupprimerPrepose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Prepose preposeSelectionne = tablePreposes.getSelectionModel().getSelectedItem();
				
				if (preposeSelectionne == null) {
					Alert fenetreAucunPreposeSelectionne = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					
					fenetreAucunPreposeSelectionne.setTitle("Erreur");
					Stage stage = (Stage) fenetreAucunPreposeSelectionne.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-erreur.png"));
					fenetreAucunPreposeSelectionne.setContentText("Veuillez sélectionner un préposé dans la table.");
					fenetreAucunPreposeSelectionne.setHeaderText(null);
					fenetreAucunPreposeSelectionne.showAndWait();
				}
				else {
					supprimerPrepose(preposeSelectionne);
				}
				
			}});
        
        Separator separateur = new Separator(Orientation.HORIZONTAL);
        Button btnDeconnexion = new Button("Déconnexion");
        
        //Déconnecter vers identification
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
		
		//Modifier un adhérent
		btnModifierAdherent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Adherent adherentSelectionne = tableAdherent.getSelectionModel().getSelectedItem();
				
				if (adherentSelectionne == null) {
					Alert fenetreAucunAdherentSelectionne = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					
					fenetreAucunAdherentSelectionne.setTitle("Erreur");
					Stage stage = (Stage) fenetreAucunAdherentSelectionne.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-erreur.png"));
					fenetreAucunAdherentSelectionne.setContentText("Veuillez sélectionner un adhérent dans la table.");
					fenetreAucunAdherentSelectionne.setHeaderText(null);
					fenetreAucunAdherentSelectionne.showAndWait();
				}
				else {
					ButtonType btnconfirmer = new ButtonType("Confirmer", ButtonData.YES);
					ButtonType btnannuler = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
					Alert fenetreAjouterPrepose = new Alert(AlertType.NONE, "default Dialog",btnconfirmer,btnannuler);
					GridPane gridpane = new GridPane();
					gridpane.setHgap(5);
					
					Text adresse = new Text("Adresse:");
					Text telephone = new Text("Téléphone:");
					
					gridpane.add(adresse, 0, 2);
					gridpane.add(telephone, 0, 3);

					TextField champsAdresse = new TextField();
					champsAdresse.setText(adherentSelectionne.getStrAdresseAdherent());
					
					TextField champsTelephone = new TextField();
					champsTelephone.setText(adherentSelectionne.getStrTelephoneAdherent());
					
					gridpane.add(champsAdresse, 1, 2);
					gridpane.add(champsTelephone, 1, 3);
					
					fenetreAjouterPrepose.setTitle("Modifier un adhérent");
					Stage stage = (Stage) fenetreAjouterPrepose.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-modifieradherent.png"));
					fenetreAjouterPrepose.setHeaderText(null);
					fenetreAjouterPrepose.getDialogPane().setContent(gridpane);
					
					fenetreAjouterPrepose.showAndWait();
					
					adherentSelectionne.setStrAdresseAdherent(champsAdresse.getText());
					adherentSelectionne.setStrTelephoneAdherent(champsTelephone.getText());
					
					tableAdherent.getItems().clear();
					
			        for (Adherent adherent : lstAdherents) {
			        	tableAdherent.getItems().add(adherent);
			        }
				}
				
			}});
		
		Button btnSupprimerAdherent = new Button("Supprimer un adhérent");
		btnSupprimerAdherent.setMaxWidth(Double.MAX_VALUE);
		
		//Supprimer un adhérent
		btnSupprimerAdherent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Adherent adherentSelectionne = tableAdherent.getSelectionModel().getSelectedItem();
				
				if (adherentSelectionne == null) {
					Alert fenetreAucunAdherentSelectionne = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					
					fenetreAucunAdherentSelectionne.setTitle("Erreur");
					Stage stage = (Stage) fenetreAucunAdherentSelectionne.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-erreur.png"));
					fenetreAucunAdherentSelectionne.setContentText("Veuillez sélectionner un adhérent dans la table.");
					fenetreAucunAdherentSelectionne.setHeaderText(null);
					fenetreAucunAdherentSelectionne.showAndWait();
				}
				else {
					lstAdherents.remove(adherentSelectionne);
					intNbAdherent--;
					
					tableAdherent.getItems().clear();
					
			        for (Adherent adherent : lstAdherents) {
			        	tableAdherent.getItems().add(adherent);
			        }
				}
				
			}});
		
		
		Button btnPayerSolde = new Button("Payer un solde");
		btnPayerSolde.setMaxWidth(Double.MAX_VALUE);
		
		//Supprimer un adhérent
		btnSupprimerAdherent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Adherent adherentSelectionne = tableAdherent.getSelectionModel().getSelectedItem();
				
				if (adherentSelectionne == null) {
					Alert fenetreAucunAdherentSelectionne = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					
					fenetreAucunAdherentSelectionne.setTitle("Erreur");
					Stage stage = (Stage) fenetreAucunAdherentSelectionne.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-erreur.png"));
					fenetreAucunAdherentSelectionne.setContentText("Veuillez sélectionner un adhérent pour qui un solde doit être payé.");
					fenetreAucunAdherentSelectionne.setHeaderText(null);
					fenetreAucunAdherentSelectionne.showAndWait();
				}
				else {
					if (adherentSelectionne.getDblSoldeDu() == 0.00) {
						Alert fenetreAucunSoldeAPayer = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
						
						fenetreAucunSoldeAPayer.setTitle("Erreur");
						Stage stage = (Stage) fenetreAucunSoldeAPayer.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("icon-erreur.png"));
						fenetreAucunSoldeAPayer.setContentText("Cet adhérent n'as aucun solde à payer .");
						fenetreAucunSoldeAPayer.setHeaderText(null);
						fenetreAucunSoldeAPayer.showAndWait();
					}
					else {
						adherentSelectionne.setDblSoldeDu(0.00);
						
						tableAdherent.getItems().clear();
						
				        for (Adherent adherent : lstAdherents) {
				        	tableAdherent.getItems().add(adherent);
				        }
					}
				}
				
			}});
		
		vboxGestionAdherent.getChildren().addAll(btnAjouterAdherent,btnModifierAdherent,btnSupprimerAdherent,btnPayerSolde);
		
		VBox vboxGestionPrets = new VBox();
		vboxGestionPrets.setSpacing(5);
		vboxGestionPrets.setAlignment(Pos.CENTER);
		
		Button btnInscrirePret = new Button("Inscrire un prêt");
		btnInscrirePret.setMaxWidth(Double.MAX_VALUE);
		
		//Inscrire un prêt
		btnInscrirePret.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Document documentSelectionne;
				if (rootPrepose.getSelectionModel().getSelectedItem() == tabCatalogue) {
					documentSelectionne = tableDocuments.getSelectionModel().getSelectedItem();
				}
				else if (rootPrepose.getSelectionModel().getSelectedItem() == tabLivres) {
					documentSelectionne = tableLivre.getSelectionModel().getSelectedItem();
				}
				else if (rootPrepose.getSelectionModel().getSelectedItem() == tabDVD) {
					documentSelectionne = tableDVD.getSelectionModel().getSelectedItem();
				}
				else {
					documentSelectionne = tablePeriodique.getSelectionModel().getSelectedItem();
				}
				
				if (documentSelectionne == null) {
					Alert fenetreAucunAdherentSelectionne = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					
					fenetreAucunAdherentSelectionne.setTitle("Erreur");
					Stage stage = (Stage) fenetreAucunAdherentSelectionne.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-erreur.png"));
					fenetreAucunAdherentSelectionne.setContentText("Veuillez sélectionner un document à prêter.");
					fenetreAucunAdherentSelectionne.setHeaderText(null);
					fenetreAucunAdherentSelectionne.showAndWait();
				}
				else {
					
					ButtonType btnconfirmer = new ButtonType("Confirmer", ButtonData.YES);
					ButtonType btnannuler = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
					Alert fenetreSelectionnerAdherent = new Alert(AlertType.NONE, "default Dialog",btnconfirmer,btnannuler);
					
					fenetreSelectionnerAdherent.setTitle("Erreur");
					Stage stage = (Stage) fenetreSelectionnerAdherent.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-erreur.png"));
					fenetreSelectionnerAdherent.getDialogPane().setContent(tableAdherent);
					fenetreSelectionnerAdherent.setHeaderText(null);
					
					effectuerPret(fenetreSelectionnerAdherent,btnconfirmer, documentSelectionne);
					
					fenetreSelectionnerAdherent.showAndWait();
					
				}
				
			}});
		
		Button btnInscrireRetour = new Button("Inscrire un retour");
		btnInscrireRetour.setMaxWidth(Double.MAX_VALUE);
		
		//Inscrire un retour
		btnInscrireRetour.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Document documentSelectionne;
				if (rootPrepose.getSelectionModel().getSelectedItem() == tabCatalogue) {
					documentSelectionne = tableDocuments.getSelectionModel().getSelectedItem();
				}
				else if (rootPrepose.getSelectionModel().getSelectedItem() == tabLivres) {
					documentSelectionne = tableLivre.getSelectionModel().getSelectedItem();
				}
				else if (rootPrepose.getSelectionModel().getSelectedItem() == tabDVD) {
					documentSelectionne = tableDVD.getSelectionModel().getSelectedItem();
				}
				else {
					documentSelectionne = tablePeriodique.getSelectionModel().getSelectedItem();
				}
				
				if (documentSelectionne == null) {
					Alert fenetreAucunAdherentSelectionne = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					
					fenetreAucunAdherentSelectionne.setTitle("Erreur");
					Stage stage = (Stage) fenetreAucunAdherentSelectionne.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-erreur.png"));
					fenetreAucunAdherentSelectionne.setContentText("Veuillez sélectionner un document à retourner.");
					fenetreAucunAdherentSelectionne.setHeaderText(null);
					fenetreAucunAdherentSelectionne.showAndWait();
				}
				else if (documentSelectionne.getDisponible().equals("Disponible")) {
					Alert fenetreDocumentSelectionneDisponible = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					
					fenetreDocumentSelectionneDisponible.setTitle("Erreur");
					Stage stage = (Stage) fenetreDocumentSelectionneDisponible.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-erreur.png"));
					fenetreDocumentSelectionneDisponible.setContentText("Veuillez sélectionner un prêté.");
					fenetreDocumentSelectionneDisponible.setHeaderText(null);
					fenetreDocumentSelectionneDisponible.showAndWait();
				}
				else {
					documentSelectionne.setDisponible("Disponible");
					documentSelectionne.setStrEmprunteur("");
				}
				
			}});
		
		vboxGestionPrets.getChildren().addAll(btnInscrirePret,btnInscrireRetour);
		
		TitledPane tpGestionCatalogue = new TitledPane("Gestion du catalogue" , vboxGestionCatalogue);
		
		TitledPane tpGestionAdherents = new TitledPane("Gestion des adhérents" , vboxGestionAdherent);
		
		
		//Afficher table des adhérents
		tpGestionAdherents.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				vboxPrepose.getChildren().clear();
				hboxRootPrepose.getChildren().remove(vboxOptionsPrepose);
				tableAdherent.getItems().clear();
				
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
		        
		        
		        //Affiche le "$" dans le tableau
		        NumberFormat Dollard = NumberFormat.getCurrencyInstance();
		        
		        ColonneSoldeDu.setCellFactory(tc -> new TableCell<Adherent, Double>() {

		            @Override
		            protected void updateItem(Double price, boolean empty) {
		                super.updateItem(price, empty);
		                if (empty) {
		                    setText(null);
		                } else {
		                    setText(Dollard.format(price));
		                }
		            }
		        });
		        
		        
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
		
		//Retour à l'Affichage par défaut
		tpGestionCatalogue.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				vboxPrepose.getChildren().clear();
				hboxRootPrepose.getChildren().removeAll(tableAdherent,vboxOptionsPrepose);
				
				vboxPrepose.getChildren().addAll(hboxOptionsRecherchePrepose, rootPrepose);
				hboxRootPrepose.getChildren().addAll(vboxOptionsPrepose);
			}
		});
		
		//Retour à l'Affichage par défaut
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
		RadioButton rbTitrePrepose = new RadioButton("Titre");
		togglegroupPrepose.getToggles().addAll(rbTitrePrepose,rbAuteurRealisateur,rbMotsCles);
		
		TextField txtfldRecherchePrepose = new TextField();
		Button btnEffacerPrepose = new Button("Effacer");
		
		btnEffacerPrepose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				txtfldRecherchePrepose.clear();
			}});
		
		btnEffacer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				txtfldRecherche.clear();
			}});
		
		hboxOptionsRecherchePrepose.getChildren().addAll(txtRecherchePrepose,rbTitrePrepose,rbAuteurRealisateurPrepose,rbMotsClesPrepose,txtfldRecherchePrepose,btnEffacerPrepose);
		
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
        
        //Recherche
        FilteredList<Document> lstDocAChercherPrepose = new FilteredList<Document>(FXCollections.observableList(Catalogue.getLstDocuments	()));
        
        if (rbTitrePrepose.isSelected()) {
        	 txtfldRecherchePrepose.textProperty().addListener((observable, oldvalue, newvalue) ->{
        		 lstDocAChercherPrepose.setPredicate(x -> x.getTitre().contains(newvalue));
             	tableDocumentsPrepose.getItems().clear();
             	tableDocumentsPrepose.getItems().addAll(lstDocAChercherPrepose);
             });
        }
        else if (rbMotsClesPrepose.isSelected()) {
        	txtfldRecherchePrepose.textProperty().addListener((observable, oldvalue, newvalue) ->{
             	lstDocAChercherPrepose.setPredicate(x -> x.getMotCles().contains(newvalue));
             	tableDocumentsPrepose.getItems().clear();
             	tableDocumentsPrepose.getItems().addAll(lstDocAChercherPrepose);
             });
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
        
        //Recherche
        FilteredList<Livre> lstLivreAChercherPrepose = new FilteredList<Livre>(FXCollections.observableList(Catalogue.getLstLivres()));
        if (rbTitre.isSelected()) {
       	 txtfldRecherchePrepose.textProperty().addListener((observable, oldvalue, newvalue) ->{
            	lstLivreAChercherPrepose.setPredicate(x -> x.getTitre().contains(newvalue));
            	tableLivrePrepose.getItems().clear();
            	tableLivrePrepose.getItems().addAll(lstLivreAChercherPrepose);
            });
       }
       else if (rbAuteurRealisateurPrepose.isSelected()) {
    	   txtfldRecherchePrepose.textProperty().addListener((observable, oldvalue, newvalue) ->{
           	lstLivreAChercherPrepose.setPredicate(x -> x.getAuteur().contains(newvalue));
           	tableLivrePrepose.getItems().clear();
           	tableLivrePrepose.getItems().addAll(lstLivreAChercherPrepose);
           });
       }
       else if(rbMotsClesPrepose.isSelected()) {
    	   txtfldRecherchePrepose.textProperty().addListener((observable, oldvalue, newvalue) ->{
              	lstLivreAChercherPrepose.setPredicate(x -> x.getMotCles().contains(newvalue));
              	tableLivrePrepose.getItems().clear();
              	tableLivrePrepose.getItems().addAll(lstLivreAChercherPrepose);
              });
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
        
        //Recherche
        FilteredList<DVD> lstDVDAChercherPrepose = new FilteredList<DVD>(FXCollections.observableList(Catalogue.getLstDvd()));
        if (rbTitrePrepose.isSelected()) {
       	 txtfldRecherchePrepose.textProperty().addListener((observable, oldvalue, newvalue) ->{
       		 	lstDVDAChercherPrepose.setPredicate(x -> x.getTitre().contains(newvalue));
            	tableDVDPrepose.getItems().clear();
            	tableDVDPrepose.getItems().addAll(lstDVDAChercherPrepose);
            });
       }
       else if (rbAuteurRealisateurPrepose.isSelected()) {
    	   txtfldRecherchePrepose.textProperty().addListener((observable, oldvalue, newvalue) ->{
    		   lstDVDAChercherPrepose.setPredicate(x -> x.getStrRealisateur().contains(newvalue));
           	tableDVDPrepose.getItems().clear();
           	tableDVDPrepose.getItems().addAll(lstDVDAChercherPrepose);
           });
       }
       else if(rbMotsClesPrepose.isSelected()) {
    	   txtfldRecherchePrepose.textProperty().addListener((observable, oldvalue, newvalue) ->{
    		   lstDVDAChercherPrepose.setPredicate(x -> x.getMotCles().contains(newvalue));
              	tableDVDPrepose.getItems().clear();
              	tableDVDPrepose.getItems().addAll(lstDVDAChercherPrepose);
              });
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
        
        //Recherche
        FilteredList<Periodique> lstPeriodiqueAChercherPrepose = new FilteredList<Periodique>(FXCollections.observableList(Catalogue.getLstPeriodiques()));
        if (rbTitrePrepose.isSelected()) {
       	 txtfldRecherchePrepose.textProperty().addListener((observable, oldvalue, newvalue) ->{
       		 	lstPeriodiqueAChercherPrepose.setPredicate(x -> x.getTitre().contains(newvalue));
            	tablePeriodiquePrepose.getItems().clear();
            	tablePeriodiquePrepose.getItems().addAll(lstPeriodiqueAChercherPrepose);
            });
       }
       else if (rbMotsClesPrepose.isSelected()) {
    	   txtfldRecherchePrepose.textProperty().addListener((observable, oldvalue, newvalue) ->{
    		   lstPeriodiqueAChercherPrepose.setPredicate(x -> x.getMotCles().contains(newvalue));
              	tablePeriodiquePrepose.getItems().clear();
              	tablePeriodiquePrepose.getItems().addAll(lstPeriodiqueAChercherPrepose);
              });
       }
		
		Tab tabPeriodiquePrepose = new Tab("Périodiques");
		tabPeriodiquePrepose.setClosable(false);
		tabPeriodiquePrepose.setGraphic(new ImageView("icon-periodique.png"));
		tabPeriodiquePrepose.setContent(tablePeriodiquePrepose);
		
		rootPrepose.getTabs().addAll(tabCataloguePrepose,tabLivresPrepose,tabDVDPrepose,tabPeriodiquePrepose);
		
		//Scene dossier
		HBox hboxRootDossier = new HBox();
		VBox vboxTables = new VBox();
		VBox vboxBtnQuitter = new VBox();
		
		
		//Table emprunt
		TableColumn<Document, String> colNoDocEmprunt = new TableColumn<Document, String>("Numéro du document");
		colNoDocEmprunt.setCellValueFactory(new PropertyValueFactory<>("NoDoc"));
		colNoDocEmprunt.setPrefWidth(170);
		TableColumn<Document, String> colTitreEmprunt = new TableColumn<Document, String>("Titre");
		colTitreEmprunt.setCellValueFactory(new PropertyValueFactory<>("Titre"));
		colTitreEmprunt.setPrefWidth(180);
		TableColumn<Document, String> colAuteurEmprunt = new TableColumn<Document, String>("Auteur/Réalisateur");
		colAuteurEmprunt.setCellValueFactory(new PropertyValueFactory<>("StrRealisateur"));
		//colAuteurEmprunt.setCellValueFactory(new PropertyValueFactory<>("Auteur"));
		colAuteurEmprunt.setPrefWidth(150);
		TableColumn<Document, LocalDate> colDateParutionEmprunt = new TableColumn<Document, LocalDate>("Date de parution");
		colDateParutionEmprunt.setCellValueFactory(new PropertyValueFactory<>("DateParution"));
		colDateParutionEmprunt.setPrefWidth(255);
		
		tableDocumentsEmpruntes.getColumns().addAll(colNoDocEmprunt, colTitreEmprunt,colAuteurEmprunt,colDateParutionEmprunt);
		
		
		//Table prets
		TableColumn<Pret, String> colNoPret = new TableColumn<Pret, String>("Numéro du pret");
		colNoPret.setCellValueFactory(new PropertyValueFactory<>("NoPret"));
		colNoPret.setPrefWidth(150);
		TableColumn<Pret, LocalDate> colDatePret = new TableColumn<Pret, LocalDate>("Date du prêt");
		colDatePret.setCellValueFactory(new PropertyValueFactory<>("DatePret"));
		colDatePret.setPrefWidth(190);
		TableColumn<Pret, LocalDate> colDateRetourPrevu = new TableColumn<Pret, LocalDate>("Date de retour prévu");
		colDateRetourPrevu.setCellValueFactory(new PropertyValueFactory<>("DateRetourPrevuPret"));
		colDateRetourPrevu.setPrefWidth(165);
		TableColumn<Pret, LocalDate> colDateRetour = new TableColumn<Pret, LocalDate>("Date de retour");
		colDateRetour.setCellValueFactory(new PropertyValueFactory<>("DateRetourPret"));
		colDateRetour.setPrefWidth(120);
		TableColumn<Pret, Double> colAmende = new TableColumn<Pret, Double>("Amende");
		colAmende.setCellValueFactory(new PropertyValueFactory<>("Amende"));
		colAmende.setPrefWidth(130);
		
		tablePretAdherent.getColumns().addAll(colNoPret,colDatePret,colDateRetourPrevu,colDateRetour,colAmende);
		
		vboxTables.getChildren().addAll(tableDocumentsEmpruntes,tablePretAdherent);
		
		Button btnQuitterDossier = new Button("Quitter");
		
		
		//Quitter vers identification
		btnQuitterDossier.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				arg0.setScene(sceneIdentification);
				arg0.setTitle("Identification");
				
			}});
		
		vboxBtnQuitter.getChildren().add(btnQuitterDossier);
		hboxRootDossier.getChildren().addAll(vboxTables,vboxBtnQuitter);
		hboxRootDossier.setPadding(new Insets(5,5,5,5));
		hboxRootDossier.setSpacing(10);
		
		Scene sceneDossier = new Scene(hboxRootDossier);
		
		//Consulter dossier
		btnConsulterDossier.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if (!noTelephone.getText().matches("^\\(\\d{3}\\)\\s\\d{3}-\\d{4}")) {
					Alert fenetreTelephoneIncorrect = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					fenetreTelephoneIncorrect.setTitle("Erreur");
					
					Stage stage1 = (Stage) fenetreTelephoneIncorrect.getDialogPane().getScene().getWindow();
					stage1.getIcons().add(new Image("icon-erreur.png"));
					fenetreTelephoneIncorrect.setContentText("Le format du numéro de téléphone entré est incorrect. Le format est \"(###) ###-####\"."  );
					fenetreTelephoneIncorrect.setHeaderText(null);
					fenetreTelephoneIncorrect.showAndWait();
				}
				
				arg0.setScene(sceneDossier);
				arg0.setTitle("Dossier de l'adhérent");
				
				for (Adherent adherent :lstAdherents) {
					if (rbNomPrenom.isSelected()) {
						
						if (txtfldNom.getText().equals(adherent.getStrNomAdherent()) && txtfldPrenom.getText().equals(adherent.getStrPrenomAdherent())) {
							
							for (Document doc : adherent.getLstDoc() ) {
								tableDocumentsEmpruntes.getItems().add(doc);
							}
							
							for (Pret pret: adherent.getLstPrets()) {
								tablePretAdherent.getItems().add(pret);
							}
							
						}
						
					}
					else if (rbNoTelephone.isSelected()) {
						if (noTelephone.getText().equals(adherent.getStrTelephoneAdherent())) {
							
							for (Document doc : adherent.getLstDoc() ) {
								tableDocumentsEmpruntes.getItems().add(doc);
							}
							
							for (Pret pret: adherent.getLstPrets()) {
								tablePretAdherent.getItems().add(pret);
							}
						}
					}
				}
				
			}});
		
		//Consulter dossier à partir du catalogue
		btnConsulterDossierAdherent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!noTelephoneAdherent.getText().matches("^\\(\\d{3}\\)\\s\\d{3}-\\d{4}")) {
					Alert fenetreTelephoneIncorrect = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					fenetreTelephoneIncorrect.setTitle("Erreur");
					
					Stage stage1 = (Stage) fenetreTelephoneIncorrect.getDialogPane().getScene().getWindow();
					stage1.getIcons().add(new Image("icon-erreur.png"));
					fenetreTelephoneIncorrect.setContentText("Le format du numéro de téléphone entré est incorrect. Le format est \"(###) ###-####\"."  );
					fenetreTelephoneIncorrect.setHeaderText(null);
					fenetreTelephoneIncorrect.showAndWait();
				}
				
				arg0.setScene(sceneDossier);
				arg0.setTitle("Dossier de l'adhérent");
				
				for (Adherent adherent :lstAdherents) {
					if (rbNomPrenomAdherent.isSelected()) {
						
						if (txtfldNomAdherent.getText().equals(adherent.getStrNomAdherent()) && txtfldPrenomAdherent.getText().equals(adherent.getStrPrenomAdherent())) {
							
							for (Document doc : adherent.getLstDoc() ) {
								tableDocumentsEmpruntes.getItems().add(doc);
							}
							
							for (Pret pret: adherent.getLstPrets()) {
								tablePretAdherent.getItems().add(pret);
							}
							
						}
						
					}
					else if (rbNoTelephoneAdherent.isSelected()) {
						if (noTelephoneAdherent.getText().equals(adherent.getStrTelephoneAdherent())) {
							
							for (Document doc : adherent.getLstDoc() ) {
								tableDocumentsEmpruntes.getItems().add(doc);
							}
							
							for (Pret pret: adherent.getLstPrets()) {
								tablePretAdherent.getItems().add(pret);
							}
						}
					}
				}
				
			}});
		
		//Ajouter un document au catalogue
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
				
				Text TypeDoc = new Text("Type de document:");
				
				ComboBox<String> cbxTypeDoc = new ComboBox<String>();
				cbxTypeDoc.getItems().addAll("Livre","DVD","Périodique");
				cbxTypeDoc.getSelectionModel().selectFirst();
				
				Text Titre = new Text("Titre:");
				TextField champsTitre = new TextField("A");
				
				TextField champsDateParution = new TextField("12-12-1990");
				Text DateParution = new Text("Date de parution:");
				
				Text AuteurLivre = new Text("Auteur:");
				TextField champsAuteurLivre = new TextField("A");
				
				Text MotsClesLivre = new Text("Mots Clés:");
				TextField champsMotsClesLivre = new TextField("A");
				
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
				
				
				Text NbDisques = new Text("Nombre de disques:");
				TextField champsNbDisques = new TextField();
				
				Text Realisateur = new Text("Réalisateur:");
				TextField champsRealisateur = new TextField();
				
				Text NoPeriodique = new Text("Numéro de périodique:");
				TextField champsNoPeriodique = new TextField();
				
				Text NoVolume = new Text("Numéro de volume:");
				TextField champsNoVolume = new TextField();
				
				//Changement dépendant du combobox
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
							
						}
					}});
				
				fenetreAjouterDocument.setTitle("Ajouter un document");
				Stage stage = (Stage) fenetreAjouterDocument.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("icon-ajouterdocument.png"));
				fenetreAjouterDocument.setHeaderText(null);
				fenetreAjouterDocument.getDialogPane().setContent(gridpaneLivre);
				
				//Vérification et ajout
				verifierEtAjouterDocument(fenetreAjouterDocument, btnconfirmer, champsTitre, champsAuteurLivre, champsDateParution, champsMotsClesLivre, cbxTypeDoc, champsNbDisques, champsRealisateur, champsNoPeriodique, champsNoVolume);
				
				fenetreAjouterDocument.showAndWait();
				
				Button btAnnuler = (Button) fenetreAjouterDocument.getDialogPane().lookupButton(btnannuler); 
				btAnnuler.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						fenetreAjouterDocument.close();
					}});
			}});
		
		//Supprimer un document 
		btnSupprimerDocument.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				Document documentSelectionne;
				if (rootPrepose.getSelectionModel().getSelectedItem() == tabCatalogue) {
					documentSelectionne = tableDocumentsPrepose.getSelectionModel().getSelectedItem();
				}
				else if (rootPrepose.getSelectionModel().getSelectedItem() == tabLivres) {
					documentSelectionne = tableLivrePrepose.getSelectionModel().getSelectedItem();
				}
				else if (rootPrepose.getSelectionModel().getSelectedItem() == tabDVD) {
					documentSelectionne = tableDVDPrepose.getSelectionModel().getSelectedItem();
				}
				else {
					documentSelectionne = tablePeriodique.getSelectionModel().getSelectedItem();
				}
				
				//Vérification si une entrée de la table est sélectionnée
				if (documentSelectionne == null) {
					Alert fenetreAucunDocumentSelectionne = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					
					fenetreAucunDocumentSelectionne.setTitle("Erreur");
					Stage stage = (Stage) fenetreAucunDocumentSelectionne.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("icon-erreur.png"));
					fenetreAucunDocumentSelectionne.setContentText("Veuillez sélectionner un document.");
					fenetreAucunDocumentSelectionne.setHeaderText(null);
					fenetreAucunDocumentSelectionne.showAndWait();
				}
				else {
					if (rootPrepose.getSelectionModel().getSelectedItem() == tabCatalogue) {
						Catalogue.getLstDocuments().remove(documentSelectionne);
						tableDocumentsPrepose.getItems().clear();
						
						for (Document doc : Catalogue.getLstDocuments()) {
							tableDocumentsPrepose.getItems().add(doc);
						}
					}
					else if (rootPrepose.getSelectionModel().getSelectedItem() == tabLivres) {
						Catalogue.getLstLivres().remove(documentSelectionne);
						tableLivrePrepose.getItems().clear();
						
						for (Livre livre : Catalogue.getLstLivres()) {
							tableLivrePrepose.getItems().add(livre);
						}
						
					}
					else if (rootPrepose.getSelectionModel().getSelectedItem() == tabDVD) {
						Catalogue.getLstDvd().remove(documentSelectionne);
						tableDVDPrepose.getItems().clear();
						
						for (DVD dvd : Catalogue.getLstDvd()) {
							tableDVDPrepose.getItems().add(dvd);
						}
					}
					else {
						Catalogue.getLstPeriodiques().remove(documentSelectionne);
						tablePeriodiquePrepose.getItems().clear();
						
						for (Periodique periodique : Catalogue.getLstPeriodiques()) {
							tablePeriodiquePrepose.getItems().add(periodique);
						}
					}
					
				}
				
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
				
				//Vérifier et ajouter un préposé
				verifierInfosPrepose(fenetreAjouterPrepose, btnconfirmer, champsNom, champsPrenom, champsAdresse, champsTelephone, champsMotDePasse);
					
				Button btAnnuler = (Button) fenetreAjouterPrepose.getDialogPane().lookupButton(btnannuler); 
				btAnnuler.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						fenetreAjouterPrepose.close();
					}});
				
				fenetreAjouterPrepose.showAndWait();
			}});
        
        //Ajouter un adhérent
        btnAjouterAdherent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				ButtonType btnconfirmer = new ButtonType("Confirmer", ButtonData.YES);
				ButtonType btnannuler = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
				Alert fenetreAjouterAdherent = new Alert(AlertType.NONE, "default Dialog",btnconfirmer,btnannuler);
				GridPane gridpane = new GridPane();
				gridpane.setHgap(5);
				
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
				
				//Verifier et ajouter un adhérent
				verifierEtAjouterAdherent(fenetreAjouterAdherent, btnconfirmer, champsNom, champsPrenom, champsAdresse, champsTelephone);
				
				fenetreAjouterAdherent.showAndWait();
				
				btAnnuler.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						fenetreAjouterAdherent.close();
					}});
				
			}});
        
        //Bouton de connexion et verification
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
						
						for (Prepose prepose : lstPrepose) {
							tablePreposes.getItems().add(prepose);
						}
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
        
        //Consulter le catalogue
        btnConsulterCatalogue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				arg0.setTitle("Médiathèque");
				arg0.setScene(sceneCatalogue);
			}});
        
        //Sérialisation à la fermeture
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
        
        //Quitter vers identification
        btnQuitterCatalogue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				arg0.setTitle("Identification");
				arg0.setScene(sceneIdentification);
			}});
        
        //Quitter vers identification
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
	
	//Trouver le préposé à supprimer dans la liste, le supprimer et réécrire le fichier texte.
	public void supprimerPrepose(Prepose prepose) {
		tablePreposes.getItems().remove(prepose);
		
		for (Prepose prep : lstPrepose) {
			if (prep.getStrNoEmploye().equals(prepose.getStrNoEmploye())) {
				lstPrepose.remove(prepose);
				break;
			}
		}
		
		try {
			File file = new File("Identifiants préposé.txt");
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false),"UTF-8"));
			
			bw.write("Administrateur");
			bw.newLine();
			bw.write("Identifiant: admin");
			bw.newLine();
			bw.write("Mot de passe: 79251367");
			bw.newLine();
			bw.write("\r\n");
			
			for (int i =0; i<lstPrepose.size(); i++) {
				bw.write("Préposé");
				bw.newLine();
				bw.write("Identifiant: " + lstPrepose.get(i).getIdentifiants().getStrIdentifiant());
				bw.newLine();
				bw.write("Mot de passe: " + lstPrepose.get(i).getIdentifiants().getStrMotDePasse());
				bw.newLine();
				bw.write("\r\n");
			}

			bw.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	//Vérifier et ajouter un préposé
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
				Adherent adherent = new Adherent("ADH"+intNbAdherent, champsNom.getText().trim(), champsPrenom.getText().trim(), champsAdresse.getText(), champsTelephone.getText().trim(), 0, 0.00, new ArrayList<Pret>(), new ArrayList<Document>());

				lstAdherents.add(adherent);
				tableAdherent.getItems().add(adherent);
				
				intNbAdherent++;
			}
			
		});
	}
	
	//Vérifier et ajouter un document
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
			else if (champsDateParution.getText().trim().isEmpty()) {
				Alert fenetreDateParutionManquante = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreDateParutionManquante.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreDateParutionManquante.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreDateParutionManquante.setContentText("Vous avez oublié d'entrer la date de parution.");
				fenetreDateParutionManquante.setHeaderText(null);
				fenetreDateParutionManquante.showAndWait();
				event.consume();
			}
			else if (!champsDateParution.getText().trim().matches("^(?:(?:31(-)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(-)(?:0?[13-9]|1[0-2])\\2))"
					+ "(?:(?:1[6-9]|[2-9]\\d)\\d{2})$|^(?:29(-)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)(?:0[48]|[2468][048]|[13579][26])|"
					+ "(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(-)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)\\d{2})$")) 
			{
				Alert fenetreDateIncorrecte = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreDateIncorrecte.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreDateIncorrecte.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreDateIncorrecte.setContentText("La date que vous avez entré ou son format sont incorrects. Le format est \"JJ-MM-AAAA\"."  );
				fenetreDateIncorrecte.setHeaderText(null);
				fenetreDateIncorrecte.showAndWait();
				event.consume();
			}
			else if (cbxTypeDoc.getValue() == "Livre") {
				if (champsAuteur.getText().isEmpty()) {
					Alert fenetreAuteurManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					fenetreAuteurManquant.setTitle("Erreur");
					
					Stage stage1 = (Stage) fenetreAuteurManquant.getDialogPane().getScene().getWindow();
					stage1.getIcons().add(new Image("icon-erreur.png"));
					fenetreAuteurManquant.setContentText("Veuillez entrer l'auteur du document.");
					fenetreAuteurManquant.setHeaderText(null);
					fenetreAuteurManquant.showAndWait();
					event.consume();
				}
				else if (champsMotsCles.getText().trim().isEmpty()) {
					Alert fenetreMotsClesManquants = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					fenetreMotsClesManquants.setTitle("Erreur");
					
					Stage stage1 = (Stage) fenetreMotsClesManquants.getDialogPane().getScene().getWindow();
					stage1.getIcons().add(new Image("icon-erreur.png"));
					fenetreMotsClesManquants.setContentText("Vous avez oublié d'entrer des mots clés");
					fenetreMotsClesManquants.setHeaderText(null);
					fenetreMotsClesManquants.showAndWait();
					event.consume();
				}
				else {
					Livre livre = new Livre("Liv"+intNbLivre, champsTitre.getText(), LocalDate.parse(champsDateParution.getText().trim(), Catalogue.getDf()), "Disponible", champsMotsCles.getText(), champsAuteur.getText().trim(), 0, "",champsMotsCles.getText());
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
						
						bw.write("Liv"+intNbLivre+ "," + champsTitre.getText() + "," + champsDateParution.getText().trim() + "," + champsAuteur.getText().trim() + "\r\n");
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			else if (cbxTypeDoc.getValue() == "DVD") {
				if (champsNbDisques.getText().trim().isEmpty()) {
					Alert fenetreNbDisquesIncorrect = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					fenetreNbDisquesIncorrect.setTitle("Erreur");
					
					Stage stage1 = (Stage) fenetreNbDisquesIncorrect.getDialogPane().getScene().getWindow();
					stage1.getIcons().add(new Image("icon-erreur.png"));
					fenetreNbDisquesIncorrect.setContentText("Veuillez entrer le nombre de disques."  );
					fenetreNbDisquesIncorrect.setHeaderText(null);
					fenetreNbDisquesIncorrect.showAndWait();
					event.consume();
				}
				else if (champsRealisateur.getText().trim().isEmpty()) {
					Alert fenetreRealisateurIncorrect = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					fenetreRealisateurIncorrect.setTitle("Erreur");
					
					Stage stage1 = (Stage) fenetreRealisateurIncorrect.getDialogPane().getScene().getWindow();
					stage1.getIcons().add(new Image("icon-erreur.png"));
					fenetreRealisateurIncorrect.setContentText("Veuillez entrer le réalisateur.");
					fenetreRealisateurIncorrect.setHeaderText(null);
					fenetreRealisateurIncorrect.showAndWait();
					event.consume();
				}
				else {
					DVD dvd = new DVD("DVD"+intNbDVD, champsTitre.getText(), LocalDate.parse(champsDateParution.getText().trim(), Catalogue.getDf()), "Disponible", Integer.parseInt(champsNbDisques.getText().trim()), champsRealisateur.getText().trim(), 0, "",champsMotsCles.getText());
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
						
						bw.write("DVD"+intNbDVD+ "," + champsTitre.getText() + "," + champsDateParution.getText().trim() + "," + champsNbDisques.getText() + "," + champsRealisateur.getText()+ "\r\n");
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
			else if (cbxTypeDoc.getValue() == "Périodique") {
				if (champsNoPeriodique.getText().isEmpty()) {
					Alert fenetreNoPeriodiqueIncorrect = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					fenetreNoPeriodiqueIncorrect.setTitle("Erreur");
					
					Stage stage1 = (Stage) fenetreNoPeriodiqueIncorrect.getDialogPane().getScene().getWindow();
					stage1.getIcons().add(new Image("icon-erreur.png"));
					fenetreNoPeriodiqueIncorrect.setContentText("Veuillez entrer le numéro du périodique.");
					fenetreNoPeriodiqueIncorrect.setHeaderText(null);
					fenetreNoPeriodiqueIncorrect.showAndWait();
					event.consume();
				}
				else if (champsNoVolume.getText().isEmpty()) {
					Alert fenetreNoVolumeIncorrect = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
					fenetreNoVolumeIncorrect.setTitle("Erreur");
					
					Stage stage1 = (Stage) fenetreNoVolumeIncorrect.getDialogPane().getScene().getWindow();
					stage1.getIcons().add(new Image("icon-erreur.png"));
					fenetreNoVolumeIncorrect.setContentText("Veuillez entrer le numéro du périodique.");
					fenetreNoVolumeIncorrect.setHeaderText(null);
					fenetreNoVolumeIncorrect.showAndWait();
					event.consume();
				}
				else {
					Periodique periodique = new Periodique("Per"+ intNbPeriodique, champsTitre.getText(), LocalDate.parse(champsDateParution.getText().trim(), Catalogue.getDf()), "Disponible", Integer.parseInt(champsNoVolume.getText().trim()), Integer.parseInt(champsNoPeriodique.getText().trim()), 0, "",champsMotsCles.getText());
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
						bw.write("Per"+intNbPeriodique+ "," + champsTitre.getText() + "," + champsDateParution.getText().trim() + "," + champsNoVolume.getText().trim() + "," + champsNoPeriodique.getText().trim() + "\r\n");
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	//Verifier et ajouter un préposé
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
				Prepose prepose = new Prepose("EMP" + intNbEmploye, champsNom.getText(), champsPrenom.getText(), champsAdresse.getText(), champsTelephone.getText(), new IdentifiantsPrepose("Préposé", "EMP"+intNbEmploye , champsMotDePasse.getText()));
				intNbEmploye++;
				IdentifiantsPrepose identifiants = new IdentifiantsPrepose("Préposé", prepose.getStrNoEmploye(), champsMotDePasse.getText());
				lstIdentifiants.add(identifiants);
				
				
				try {
					File file = new File("Identifiants préposé.txt");
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"UTF-8"));
					
					bw.write("Préposé");
					bw.newLine();
					bw.write("Identifiant: " + prepose.getIdentifiants().getStrIdentifiant());
					bw.newLine();
					bw.write("Mot de passe: " + prepose.getIdentifiants().getStrMotDePasse());
					bw.newLine();
					bw.write("\r\n");
					bw.close();
					
					lstPrepose.add(prepose);
					tablePreposes.getItems().clear();

					for (Prepose prep : lstPrepose) {
						tablePreposes.getItems().add(prep);
					}
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			
		});
	}
	
	//Effectuer le prêt
	private void effectuerPret(Alert fenetreSelectionnerAdherent, ButtonType btnconfirmer, Document Document) {
		Button btConfirmer = (Button) fenetreSelectionnerAdherent.getDialogPane().lookupButton(btnconfirmer);
		
		Adherent adherentSelectionne = tableAdherent.getSelectionModel().getSelectedItem();
		
		btConfirmer.addEventFilter(ActionEvent.ACTION, event->{
			
			if (adherentSelectionne == null) {
				Alert fenetreErreurAdherent = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreErreurAdherent.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreErreurAdherent.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreErreurAdherent.setContentText("Veuillez sélectionner un adhérent.");
				fenetreErreurAdherent.setHeaderText(null);
				fenetreErreurAdherent.showAndWait();
				event.consume();
			}
			else if (adherentSelectionne.getDblSoldeDu() > 0.00) {
				Alert fenetreAmendeImpayee = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
				fenetreAmendeImpayee.setTitle("Erreur");
				
				Stage stage1 = (Stage) fenetreAmendeImpayee.getDialogPane().getScene().getWindow();
				stage1.getIcons().add(new Image("icon-erreur.png"));
				fenetreAmendeImpayee.setContentText("Cette utilisateur ne peut pas emprunter un nouveau document car il a une amende impayée.");
				fenetreAmendeImpayee.setHeaderText(null);
				fenetreAmendeImpayee.showAndWait();
				event.consume();
			}
			else {
				Document.setDisponible("Non disponible");
				Document.setIntNbPrets(Document.getIntNbPrets() +1);
				Document.setStrEmprunteur(adherentSelectionne.getStrPrenomAdherent() + " " + adherentSelectionne.getStrNomAdherent());
				
				for (Adherent adherent :lstAdherents) {
					
					if (adherent.getStrNoAdherent().equals(adherentSelectionne.getStrNoAdherent())) {
						adherent.getLstDoc().add(Document);
						
						for (Livre livre : Catalogue.getLstLivres()) {
							if (livre.getNoDoc().equals(Document.getNoDoc())) {
								intNbPrets++;
								Pret pret = new Pret(intNbPrets, LocalDate.now(), LocalDate.now().plusDays(14), null, 0.00);
								adherent.getLstPrets().add(pret);
							}
						}
						
						for (DVD dvd: Catalogue.getLstDvd()) {
							if (dvd.getNoDoc().equals(Document.getNoDoc())) {
								intNbPrets++;
								Pret pret = new Pret(intNbPrets, LocalDate.now(), LocalDate.now().plusDays(3), null, 0.00);
								adherent.getLstPrets().add(pret);
							}
						}
						
						for (Periodique periodique : Catalogue.getLstPeriodiques()) {
							if (periodique.getNoDoc().equals(Document.getNoDoc())) {
								intNbPrets++;
								Pret pret = new Pret(intNbPrets, LocalDate.now(), LocalDate.now().plusDays(7), null, 0.00);
								adherent.getLstPrets().add(pret);
							}
						}	
					}
				}
			}
		});	
	}

	//Getter et Setter
	
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
