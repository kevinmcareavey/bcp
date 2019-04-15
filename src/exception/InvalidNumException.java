package exception;

public class InvalidNumException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6547128858231259776L;
	
	public InvalidNumException(int value) {
		super("invalid bound: " + value);
	}

}
