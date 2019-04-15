package language.effect.deterministic.term.unconditional.constant;

import language.Atom;
import language.Symbol;
import language.effect.deterministic.term.unconditional.ConstantEffect;
import planner.State;
import structure.AdvancedSet;

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
