package language.observation.nondeterministic;

import language.Symbol;
import language.observation.DeterministicObservation;
import language.observation.NondeterministicObservation;
import structure.AdvancedSet;

public class OneOfObservation extends NondeterministicObservation {
	
	private AdvancedSet<DeterministicObservation> alternatives;
	
	public OneOfObservation(AdvancedSet<DeterministicObservation> alternatives) {
		this.setAlternatives(alternatives);
	}
	
	public OneOfObservation(DeterministicObservation... alternatives) {
		this(new AdvancedSet<DeterministicObservation>(alternatives));
	}

	public AdvancedSet<DeterministicObservation> getAlternatives() {
		return this.alternatives;
	}

	public void setAlternatives(AdvancedSet<DeterministicObservation> alternatives) {
		this.alternatives = alternatives;
	}

	@Override
	public String toString() {
		String output = "(" + Symbol.NONDETERMINISM;
		for(DeterministicObservation alternative : this.getAlternatives()) {
			output += " " + alternative;
		}
		output += ")";
		return output;
	}

}
