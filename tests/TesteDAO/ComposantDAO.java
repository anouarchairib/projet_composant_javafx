package TesteDAO;


import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import dao.DAOFactory;
import dao.DAOFactory.TypePersistance;
import databases.ConnexionFromFile;
import databases.ConnexionSingleton;
import databases.Databases;
import databases.PersistanceException;
import modele.Composant;

public class ComposantDAO {
private DAOFactory factory;
	//Permet de teste la methode qui permet de recuperer la ligne selon la cle primaire passer en para
	@Test(groups = "composant")
	public void getFromID() throws SQLException {
		Composant com= factory.getComposantDAO().getFromID("C1");
		assertNotNull(com);//verifie synthaxe de la requete est a null si elle a null la requete est incorecte
		assertEquals(com.getCode_comp(), "C1");//Verifie que la cle passer en parametre corspond a la cle entrer en paramettre 
		assertEquals(com.getPrix_com(), 0.10);
		assertEquals(com.getQt_com(), 50);
		System.out.println(com);
	}
	@Test
	public void getList()
	{
		List<Composant> liste = new ArrayList();
		liste = factory.getComposantDAO().getListe("");
		for(int i = 0; i<liste.size();i++) {
			System.out.println(liste.get(i).toString());
		}
	}
	@Test
	public void insert() throws Exception{
		Composant com = new Composant("V55", 55.2, 69);
		factory.getComposantDAO().insert(com);
	}
	@Test
	public void listCom() {//Recupere que les composant 
		List<Composant> liste = new ArrayList();
		liste = factory.getComposantDAO().getListeCom();
		for(int i = 0; i<liste.size();i++) {
			System.out.println(liste.get(i).getCode_comp());
		}
	}
	@Test(groups="composant")
	public void updateComposant() throws Exception{
		Composant c = new Composant("R1", 0.10, 50);
		assertTrue(factory.getComposantDAO().update(c));
	}

	@BeforeClass
	public void beforeClass() throws PersistanceException {
		ConnexionSingleton.setInfoConnexion(
				new ConnexionFromFile("./ressources/connexion_composant.properties", Databases.FIREBIRD));//va lancer la connexion appartir de de fichie properties
		factory = DAOFactory.getDAOFactory(TypePersistance.FIREBIRD,(ConnexionSingleton.getConnexion()));
	}
	@AfterClass
	public void afterClass() {
		ConnexionSingleton.liberationConnexion();
	}
}