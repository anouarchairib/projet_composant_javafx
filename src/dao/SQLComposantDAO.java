package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modele.Composant;

public abstract class SQLComposantDAO implements IComposantDAO {
	private static String sqlFromId = "Select * from TComposant where CODE_COM=?";
	private static String sqlListeCom = "Select CODE_COM,PRIX_COM,QT_COM from TComposant";
	private static String sqlInsert = "INSERT INTO TComposant (CODE_COM,PRIX_COM,QT_COM) VALUES (?,?,?)";
	private static String sqlDelete = "Delete from TComposant WHERE CODE_COM=?";
	private static String sqlUpdate = "UPDATE TComposant set QT_COM  = ? where trim(CODE_COM) = ?";
	private static String sqlComp ="Select * from TCOMPOSANT c where c.CODE_COM not in (Select co.FKCODE_CON from TCONDENSATEUR co ) and c.CODE_COM not in (select r.FKCODE_RES from TRESISTANCE r )";
	// Logger
	private static final Logger logger = LoggerFactory.getLogger(SQLComposantDAO.class);

	// La factory pour avoir la connexion
	private final SQLDAOFactory factory;

	/**
	 * Construction pour avoir l'accès à la factory et ainsi obtenir la connexion
	 * 
	 * @param factory
	 */
	public SQLComposantDAO(SQLDAOFactory factory) {
		this.factory = factory;
	}

	@Override
	public Composant getFromID(String id) {

		Composant com = null;
		if (id != null)
			id = id.trim();
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlFromId);) {
			ResultSet rs;
			query.setString(1,id);
			// exécution
			rs = query.executeQuery();
			// parcourt du ResultSet
			if (rs.next()) {
				com = new Composant(id, rs.getDouble(2),rs.getInt(3));
				logger.debug("Création d'un composant");
				}
		} catch (SQLException e) {
			logger.error("Erreur SQL pour créer une composant", e);
		}
		return com;
		}
	public List<Composant> getListe(String regExpr) {
		List<Composant> liste = new ArrayList<>();
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlListeCom)) {
			Composant com;
			ResultSet rs;
			rs = query.executeQuery();
			while (rs.next()) {
				com = new Composant(rs.getString(1), rs.getDouble(2), rs.getInt(3)); // Type du constructeur de la classe composant
				liste.add(com);
				}
		} catch (SQLException e) {
			logger.error("Erreur lors du chargement des catégories feuilles", e);
		}
		return liste;
	}
	public String insert(Composant c) throws Exception {
		if (c == null)
			return null;
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlInsert)) {
			query.setString(1, c.getCode_comp());
			query.setDouble(2, c.getPrix_com());
			query.setInt(3, c.getQt_com());
			if (!c.getCode_comp().equalsIgnoreCase("") && c.getPrix_com() != 0 && c.getQt_com() != 0) {
				query.executeUpdate();
				query.getConnection().commit();
			}
		} catch (SQLException e) {
			logger.error("Erreur d'insertion du Composant", e);
			this.factory.getConnexion().rollback();
			dispatchSpecificException(e);
		}
		return c.getCode_comp();
	}
	public List<Composant> getListeCom(){
		List<Composant> liste = new ArrayList<>();
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlComp)) {
			Composant com;
			ResultSet rs;
			rs = query.executeQuery();
			while (rs.next()) {
				com = new Composant(rs.getString(1), rs.getDouble(2), rs.getInt(3)); // Type du constructeur de la classe composant
				liste.add(com);
				}
		} catch (SQLException e) {
			logger.error("Erreur lors du chargement des catégories feuilles", e);
		}
		return liste;
	}




	public boolean update(Composant c) throws Exception {
		boolean update = false;
		if (c.getCode_comp() == null || c.getPrix_com() == 0 || c.getQt_com() <= 0) {
			update = false;
		} else {
			try (PreparedStatement query = this.factory.getConnexion().prepareStatement(sqlUpdate)) {
				query.setInt(1, c.getQt_com());
				query.setString(2, c.getCode_comp());
				query.execute();
				query.getConnection().commit();
			} catch (SQLException e) {
				logger.error("Erreur de mise Ã  jour du composant " + c.toString(), e);
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