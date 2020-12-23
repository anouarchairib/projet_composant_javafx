package controleurs;
import java.awt.HeadlessException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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


import dao.IResistanceDAO;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import modele.Resistance;
import services.Facade;
public class ListResistanceCTRL implements Initializable, Observer{
	@FXML private TableView<Resistance> tbResistance;
	@FXML private TableColumn<Resistance,String> ref;
	@FXML private TableColumn<Resistance,String> ohm;
	@FXML private TableColumn<Resistance,String> watt;
	@FXML private Button exit;

	private ObservableList<Resistance> loResistance;
	private ObservableList<String> loRes;
	private Resistance res;
	private IResistanceDAO ResistanceDao;
	private Facade facade;
	public void setUp(Facade facade) {
    	this.facade = facade;
    	ResistanceDao = facade.getResistanceDAO();
    	initiliseDonnees();
    	facade.addServeurObserver(this);
    }
	@FXML
	 void exit()  {
		Stage stage = (Stage)exit.getScene().getWindow();
		stage.close();
	}
	private void initiliseDonnees() {
        List<Resistance> liste = facade.getResistanceDAO().getListe("");
        loResistance = FXCollections.observableArrayList(liste);
    	List<String> listeResistance = facade.getResistanceDAO().getListeRes(""); 
        loRes = FXCollections.observableArrayList(listeResistance);
        tbResistance.setItems(loResistance);
      }
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		ref.setCellValueFactory(new PropertyValueFactory<Resistance, String>("fkCode"));
    	ohm.setCellValueFactory(new PropertyValueFactory<Resistance, String>("ohms_res"));
    	watt.setCellValueFactory(new PropertyValueFactory<Resistance, String>("watt_res"));
    	
    	
    	ref.setStyle("-fx-alignment: center;");
		ohm.setStyle("-fx-alignment: center;");
		watt.setStyle("-fx-alignment: center;");
		
	}
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
}
