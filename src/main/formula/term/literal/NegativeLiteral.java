package main.formula.term.literal;

import main.Atom;
import main.State;
import main.Symbol;
import main.formula.term.Literal;

public class NegativeLiteral extends Literal {
	
	public NegativeLiteral(Atom atom) {
		super(atom);
	}

	@Override
	public boolean satisfies(State state) {
		return !state.contains(this.getAtom());
	}

	@Override
	public String toString() {
		return "(" + Symbol.NEGATION + " " + this.getAtom() + ")";
//		return "[NegativeLiteral: " + this.getAtom() + "]";
	}
	
}
