package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modele.Condensateur;
import modele.Condensateur;

public abstract class SQLCondensateurDAO implements ICondensateurDAO {
	private static String sqlFromId = "Select * from TCondensateur where FKCODE_CON=?";
	
	private static String sqlListeCom = "Select * from TCondensateur";
	private static String sqlListeFkCode = "Select distinct h.FKCODE_CON from TCONDENSATEUR h";
	private static String sqlInsert = "INSERT INTO TCondensateur (FKCODE_CON,CAPACITE_CON,TENSION_MAX,ELECTRO_CON) VALUES (?,?,?,?)";
	private static String sqlDelete = "Delete from TCondensateur WHERE FKCODE_CON=?";
	private static String sqlUpdate = "UPDATE TCondensateur set CAPACITE_CON = ? and TENSION_MAX  = ? where FKCODE_CON = ?";
	
	// Logger
	private static final Logger logger = LoggerFactory.getLogger(SQLCondensateurDAO.class);

	// La factory pour avoir la connexion
	private final SQLDAOFactory factory;

	/**
	 * Construction pour avoir l'accès à la factory et ainsi obtenir la connexion
	 * 
	 * @param factory
	 */
	public SQLCondensateurDAO(SQLDAOFactory factory) {
		this.factory = factory;
	}

	@Override
	public Condensateur getFromID(String id) {

		Condensateur com = null;
		if (id != null)
			id = id.trim();
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlFromId);) {
			ResultSet rs;
			query.setString(1,id);
			// exécution
			rs = query.executeQuery();
			// parcourt du ResultSet
			if (rs.next()) {
				com = new Condensateur(id, rs.getFloat(2),rs.getInt(3),rs.getBoolean(4));
				logger.debug("Création d'un Condensateur");
				}
		} catch (SQLException e) {
			logger.error("Erreur SQL pour créer une Condensateur", e);
		}
		return com;
		}
	public List<Condensateur> getListe(String regExpr) {
		List<Condensateur> liste = new ArrayList<>();
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlListeCom)) {
			Condensateur com;
			ResultSet rs;
			rs = query.executeQuery();
			while (rs.next()) {
				com = new Condensateur(rs.getString(1), rs.getFloat(2), rs.getInt(3),rs.getBoolean(4)); // Type du constructeur de la classe Condensateur
				liste.add(com);
				}
		} catch (SQLException e) {
			logger.error("Erreur lors du chargement des catégories feuilles", e);
		}
		return liste;
	}
	public String insert(Condensateur c) throws Exception {
		if (c == null)
			return null;
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlInsert)) {
			query.setString(1, c.getFkCode());
			query.setDouble(2, c.getCapaciter());
			query.setInt(3, c.getTension_max());
			query.setBoolean(4, c.getElectro_con());
			if (!c.getFkCode().equalsIgnoreCase("") && c.getCapaciter() != 0 && c.getTension_max() != 0 ) {
				query.executeUpdate();
				query.getConnection().commit();
			}
		} catch (SQLException e) {
			logger.error("Erreur d'insertion du Composant", e);
			this.factory.getConnexion().rollback();
			dispatchSpecificException(e);
		}
		return c.getFkCode();
	}

	public List<String> getListeCon(String regExpr) {
		List<String> liste = new ArrayList<>();
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlListeFkCode)) {
			String con;
			ResultSet rs;

			rs = query.executeQuery();
			while (rs.next()) {
				con = rs.getString(1).trim();
				liste.add(con);
			}
		} catch (SQLException e) {
			logger.error("Erreur lors du chargement des Condensateur", e);
		}
		return liste;
	}

	/**
	 * Supprime une Condensateure que si c'est une feuille
	 */
	public boolean update(Condensateur c) throws Exception {
		boolean update = false;
		if (c.getFkCode() == null || c.getCapaciter() == 0 || c.getTension_max() <= 0) {
			update = false;
		} else {
			try (PreparedStatement query = this.factory.getConnexion().prepareStatement(sqlUpdate)) {
				query.setString(1, c.getFkCode());
				query.setDouble(2, c.getCapaciter());
				query.setInt(3, c.getTension_max());
				query.setBoolean(4, c.getElectro_con());
				query.execute();
				query.getConnection().commit();
			} catch (SQLException e) {
				logger.error("Erreur de mise Ã  jour du Condensateur " + c.toString(), e);
				this.factory.getConnexion().rollback();
				 dispatchSpecificException(e);
			}
			update = true;
		}
		return update;
	}
	private static void dispatchSpecificException(SQLException e) throws AnouarSQLException{
		
		switch (e.getErrorCode()) {
		case 335544665:// PK
			throw new AnouarPKException(e.getMessage(), e.getErrorCode(), "CODE");
		case 335544347:// Contrainte unicitÃ©, check
			throw new AnouarConstraintException(e.getMessage(), e.getErrorCode(), extractField(e.getMessage()));
		default:
			throw new AnouarSQLException(e.getMessage(), e.getErrorCode());
		}
	}
	
	private static String extractField(String msg) {
		return msg.substring(msg.indexOf('.')+2, msg.indexOf(',')-5);	
	}
	}