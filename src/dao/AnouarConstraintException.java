package dao;

public class AnouarConstraintException extends AnouarSQLException {
	private final String champ;

	public AnouarConstraintException(String msg, int code, String champ) {
		super(msg, code);
		this.champ = champ;

	}

	/**
	 * @return the champ
	 */
	public String getChamp() {
		return champ;
	}

}
