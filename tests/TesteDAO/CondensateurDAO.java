package TesteDAO;




import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import java.util.*;
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
import modele.Condensateur;

public class CondensateurDAO {
private DAOFactory factory;
	
	@Test(groups = "condensateur")
	public void getFromID() throws SQLException {
		Condensateur con= factory.getCondensateurDAO().getFromID("C1");
		assertNotNull(con);//verifie synthaxe de la requete est a null si elle a null la requete est a null
		assertEquals(con.getFkCode(), "C1");
		assertEquals(con.getCapaciter(), 100.00);
		assertEquals(con.getTension_max(), 30);
		assertEquals(con.getElectro_con(), false);
		System.out.println(con);
	}
	@Test
	public void getList()
	{
		List<Condensateur> liste = new ArrayList();
		liste = factory.getCondensateurDAO().getListe("");
		for(int i = 0; i<liste.size();i++) {
			System.out.println(liste.get(i).toString());
		}
	}
	@Test
	public void insert() throws Exception {
		int choix;
		Scanner scaner = new Scanner(System.in);
		List<Composant> liste = new ArrayList();
		liste = factory.getComposantDAO().getListeCom();
		for(int i = 0; i<liste.size();i++) {
			System.out.println(liste.get(i).toString());
		}
		System.out.println("Veuillez");
		choix =scaner.nextInt();
		while(choix > liste.size()-1 || choix <0) {
			choix =scaner.nextInt();
		}
		
		Condensateur con = new Condensateur( liste.get(choix).getCode_comp(), 55, 69,false);
		factory.getCondensateurDAO().insert(con);
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