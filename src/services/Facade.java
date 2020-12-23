package services;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.DAOFactory;
import dao.DAOFactory.TypePersistance;
import dao.IComposantDAO;
import dao.ICondensateurDAO;
import dao.IResistanceDAO;
import dao.IComposantDAO;
import dao.SQLComposantDAO;
import databases.ConnexionFromFile;
import databases.ConnexionSingleton;
import databases.Databases;
import databases.PersistanceException;


public class Facade {
	// Logger
	private static final Logger logger = LoggerFactory.getLogger(Facade.class);
	private DAOFactory fabrique;
	private IComposantDAO ComposantIDAO;
	private ICondensateurDAO condensateurDAO;
	private IResistanceDAO resistanceDAO;
	

	private ObservableSource canalServeur;

	public Facade() {
		// crée une fabrique pour les vues
		try {
			ConnexionSingleton.setInfoConnexion(
					new ConnexionFromFile("./ressources/connexion_composant.properties", Databases.FIREBIRD));
			fabrique = DAOFactory.getDAOFactory(TypePersistance.FIREBIRD, ConnexionSingleton.getConnexion());

			// charge les DAO
			// DAO Serveur
			ComposantIDAO = fabrique.getComposantDAO();
			condensateurDAO = fabrique.getCondensateurDAO();
			resistanceDAO = fabrique.getResistanceDAO();
			
			// Observable Objects
			canalServeur = new ObservableSource();

		} catch (PersistanceException e) {
			// showErreur(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * @return the fabrique
	 */
	public DAOFactory getFabrique() {
		return fabrique;
	}

	/**
	 * @return the ComposantDAO
	 */
	public IComposantDAO getComposantDAO() {
		return ComposantIDAO;
	}

	/**
	 * @return the condensateurDAO
	 */
	public ICondensateurDAO getCondensateurDAO() {
		return condensateurDAO;
	}
	
	/**
	 * @return the resistanceDAO
	 */
	public IResistanceDAO getResistanceDAO() {
		return resistanceDAO;
	}

	/**
	 * @return the historiqueDAO
	 */


	/**
	 * @param o
	 * @see java.util.Observable#addObserver(java.util.Observer)
	 */
	public void addServeurObserver(Observer o) {
		canalServeur.addObserver(o);
	}

	/**
	 * @param o
	 * @see java.util.Observable#deleteObserver(java.util.Observer)
	 */
	public void deleteServeurObserver(Observer o) {
		canalServeur.deleteObserver(o);
	}

	/**
	 * @param arg permet d'envoyer un objet pour les écouteur
	 * @see java.util.Observable#notifyObservers(java.lang.Object)
	 */
	public void notifyServeurObservers(Object arg) {
		// indique un changement
		canalServeur.setChanged();
		// averti les écouteurs
		canalServeur.notifyObservers(arg);
	}

	/**
	 * Averti les écouteurs sans envoyer d'objet
	 * 
	 * @see java.util.Observable#notifyObservers()
	 */
	public void notifyServeurObservers() {
		// indique un changement
		canalServeur.setChanged();
		// averti les écouteurs
		canalServeur.notifyObservers();
	}

}