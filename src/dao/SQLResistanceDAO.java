package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modele.Resistance;

public  abstract class  SQLResistanceDAO implements IResistanceDAO{
	private static String sqlFromId = "Select FKCODE_RES,OHMS_RES,WATT_RES from TRESISTANCE where FKCODE_RES=?";
	
	private static String sqlListeRes = "Select FKCODE_RES,OHMS_RES,WATT_RES from TRESISTANCE";
	private static String sqlListeFkCode = "Select distinct r.FKCODE_RES from TRESISTANCE r";
	private static String sqlInsert = "INSERT INTO TResistance (FKCODE_RES, OHMS_RES,WATT_RES) VALUES (?,?,?)";
	private static String sqlDelete = "Delete from TResistance WHERE FKCODE_RES=?";

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(SQLResistanceDAO.class);

	// La factory pour avoir la connexion
	private final SQLDAOFactory factory;

	/**
	 * Construction pour avoir l'accès à la factory et ainsi obtenir la connexion
	 * 
	 * @param factory
	 */
	public SQLResistanceDAO(SQLDAOFactory factory) {
		this.factory = factory;
	}

	@Override
	public Resistance getFromID(String id) {

		Resistance res = null;
		if (id != null)
			id = id.trim();
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlFromId);) {
			ResultSet rs;
			query.setString(1,id);
			// exécution
			rs = query.executeQuery();
			// parcourt du ResultSet
			if (rs.next()) {
				res = new Resistance(id, rs.getDouble(2),rs.getDouble(3));
				logger.debug("Création d'un Condensateur");
				}
		} catch (SQLException e) {
			logger.error("Erreur SQL pour créer une Condensateur", e);
		}
		return res;
		}

	@Override
	public List<Resistance> getListe(String regExpr) {
		List<Resistance> liste = new ArrayList<>();
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlListeRes)) {
			Resistance res;
			ResultSet rs;

			rs = query.executeQuery();
			while (rs.next()) {
				res = new Resistance(rs.getString(1), rs.getDouble(2), rs.getDouble(3)); // Type du constructeur de la classe composant
				liste.add(res);
			}
		} catch (SQLException e) {
			logger.error("Erreur lors du chargement des resistance feuilles", e);
		}
		return liste;
	}
	public List<String> getListeRes(String regExpr){
		List<String> liste = new ArrayList<>();
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlListeFkCode)) {
			String res;
			ResultSet rs;

			rs = query.executeQuery();
			while (rs.next()) {
				res = rs.getString(1).trim();
				liste.add(res);
			}
		} catch (SQLException e) {
			logger.error("Erreur lors du chargement des resistances", e);
		}
		return liste;
	}
	@Override
	public String insert(Resistance r) throws Exception {
		if (r == null)
			return null;
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlInsert)) {
			query.setString(1, r.getFkCode());
			query.setDouble(2, r.getOhms_res());
			query.setDouble(3,r.getWatt_res() );
			if (!r.getFkCode().equalsIgnoreCase("") && r.getOhms_res() != 0 && r.getWatt_res() != 0) {
				query.executeUpdate();
				query.getConnection().commit();
			}
		} catch (SQLException e) {
			logger.error("Erreur d'insertion du Resistance", e);
			this.factory.getConnexion().rollback();
			dispatchSpecificException(e);
		}
		return r.getFkCode();
	}

	

	@Override
	public boolean update(Resistance object) throws Exception {
		throw new OperationNotSupportedException();

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
