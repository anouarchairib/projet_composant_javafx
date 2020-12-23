package modele;

public class Condensateur  {

	private String fkCode ;
	private float capaciter;
	private int tension_max;
	private boolean electro_con;

	public Condensateur(String fkCode, float capaciter, int tension_max, boolean electro_con) {
		super();
		this.fkCode = fkCode;
		this.capaciter = capaciter;
		this.tension_max = tension_max;
		this.electro_con = electro_con;
	}

	
	

	







	public boolean getElectro_con() {
		return electro_con;
	}





	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(capaciter);
		result = prime * result + (electro_con ? 1231 : 1237);
		result = prime * result + ((fkCode == null) ? 0 : fkCode.hashCode());
		result = prime * result + tension_max;
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
		Condensateur other = (Condensateur) obj;
		if (Float.floatToIntBits(capaciter) != Float.floatToIntBits(other.capaciter))
			return false;
		if (electro_con != other.electro_con)
			return false;
		if (fkCode == null) {
			if (other.fkCode != null)
				return false;
		} else if (!fkCode.equals(other.fkCode))
			return false;
		if (tension_max != other.tension_max)
			return false;
		return true;
	}












	public void setElectro_con(boolean electro_con) {
		this.electro_con = electro_con;
	}












	@Override
	public String toString() {
		return "Condensateur [fkCode=" + fkCode + ", capaciter=" + capaciter + ", tension_max=" + tension_max
				+ ", electro_con=" + electro_con + "]";
	}














	public String getFkCode() {
		return fkCode;
	}
	public float getCapaciter() {
		return capaciter;
	}
	public int getTension_max() {
		return tension_max;
	}
	public void setfkCode(String fkCode) {
		this.fkCode = fkCode;
	}
	public void setCapaciter(float capaciter) {
		this.capaciter = capaciter;
	}
	public void setTension_max(int tension_max) {
		this.tension_max = tension_max;
	}

	
}
