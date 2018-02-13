package exception;

import language.Effect;

public class UnrecognisedEffect extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6093724485721053459L;

	public UnrecognisedEffect(Effect effect) {
		super("unrecognised effect: " + effect);
	}

}
