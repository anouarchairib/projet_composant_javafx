package dao;



import java.util.List;

import modele.Composant;

public interface IComposantDAO extends IDAO<Composant, String> {
	public List<Composant> getListeCom();
}
