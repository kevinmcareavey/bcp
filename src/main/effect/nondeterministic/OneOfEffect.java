package main.effect.nondeterministic;

import main.AdvancedSet;
import main.effect.DeterministicEffect;
import main.effect.NondeterministicEffect;

public class OneOfEffect extends NondeterministicEffect {
	
	private AdvancedSet<DeterministicEffect> alternatives;
	
	public OneOfEffect(AdvancedSet<DeterministicEffect> alternatives) {
		this.setAlternatives(alternatives);
	}
	
	public OneOfEffect(DeterministicEffect... alternatives) {
		this(new AdvancedSet<DeterministicEffect>(alternatives));
	}

	public AdvancedSet<DeterministicEffect> getAlternatives() {
		return this.alternatives;
	}

	public void setAlternatives(AdvancedSet<DeterministicEffect> alternatives) {
		this.alternatives = alternatives;
	}

	@Override
	public String toString() {
		String output = "(oneof";
		for(DeterministicEffect alternative : this.getAlternatives()) {
			output += " " + alternative;
		}
		output += ")";
		return output;
	}

}
