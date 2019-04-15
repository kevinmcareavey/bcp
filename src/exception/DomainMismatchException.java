package exception;

import language.Domain;
import language.Problem;

public class DomainMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6547128858231259776L;
	
	public DomainMismatchException(Domain domain, Problem problem) {
		super("mismatched domain: " + domain.getName() + ", " + problem.getDomainName());
	}

}
