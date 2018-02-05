package main.formula.term.literal;

import main.Atom;
import main.State;
import main.formula.term.Literal;

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
