package language.formula.term.literal;

import language.Atom;
import language.Symbol;
import language.formula.term.Literal;
import search.State;

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
