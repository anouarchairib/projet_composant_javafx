package dao;

import java.sql.Connection;

/**
 * Fabrique concrète pour Firebird
 * 
 * @author Didier
 *
 */
public class FBDAOFactory extends SQLDAOFactory {

	public FBDAOFactory(Connection connexion) {
		super(connexion);
	}

	/**
	 * retourne une implémentation DAOCategorie pour Firebird
	 */
	@Override
	public IComposantDAO getComposantDAO() {
		return new FBComposantDAO(this);
	}
	public ICondensateurDAO getCondensateurDAO() 
	{
		return new FBCondensateurDAO(this);
	}
	public IResistanceDAO getResistanceDAO() 
	{
		return new FBResistanceDAO(this);
	}

}
