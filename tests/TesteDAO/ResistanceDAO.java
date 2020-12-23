package TesteDAO;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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

import modele.Resistance;

public class ResistanceDAO {
	private DAOFactory factory;

	@Test(groups = "resistance")
	public void getFromID() throws SQLException {
		Resistance res= factory.getResistanceDAO().getFromID("R1");
		System.out.println("hi"+res);
		assertNotNull(res);
		assertEquals(res.getFkCode(), "R1");
		assertEquals(res.getOhms_res(), 47.0);
		assertEquals(res.getWatt_res(), 0.25);
		System.out.println(res);
	}
	@Test
	public void getList()
	{
		List<Resistance> liste = new ArrayList();
		liste = factory.getResistanceDAO().getListe("");
		for(int i = 0; i<liste.size();i++) {
			System.out.println(liste.get(i).toString());
		}
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
