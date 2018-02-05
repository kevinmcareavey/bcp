package exceptions;

public class EmptyBeliefStateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2674291453216813037L;
	
	public EmptyBeliefStateException() {
		super("empty belief state");
	}

}
