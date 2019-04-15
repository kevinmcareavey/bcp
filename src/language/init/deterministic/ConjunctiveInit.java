package language.init.deterministic;

import language.Symbol;
import language.init.DeterministicInit;
import planner.State;
import structure.AdvancedSet;

public class ConjunctiveInit extends DeterministicInit {
	
	private AdvancedSet<PositiveLiteralInit> conjuncts;
	
	public ConjunctiveInit(AdvancedSet<PositiveLiteralInit> conjuncts) {
		this.setConjuncts(conjuncts);
	}
	
	public ConjunctiveInit(PositiveLiteralInit... conjuncts) {
		this(new AdvancedSet<PositiveLiteralInit>(conjuncts));
	}

	public AdvancedSet<PositiveLiteralInit> getConjuncts() {
		return this.conjuncts;
	}

	public void setConjuncts(AdvancedSet<PositiveLiteralInit> conjuncts) {
		this.conjuncts = conjuncts;
	}
	
	@Override
	public State getState() {
		State state = new State();
		for(PositiveLiteralInit conjunct : this.getConjuncts()) {
			state.add(conjunct.getAtom());
		}
		return state;
	}
	
	@Override
	public String toString() {
		String output = "(" + Symbol.CONJUNCTION;
		for(PositiveLiteralInit conjunct : this.getConjuncts()) {
			output += " " + conjunct.toString();
		}
		output += ")";
		return output;
	}

}
