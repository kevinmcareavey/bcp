package language.formula.term.literal;

import language.Atom;
import language.formula.term.Literal;
import search.State;

public class PositiveLiteral extends Literal {
	
	public PositiveLiteral(Atom atom) {
		super(atom);
	}

	@Override
	public boolean satisfies(State state) {
		return state.contains(this.getAtom());
	}

	@Override
	public String toString() {
		return this.getAtom().toString();
//		return "[PositiveLiteral: " + this.getAtom().toString() + "]";
	}

}
