package modele;

public class Resistance {
	private String fkCode;
	private double ohms_res;
	private double watt_res;

	public Resistance(String fkCode, double ohms_res, double watt_res) {
		super();
		this.fkCode = fkCode;
		this.ohms_res = ohms_res;
		this.watt_res = watt_res;
	}
	public String toString() {
		return "Resistance [fkCode=" + fkCode + ", ohms_res=" + ohms_res + ", watt_res=" + watt_res + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fkCode == null) ? 0 : fkCode.hashCode());
		long temp;
		temp = Double.doubleToLongBits(ohms_res);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(watt_res);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Resistance other = (Resistance) obj;
		if (fkCode == null) {
			if (other.fkCode != null)
				return false;
		} else if (!fkCode.equals(other.fkCode))
			return false;
		if (Double.doubleToLongBits(ohms_res) != Double.doubleToLongBits(other.ohms_res))
			return false;
		if (Double.doubleToLongBits(watt_res) != Double.doubleToLongBits(other.watt_res))
			return false;
		return true;
	}
	public String getFkCode() {
		return fkCode;
	}
	public double getOhms_res() {
		return ohms_res;
	}
	public double getWatt_res() {
		return watt_res;
	}
	public void setFkCode(String fkCode) {
		this.fkCode = fkCode;
	}
	public void setOhms_res(double ohms_res) {
		this.ohms_res = ohms_res;
	}
	public void setWatt_res(double watt_res) {
		this.watt_res = watt_res;
	}

}
