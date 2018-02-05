package main.init.nondeterministic;

import main.AdvancedSet;
import main.BeliefState;
import main.init.DeterministicInit;
import main.init.NondeterministicInit;

public class OneOfInit extends NondeterministicInit {
	
	private AdvancedSet<DeterministicInit> alternatives;
	
	public OneOfInit(AdvancedSet<DeterministicInit> alternatives) {
		this.setAlternatives(alternatives);
	}
	
	public OneOfInit(DeterministicInit... alternatives) {
		this(new AdvancedSet<DeterministicInit>(alternatives));
	}

	public AdvancedSet<DeterministicInit> getAlternatives() {
		return this.alternatives;
	}

	public void setAlternatives(AdvancedSet<DeterministicInit> alternatives) {
		this.alternatives = alternatives;
	}
	
	@Override
	public BeliefState getBeliefState() {
		BeliefState beliefState = new BeliefState();
		for(DeterministicInit alternative : this.getAlternatives()) {
			beliefState.add(alternative.getState());
		}
		return beliefState;
	}
	
	@Override
	public String toString() {
		String output = "(oneof";
		for(DeterministicInit alternative : this.getAlternatives()) {
			output += " " + alternative;
		}
		output += ")";
		return output;
	}

}
