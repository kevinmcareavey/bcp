package language.effect.deterministic;

import language.Atom;
import language.Symbol;
import language.effect.DeterministicEffect;
import search.State;
import structure.AdvancedSet;

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
