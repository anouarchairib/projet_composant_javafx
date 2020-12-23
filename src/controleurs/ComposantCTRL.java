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

import dao.AnouarConstraintException;
import dao.AnouarPKException;
//import controleurs.ComposantApp;
import dao.ExceptionNull;
import dao.IComposantDAO;
import dao.ICondensateurDAO;
import dao.IResistanceDAO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modele.Composant;
import services.Facade;
import modele.Condensateur;
import modele.Resistance;
public class ComposantCTRL implements Initializable, Observer {
    @FXML private TextField refTf;
    @FXML private TextField prixTf;
    @FXML private TextField quantiterTf;
    @FXML private TextField ohmTf;
    @FXML private  TextField electroTf;
    @FXML private TextField lb3Tf;
    @FXML private TextField refCtf;
    @FXML private Button btnAjout;
    @FXML private Button btnExit;
    @FXML private Label ref;
    @FXML private Label lbTitre;
    @FXML private Label lb2;
    @FXML private TextArea texteErreur;
    private BooleanProperty modif = new SimpleBooleanProperty(false);
 @FXML private Label lb3;

    @FXML private RadioButton condensateur;
    @FXML private RadioButton resistance;
    @FXML private ToggleGroup group;
    private Set<TextField>errorField = new HashSet<>();
    private  Composant com ;
    public Composant getCom() {
		return com;
	}

	
	private Resistance  res;
    private Condensateur con;
    private ICondensateurDAO condensateurDao;
    private IComposantDAO composantDAO;
    private IResistanceDAO resistanceDAO;  
    private Facade facade;
    
    public void setUp(Facade facade) {
    	this.facade = facade;
    	condensateurDao = facade.getCondensateurDAO();
    	composantDAO = facade.getComposantDAO();
    	resistanceDAO = facade.getResistanceDAO();
    	facade.addServeurObserver(this);
    	
    }
 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	//ajout pour passe le bt ajout a false
	    refCtf.textProperty().bind(refTf.textProperty());
	    btnAjout.disableProperty().bind(modif.or(refTf.textProperty().isEmpty()).or(prixTf.textProperty().isEmpty()).or(quantiterTf.textProperty().isEmpty()).or(ohmTf.textProperty().isEmpty()).or(lb3Tf.textProperty().isEmpty()));
	}
	@FXML
	 void exit()  {
		Stage stage = (Stage)btnExit.getScene().getWindow();
		stage.close();
	}
	@FXML
	 void ajout()  {
		errorField.forEach(tf->{
			tf.getStyleClass().remove("error");
			
		});
		errorField.clear();
		Boolean bool = true;
		float capacite = Float.parseFloat(ohmTf.getText());
		int tension = Integer.parseInt(lb3Tf.getText());
		double ohm = Double.parseDouble(ohmTf.getText());
		double watt = Double.parseDouble(lb3Tf.getText());
         double prix = Double.parseDouble(prixTf.getText());
         int quantiter = Integer.parseInt(quantiterTf.getText());
         boolean electro = Boolean.parseBoolean(electroTf.getText());
         
         if(refTf.getText().isEmpty() || prixTf.getText().isEmpty() || quantiterTf.getText().isEmpty() || electroTf.getText().isEmpty() ) {
		 if(refTf.getText().isEmpty() || !prixTf.getText().isEmpty() || !quantiterTf.getText().isEmpty())
			JOptionPane.showMessageDialog(null, "Veuillez entrer un reference");
		 else if(!refTf.getText().isEmpty() || prixTf.getText().isEmpty() || !quantiterTf.getText().isEmpty())
			JOptionPane.showMessageDialog(null, "Veuillez entrer un prix");
		 else if(!refTf.getText().isEmpty() || !prixTf.getText().isEmpty() || quantiterTf.getText().isEmpty())
			JOptionPane.showMessageDialog(null, "Veuillez entrer un quantitée");
		 else
			JOptionPane.showMessageDialog(null, "Veuillez entrer une valeur dans les champs");
		   bool = false;
		 }
    
		
		
		
		if(refTf.getText().length() < 2) {
			JOptionPane.showMessageDialog(null, "Attention le composÃ© d'au plus petit- caractÃ¨res");
			bool = false;
		}
			try {
				
				if(composantDAO.getFromID(refTf.getText()) == null) {
					com = new Composant(refTf.getText(),prix,quantiter);//parse counvertir la chaine de caractere dans le type qu on a besoin

				composantDAO.insert(com);
				JOptionPane.showMessageDialog(null, "Insertion du composant" + com.getCode_comp() + "," + com.getPrix_com() + "," + com.getQt_com());
				}
			     if(radioSelect().equals(condensateur)) {
		        	 con = new Condensateur(refTf.getText(), capacite, tension,electro);//parse counvertir la chaine de caractere dans le type qu on a besoin
		        	 condensateurDao.insert(con);
		        	 JOptionPane.showMessageDialog(null, "Insertion du Condensateur" +  con.getFkCode() + "," + con.getCapaciter() + "," + con.getTension_max()+","+con.getElectro_con());
		         }
		         if(radioSelect().equals(resistance)) {
		        	 res= new Resistance(refTf.getText(),ohm,watt);//parse counvertir la chaine de caractere dans le type qu on a besoin
		        	 resistanceDAO.insert(res);
		        	 JOptionPane.showMessageDialog(null, "Insertion de la resisatnce" +  res.getFkCode() + "," + res.getOhms_res() + "," + res.getWatt_res());
		         }
				
				facade.notifyServeurObservers(com);//Avertir au class qui doit etre tenue au courrant qui eux un changement (listCond listResistance et composantApp)
				Stage stage = (Stage) btnAjout.getScene().getWindow();
				stage.close();
			}catch(AnouarPKException e){
				texteErreur.setVisible(true);
				texteErreur.setText("Erreur Pk existant : " + e.getChamp());
		    	errorField.add(refTf);
			}catch (AnouarConstraintException e) {
				texteErreur.setVisible(true);
				//recherche le code i18n et affiche dans le TexteArea
				texteErreur.setText("Erreur contrainte : " + e.getChamp());
				//MÃ©morise la Zone de Texte en erreur
				switch (e.getChamp()) {
				case "REF":
					errorField.add(refTf);
					break;
				case "PRIX":
					errorField.add(prixTf);
					break;
				case "QUANTITER":
					errorField.add(quantiterTf);
					break;
				case "ELECTRO" :
					errorField.add(electroTf);
				default:
					break;
				}

			} catch (Exception e) {
				texteErreur.setVisible(true);
				e.printStackTrace();
				texteErreur.setText("Erreur : " + e.getMessage());
			}
			//Change la classe CSS pour les Zones de Texte en erreur
			// En cas d'erreur Applique la classe css error
			errorField.forEach(tf -> {
				tf.getStyleClass().add("error");
			});
	    }
	@FXML
	private RadioButton radioSelect() {
		if(condensateur.isSelected()) {
			lbTitre.setText("Ajout Condensateur");
			lb3.setText("puissance :");
			return condensateur;
		}
		if(resistance.isSelected()) {
			lbTitre.setText("Ajout Résistance");
			lb3.setText("watt :");
			return resistance;
		}
		return condensateur;
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
