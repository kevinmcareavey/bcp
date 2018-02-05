package main.effect.deterministic;

import main.AdvancedSet;
import main.Atom;
import main.State;
import main.Symbol;
import main.effect.DeterministicEffect;

public class ConjunctiveEffect extends DeterministicEffect {
	
	private AdvancedSet<TermEffect> conjuncts;
	
	public ConjunctiveEffect(AdvancedSet<TermEffect> conjuncts) {
		this.setConjuncts(conjuncts);
	}
	
	public ConjunctiveEffect(TermEffect... conjuncts) {
		this(new AdvancedSet<TermEffect>(conjuncts));
	}

	public AdvancedSet<TermEffect> getConjuncts() {
		return this.conjuncts;
	}

	public void setConjuncts(AdvancedSet<TermEffect> conjuncts) {
		this.conjuncts = conjuncts;
	}
	
	@Override
	public AdvancedSet<Atom> getDeleteList(State state) {
		AdvancedSet<Atom> atoms = new AdvancedSet<Atom>();
		for(TermEffect conjunct : this.getConjuncts()) {
			atoms.addAll(conjunct.getDeleteList(state));
		}
		return atoms;
	}
	
	@Override
	public AdvancedSet<Atom> getAddList(State state) {
		AdvancedSet<Atom> atoms = new AdvancedSet<Atom>();
		for(TermEffect conjunct : this.getConjuncts()) {
			atoms.addAll(conjunct.getAddList(state));
		}
		return atoms;
	}

	@Override
	public String toString() {
		String output = "(" + Symbol.CONJUNCTION;
//		String output = "[ConjunctiveEffect: ";
		for(TermEffect conjunct : this.getConjuncts()) {
			output += " " + conjunct;
		}
		output += ")";
//		output += "]";
		return output;
	}

}
