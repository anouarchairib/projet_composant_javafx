package dao;


import java.util.List;

import modele.Resistance;
public interface IResistanceDAO extends IDAO <Resistance, String> {
	public List<String> getListeRes(String regExpr);
}
