package services;

import java.util.Observable;

/**
 * Adapation de la classe Observable pour éviter que l'objet Observé doive
 * hériter de la classe Observable Elle rend public la méthode setChanged(),
 * pour permettre d'utiliser la délégation à la place de l'héritage
 * 
 */
public class ObservableSource extends Observable {

	/**
	 * rend la méthode setChanged public pour permettre d'utiliser la délégation à
	 * la place de héritage
	 */
	public void setChanged() {
		super.setChanged();
	}
}
