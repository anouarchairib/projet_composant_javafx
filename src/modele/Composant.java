package modele;

public class Composant {
	private String code_comp;
	private double prix_com;
	private int qt_com;
	
	
	public Composant(String code_comp,double prix_com, int qt_com) {
		this.code_comp = code_comp;
		this.prix_com = prix_com;
		this.qt_com = qt_com;
	}
	public String getCode_comp() {
		return code_comp;
	}
	public double getPrix_com() {
		return prix_com;
	}
	public int getQt_com() {
		return qt_com;
	}
	public void setCode_comp(String code_comp) {
		this.code_comp = code_comp;
	}
	public void setPrix_com(double prix_com) {
		this.prix_com = prix_com;
	}
	public void setQt_com(int qt_com) {
		this.qt_com = qt_com;
	}
	
	@Override
	public String toString() {
		return "Composant [code_comp=" + code_comp + ", prix_com=" + prix_com + ", qt_com=" + qt_com + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code_comp == null) ? 0 : code_comp.hashCode());
		long temp;
		temp = Double.doubleToLongBits(prix_com);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + qt_com;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Composant other = (Composant) obj;
		if (code_comp == null) {
			if (other.code_comp != null)
				return false;
		} else if (!code_comp.equals(other.code_comp))
			return false;
		if (Double.doubleToLongBits(prix_com) != Double.doubleToLongBits(other.prix_com))
			return false;
		if (qt_com != other.qt_com)
			return false;
		return true;
	}

}
