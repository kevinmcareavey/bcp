package language.observation.deterministic.term.unconditional.literal;

import language.Percept;
import language.Symbol;
import language.observation.deterministic.term.unconditional.LiteralObservation;

public class NegativeLiteralObservation extends LiteralObservation {

	public NegativeLiteralObservation(Percept percept) {
		super(percept);
	}
	
	@Override
	public String toString() {
		return "(" + Symbol.NEGATION + " " + this.getPercept() + ")";
//		return "[NegativeLiteralObservation: " + this.getPercept() + "]";
	}

}
