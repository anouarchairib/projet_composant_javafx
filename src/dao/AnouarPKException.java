package dao;

public class AnouarPKException extends AnouarSQLException {
	private final String champ;

	public AnouarPKException(String msg, int code, String champ) {
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
