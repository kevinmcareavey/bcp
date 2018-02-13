package exception;

import language.Observation;

public class UnrecognisedObservation extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4227598388760484932L;

	public UnrecognisedObservation(Observation observation) {
		super("unrecognised observation: " + observation);
	}

}
