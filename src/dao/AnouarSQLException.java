package dao;

public class AnouarSQLException extends Exception {
    private final int code;
    
	public AnouarSQLException(String msg,int code) {
		super(msg);
		this.code=code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

}
