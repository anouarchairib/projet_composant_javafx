package controleurs;
import java.awt.HeadlessException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;


import dao.ICondensateurDAO;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import modele.Composant;
import modele.Condensateur;
import services.Facade;
public class ListCondensateurCTRL implements Initializable, Observer {

	@FXML private TableView<Condensateur> tbCondensateur;
	@FXML private TableColumn<Condensateur,String> reference;
	@FXML private TableColumn<Condensateur,String> capacite;
	@FXML private TableColumn<Condensateur,String> tension;
	@FXML  private TableColumn<Condensateur,String> electro;
	@FXML private Button btExit;

	private ObservableList<Condensateur> loCondensateur;
	private ObservableList<String> loCon;
	private Condensateur con;
	private ICondensateurDAO condensateurDao;
	private Facade facade;
	public void setUp(Facade facade) {
    	this.facade = facade;
    	condensateurDao = facade.getCondensateurDAO();
    	initiliseDonnees();
    	
    	facade.addServeurObserver(this);
    }
	@FXML
	 void exit()  {
		Stage stage = (Stage)btExit.getScene().getWindow();
		stage.close();
	}
	private void initiliseDonnees() {
        List<Condensateur> liste = facade.getCondensateurDAO().getListe("");
        loCondensateur = FXCollections.observableArrayList(liste);
    	List<String> listeCondensateur = facade.getCondensateurDAO().getListeCon(""); 
        loCon = FXCollections.observableArrayList(listeCondensateur);
      
        tbCondensateur.setItems(loCondensateur);
      }
	public void initialize(URL arg0, ResourceBundle arg1) {
		reference.setCellValueFactory(new PropertyValueFactory<Condensateur, String>("fkCode"));
    	capacite.setCellValueFactory(new PropertyValueFactory<Condensateur, String>("capaciter"));
    	tension.setCellValueFactory(new PropertyValueFactory<Condensateur, String>("tension_max"));
    	electro.setCellValueFactory(new PropertyValueFactory<Condensateur, String>("electro_con"));
    	
    	
    	reference.setStyle("-fx-alignment: center;");
		capacite.setStyle("-fx-alignment: center;");
		tension.setStyle("-fx-alignment: center;");
		electro.setStyle("-fx-alignment: center;");
		
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		
	}



}
