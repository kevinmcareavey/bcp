package language.init.deterministic;

import language.Atom;
import language.init.DeterministicInit;
import planner.State;

public class PositiveLiteralInit extends DeterministicInit {
	
	private Atom atom;
	
	public PositiveLiteralInit(Atom atom) {
		this.setAtom(atom);
	}

	public Atom getAtom() {
		return this.atom;
	}

	public void setAtom(Atom atom) {
		this.atom = atom;
	}
	
	@Override
	public State getState() {
		return new State(this.getAtom());
	}
	
	@Override
	public String toString() {
		return this.getAtom().toString();
	}

}
