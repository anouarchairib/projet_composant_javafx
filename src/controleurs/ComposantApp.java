package controleurs;

import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JSpinner.ListEditor;

import com.sun.media.jfxmedia.logging.Logger;

import dao.IComposantDAO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import modele.Composant;
import modele.Resistance;
import services.Facade;
//la facade va servir d intermediaire entre la vue est mes controlleur
public class ComposantApp  extends Application implements Observer{
	/*********************************************************/
	private final Locale DEFAULT_LOCALE = new Locale("fr", "BE");
	private IComposantDAO ComposantDAO;
	// Locale par dÃ©faut de l'application
	private Locale locale = DEFAULT_LOCALE;
	private Label lbl;
	private List<Composant> listeCom ;
	private Facade facade;
	@FXML private TableColumn<Composant, String> tbCode;
	@FXML private TableColumn<Composant, Double> tbPrix;
	@FXML private TableColumn<Composant, Integer> tbQt;
	
	//@FXML private Button btRefresh;
	private Button btComposant;
	private Button btListCondensateur;
	private Button btListResistance; 
	private Button btUpdate;
	private Button btTest; 
	private Button btReset;
	private BooleanProperty modif = new SimpleBooleanProperty(false); 
	private Set<String> composantModif = new HashSet<>();
	private ObservableList<Composant> loComposant;
	private ComposantCTRL composantctrl;
	@FXML private TableView<Composant> tbComposant;
	// Conteneur principal de la vue principale
	private BorderPane contentPane;
	@Override
	
	public void start(Stage primaryStage) throws IOException {
		// CrÃ©ation du conteneur principal
		contentPane = new BorderPane();
		//CrÃ©ation de la facade
		facade = new Facade();
		facade.addServeurObserver(this);//il va etre abonner pour etre tenue au courrant des modification 
		ComposantDAO = facade.getComposantDAO();
		// Titre de l'aplication
		lbl = new Label("  Liste Composant  ");
		lbl.setId("AccueilComposant");// id pour CSS
		contentPane.setPadding(new Insets(10, 10, 10, 10));
		// La taille doit etre celle du conteneur
		lbl.prefWidthProperty().bind(contentPane.widthProperty());
		lbl.setAlignment(Pos.CENTER);
		lbl.setPadding(new Insets(10, 10, 10,10));
		contentPane.setTop(lbl);
		contentPane.setStyle("-fx-background-color: orange;");

		// Bouton pour afficher la vueAjoutComposant
		btComposant= new Button("Ajout Composant");
		btComposant.setPrefWidth(200);
		btComposant.setOnAction(a -> {
			actionAjoutComposant(primaryStage);	
		});
		//Bouton pour afficher la VueListeCondensateur
		btListCondensateur = new Button("Liste des condensateurs");
		btListCondensateur.setPrefWidth(200);
		btListCondensateur.setOnAction(a -> {
			actionListCondensateur(primaryStage);
		});
		btListResistance = new Button("Liste des Resistances");
		btListResistance.setPrefWidth(200);
		btListResistance.setOnAction(a -> {
			actionListResistance(primaryStage);
		});
	/*	btRefresh = new Button("Rafraichir la page");
		btRefresh.setPrefWidth(200);
		btRefresh.setOnAction(a -> {
			try {
				start(primaryStage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});*/
		
		//reset
		btReset = new Button("Reset");
		btReset.setPrefWidth(200);
		btReset.setOnAction(a ->{
			actionReset();
		});
		//update
		btUpdate = new Button("Mise a jour");
		btUpdate.setPrefWidth(200);
		btUpdate.setOnAction(a ->{
			actionMiseAJour();
		});
		// Bouton pour afficher la vueComposant
		// Affiche la liste des voitures dans le parking
		tbComposant = new TableView<>();
		tbCode = new TableColumn<>("Reference Composant");
		tbPrix = new TableColumn<>("Prix");
		tbQt = new TableColumn<>("Quantiter");
		contentPane.setCenter(tbComposant);

		// Ajout des  boutons
		//Pane pour les boutons
		TilePane lstBoutons = new TilePane();
		lstBoutons.getChildren().addAll(btComposant);
		lstBoutons.getChildren().addAll(btListCondensateur);
		lstBoutons.getChildren().addAll(btListResistance);
		//lstBoutons.getChildren().addAll(btRefresh);
		lstBoutons.getChildren().addAll(btUpdate);
		lstBoutons.getChildren().addAll(btReset);
		
		lstBoutons.setPadding(new Insets(0,10,0,10));
		// rajoute le conteneur des boutons Ã  droite dans le cp
		contentPane.setRight(lstBoutons);
		btUpdate.disableProperty().bind(modif.not());
		btReset.disableProperty().bind(modif.not());
		// crÃ©ation d'une scÃ¨ne avec son conteneur parent
		Scene scene = new Scene(contentPane, 1100, 700);
		scene.getStylesheets().add("/vues/css/vues.css");
		primaryStage.setTitle("Composant");
		// Ajout de la scÃ¨ne Ã  la Stage
		primaryStage.setScene(scene);
		
		primaryStage.show();
		initiliseDonnees();
	}
	private void initiliseDonnees() {
		
		listeCom = ComposantDAO.getListe("");
		loComposant = FXCollections.observableArrayList(listeCom);
		tbCode.setCellValueFactory(new PropertyValueFactory<Composant, String>("code_comp"));
		tbPrix.setCellValueFactory(new PropertyValueFactory<Composant,Double>("prix_com"));		
		tbQt.setCellValueFactory(new PropertyValueFactory<Composant,Integer>("qt_com"));//par raport au modele
		tbQt.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		tbComposant.setEditable(true);
		tbQt.setOnEditCommit(comp -> {
			comp.getRowValue().setQt_com(comp.getNewValue());
			composantModif.add(comp.getRowValue().getCode_comp());
			modif.set(true);

		});
		
		tbComposant.getColumns().addAll(tbCode,tbPrix,tbQt);
		tbComposant.setItems(loComposant);

		
	}
	public Stage actionListCondensateur(Stage primaryStage) {
		ResourceBundle bundles;
		// Création d'une nouvelle fenêtre
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(primaryStage);
		// Position lors de l'ouverture;
		stage.setX(primaryStage.getX() + 100);
		stage.setY(primaryStage.getY() + 40);
		FXMLLoader root = new FXMLLoader(getClass().getResource("/vues/VueListCondensateur.fxml"));
		try {
			bundles = ResourceBundle.getBundle("vues.bundles.vueListCondensateur", locale);
			root.setResources(bundles);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AnchorPane anch;
		try {
			anch = root.load();
			ListCondensateurCTRL listCondensateurCtrl = root.getController();
			System.out.println("oki"+ listCondensateurCtrl);
			listCondensateurCtrl.setUp(facade);
			stage.setScene(new Scene(anch));
			stage.showAndWait();

		} catch (IOException e) {
			showErreur("Impossible de charger la vueListeCondensateur: " + e.getMessage());
			stage = null;
		}
		return stage;
	} 
	public Stage actionListResistance(Stage primaryStage) {
		ResourceBundle bundles;
		// Création d'une nouvelle fenêtre
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(primaryStage);
		// Position lors de l'ouverture;
		stage.setX(primaryStage.getX() + 100);
		stage.setY(primaryStage.getY() + 40);
		FXMLLoader root = new FXMLLoader(getClass().getResource("/vues/VueListResistance.fxml"));
		try {
			bundles = ResourceBundle.getBundle("vues.bundles.vueListResistance", locale);
			root.setResources(bundles);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AnchorPane anch;
		try {
			System.out.println("OK1");
			anch = root.load();
			ListResistanceCTRL listResistanceCTRL = root.getController();
			System.out.println("oki"+ listResistanceCTRL);
			listResistanceCTRL.setUp(facade);
			stage.setScene(new Scene(anch));
			stage.showAndWait();

		} catch (IOException e) {
			showErreur("Impossible de charger la vueListeResistance: " + e.getMessage());
			stage = null;
		}
		return stage;
	}
	public Stage actionAjoutComposant(Stage primaryStage) {
		ResourceBundle bundles;
		// Création d'une nouvelle fenêtre
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(primaryStage);
		// Position lors de l'ouverture;
		stage.setX(primaryStage.getX() + 100);
		stage.setY(primaryStage.getY() + 40);
		FXMLLoader root = new FXMLLoader(getClass().getResource("/vues/VueCondensateur.fxml"));
		try {
			bundles = ResourceBundle.getBundle("vues.bundles.vueComposant", locale);
			root.setResources(bundles);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AnchorPane anch;
		try {
			anch = root.load();
			composantctrl = root.getController();
			composantctrl.setUp(facade);
			stage.setScene(new Scene(anch));
			stage.showAndWait();

		} catch (IOException e) {
			showErreur("Impossible de charger la vueAjoutComposant: " + e.getMessage());
			stage = null;
		}
		return stage;
	}
	public void actionMiseAJour() {
		Composant com;
		List<String> list =new ArrayList<>();
		System.out.println(composantModif);
		for(String code : composantModif){
			try {
				ComposantDAO.update(tbComposant.getItems().filtered((comp)->comp.getCode_comp().equals(code)).get(0));
				list.add(code);
				JOptionPane.showMessageDialog(null, "liste modifier du composant" );
			}
			catch(Exception e) 
			{
				e.printStackTrace();
			}

		}
		composantModif.removeAll(list);	
	}
	public void actionReset() {
		List<String> list = new ArrayList<String>();
		for(String code : composantModif) {
			try {
				Composant bdCom = ComposantDAO.getFromID(code);
				tbComposant.getItems().replaceAll(o -> o.getCode_comp().equals(code) ? bdCom : o);
				list.add(code);
			}catch(Exception e1) {
				showErreur(e1.getMessage());
			}
		}
		composantModif.removeAll(list);
		if(composantModif.isEmpty()) {
			modif.set(false);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void update(Observable arg0, Object arg1) {	
		listeCom = ComposantDAO.getListe("");
		loComposant = FXCollections.observableArrayList(listeCom);
		tbComposant.setItems(loComposant);
	}
	private void showErreur(String message) {
		Alert a = new Alert(AlertType.ERROR, message);
		a.showAndWait();
	}
}