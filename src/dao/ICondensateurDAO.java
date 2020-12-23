package dao;

import java.util.List;

import modele.Condensateur;
public interface ICondensateurDAO extends IDAO<Condensateur, String>{
	public List<String> getListeCon(String regExpr);
}
