package language.observation.deterministic.term.unconditional.constant;

import language.Symbol;
import language.observation.deterministic.term.unconditional.ConstantObservation;

public class NoObservation extends ConstantObservation {

	@Override
	public String toString() {
		return Symbol.NOOB;
	}

}
