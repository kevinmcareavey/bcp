package language.observation.deterministic;

import language.Symbol;
import language.observation.DeterministicObservation;
import language.observation.deterministic.term.UnconditionalTermObservation;
import search.State;
import structure.AdvancedSet;

public class ConjunctiveObservation extends DeterministicObservation {
	
	private AdvancedSet<TermObservation> conjuncts;
	
	public ConjunctiveObservation(AdvancedSet<TermObservation> conjuncts) {
		this.setConjuncts(conjuncts);
	}
	
	public ConjunctiveObservation(TermObservation... conjuncts) {
		this(new AdvancedSet<TermObservation>(conjuncts));
	}

	public AdvancedSet<TermObservation> getConjuncts() {
		return this.conjuncts;
	}

	public void setConjuncts(AdvancedSet<TermObservation> conjuncts) {
		this.conjuncts = conjuncts;
	}
	
	@Override
	public AdvancedSet<UnconditionalTermObservation> getObservations(State predictedState) {
		AdvancedSet<UnconditionalTermObservation> observations = new AdvancedSet<>();
		for(TermObservation conjunct : this.getConjuncts()) {
			observations.addAll(conjunct.getObservations(predictedState));
		}
		return observations;
	}
	
	@Override
	public String toString() {
		String output = "(" + Symbol.CONJUNCTION;
//		String output = "[ConjunctiveObservation: ";
		for(TermObservation conjunct : this.getConjuncts()) {
			output += " " + conjunct;
		}
		output += ")";
//		output += "]";
		return output;
	}

}
