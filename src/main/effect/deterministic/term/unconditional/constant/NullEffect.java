package main.effect.deterministic.term.unconditional.constant;

import main.AdvancedSet;
import main.Atom;
import main.State;
import main.Symbol;
import main.effect.deterministic.term.unconditional.ConstantEffect;

public class NullEffect extends ConstantEffect {
	
	@Override
	public AdvancedSet<Atom> getDeleteList(State state) {
		return new AdvancedSet<Atom>();
	}
	
	@Override
	public AdvancedSet<Atom> getAddList(State state) {
		return new AdvancedSet<Atom>();
	}
	
	@Override
	public String toString() {
		return Symbol.NULL;
//		return "[Constant: " + Symbol.NULL + "]";
	}

}
