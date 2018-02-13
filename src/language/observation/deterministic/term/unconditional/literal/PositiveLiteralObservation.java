package language.observation.deterministic.term.unconditional.literal;

import language.Percept;
import language.observation.deterministic.term.unconditional.LiteralObservation;

public class PositiveLiteralObservation extends LiteralObservation {

	public PositiveLiteralObservation(Percept percept) {
		super(percept);
	}
	
	@Override
	public String toString() {
		return this.getPercept().toString();
//		return "[PositiveLiteralObservation: " + this.getPercept() + "]";
	}

}
