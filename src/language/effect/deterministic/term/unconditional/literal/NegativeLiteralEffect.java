package language.effect.deterministic.term.unconditional.literal;

import language.Atom;
import language.Symbol;
import language.effect.deterministic.term.unconditional.LiteralEffect;
import search.State;
import structure.AdvancedSet;

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
