package main.formula.term;

import main.Atom;
import main.formula.Term;

public abstract class Literal extends Term {
	
	private Atom atom;
	
	public Literal(Atom atom) {
		this.setAtom(atom);
	}

	public Atom getAtom() {
		return this.atom;
	}

	public void setAtom(Atom atom) {
		this.atom = atom;
	}

}
