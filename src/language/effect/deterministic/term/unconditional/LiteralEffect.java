package language.effect.deterministic.term.unconditional;

import language.Atom;
import language.effect.deterministic.term.UnconditionalTermEffect;

public abstract class LiteralEffect extends UnconditionalTermEffect {
	
	private Atom atom;
	
	public LiteralEffect(Atom atom) {
		this.setAtom(atom);
	}

	public Atom getAtom() {
		return this.atom;
	}

	public void setAtom(Atom atom) {
		this.atom = atom;
	}

}
