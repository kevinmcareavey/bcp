package main.effect.deterministic.term.unconditional.literal;

import main.AdvancedSet;
import main.Atom;
import main.State;
import main.effect.deterministic.term.unconditional.LiteralEffect;

public class PositiveLiteralEffect extends LiteralEffect {
	
	public PositiveLiteralEffect(Atom atom) {
		super(atom);
	}

	@Override
	public AdvancedSet<Atom> getDeleteList(State state) {
		return new AdvancedSet<Atom>();
	}
	
	@Override
	public AdvancedSet<Atom> getAddList(State state) {
		return new AdvancedSet<Atom>(this.getAtom());
	}

	@Override
	public String toString() {
		return this.getAtom().toString();
//		return "[PositiveLiteralEffect: " + this.getAtom() + "]";
	}

}
