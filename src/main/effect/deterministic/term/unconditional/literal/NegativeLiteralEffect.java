package main.effect.deterministic.term.unconditional.literal;

import main.AdvancedSet;
import main.Atom;
import main.State;
import main.Symbol;
import main.effect.deterministic.term.unconditional.LiteralEffect;

public class NegativeLiteralEffect extends LiteralEffect {
	
	public NegativeLiteralEffect(Atom atom) {
		super(atom);
	}

	@Override
	public AdvancedSet<Atom> getDeleteList(State state) {
		return new AdvancedSet<Atom>(this.getAtom());
	}
	
	@Override
	public AdvancedSet<Atom> getAddList(State state) {
		return new AdvancedSet<Atom>();
	}

	@Override
	public String toString() {
		return "(" + Symbol.NEGATION + " " + this.getAtom() + ")";
//		return "[NegativeLiteralEffect: " + this.getAtom() + "]";
	}

}
