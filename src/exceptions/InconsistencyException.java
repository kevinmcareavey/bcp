package exceptions;

import main.effect.DeterministicEffect;

public class InconsistencyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6547128858231259776L;
	
	public InconsistencyException(DeterministicEffect deterministicEffect) {
		super("inconsistent effect: " + deterministicEffect);
	}

}
