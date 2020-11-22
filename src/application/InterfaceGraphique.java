package application;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import javax.swing.GroupLayout.Alignment;

import donnees.Catalogue;
import donnees.DVD;
import donnees.DeserialisationPreposes;
import donnees.Document;
import donnees.Livre;
import donnees.Periodique;
import donnees.Prepose;
import donnees.SerialisationCatalogue;
import donnees.SerialisationPreposes;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
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
	private final static TableView<Document> tableDocuments = new TableView<Document>();
	private final static TableView<Livre> tableLivre = new TableView<Livre>();
	private final static TableView<DVD> tableDVD = new TableView<DVD>();
	private final static TableView<Periodique> tablePeriodique = new TableView<Periodique>();
	private final static TableView<Prepose> tablePreposes = new TableView<Prepose>();
	
	ArrayList<IdentifiantsPrepose> lstIdentifiants = new ArrayList<IdentifiantsPrepose>();
	static ArrayList<Prepose> lstPrepose = new ArrayList<Prepose>();
	
	String strIdentifiant = "";
	String strMotDePasse = "";
	
	int intNbEmploye = 1;
	
	File serPrepose = new File("Preposes.ser");
	
	@Override
	public void start(Stage arg0) throws Exception {
		
		//lecture du fichier d'identifiants et fichier Prepose.ser
		try {
			if (serPrepose.exists()) {
				DeserialisationPreposes dP = new DeserialisationPreposes();
	        	dP.Deserialiser();
			}
			
			Scanner scFichierIdentifiant = new Scanner(new File("Identifiants préposé.txt")); //Début de la lecture du fichier
			
			while (scFichierIdentifiant.hasNextLine()) { //Lecture du fichier
				String strTypeUtilisateur = scFichierIdentifiant.nextLine().substring(3); //Caractère bizzare pour aucune raison. substring pour les enlever.
				String strLigneIdentifiant = scFichierIdentifiant.nextLine().replaceAll("\\s+","").split(":")[1];
				String strLigneMotDePasse = scFichierIdentifiant.nextLine().replaceAll("\\s+","").split(":")[1];
				scFichierIdentifiant.nextLine();
				
				//Création d'un objet contenant les identifiants, ajout à un ArrayList pour vérification et ajout au TableView
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
		tabCatalogue.setClosable(false);
		tabCatalogue.setGraphic(new ImageView("icon-collection.png"));
		tabCatalogue.setContent(tableDocuments);
		
		TableColumn<Document, String> ColonneNoDoc = new TableColumn<Document, String>("Numéro Document");
		ColonneNoDoc.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getNoDoc()));
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
        TableColumn<Livre, String> ColonneMotsClesLivre = new TableColumn<Livre, String>("Mots Clés");
        /**/
        ColonneMotsClesLivre.setPrefWidth(120);
        TableColumn<Livre, String> ColonneAuteurLivre = new TableColumn<Livre, String>("Auteur");
        ColonneAuteurLivre.setCellValueFactory(new PropertyValueFactory<>("Auteur"));
        ColonneAuteurLivre.setPrefWidth(120);
        
        tableLivre.getColumns().addAll(ColonneNoDocLivre,ColonneTitreLivre,ColonneDateParutionLivre,ColonneLivreDispo,ColonneMotsClesLivre,ColonneAuteurLivre);
        
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
        	System.out.println(dvd.getNoDoc());
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
        
        Text txtGestionPrepose = new Text("Gestion préposés");
        Button btnAjouterPrepose = new Button("Ajouter un préposé");
        
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
        
        btnConnexion.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				for (IdentifiantsPrepose identifiants : lstIdentifiants) {
					if (txtfldNoEmploye.getText().equals(identifiants.strIdentifiant) && txtfldMotDePasse.getText().equals(identifiants.strMotDePasse)) {
						//Si l'utilisateur est administrateur, ouvrir la fênetre administrateur sinon ouvrir préposé
						if (identifiants.strType.equals("Administrateur")) {
							arg0.setScene(sceneAdmin);
							arg0.setTitle("Gèrer les préposés");
							break;
						}
						else {
//							arg0.setScene(scenePrepose);
							break;
						}
					}
					else if (txtfldNoEmploye.getText().trim().isEmpty()) {
						Alert fenetreNoEmployeManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
						
						fenetreNoEmployeManquant.setTitle("Erreur");
						Stage stage = (Stage) fenetreNoEmployeManquant.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("icon-erreur.png"));
						fenetreNoEmployeManquant.setContentText("Vous avez oublié d'inscrire le numéro d'employé.");
						fenetreNoEmployeManquant.setHeaderText(null);
						fenetreNoEmployeManquant.showAndWait();
					}
					else if (txtfldMotDePasse.getText().trim().isEmpty()) {
						Alert fenetreMotDePasseManquant = new Alert(AlertType.NONE, "default Dialog",ButtonType.OK);
						
						fenetreMotDePasseManquant.setTitle("Erreur");
						Stage stage = (Stage) fenetreMotDePasseManquant.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("icon-erreur.png"));
						fenetreMotDePasseManquant.setContentText("Vous avez oublié d'inscrire le mot de passe.");
						fenetreMotDePasseManquant.setHeaderText(null);
						fenetreMotDePasseManquant.showAndWait();
					}
					else if (((!txtfldNoEmploye.getText().equals(identifiants.strIdentifiant) && !(txtfldNoEmploye.getText().trim().isEmpty())) || (!txtfldMotDePasse.getText().equals(identifiants.strMotDePasse) && !(txtfldMotDePasse.getText().trim().isEmpty())))) {
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
        });
		
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
	
}
