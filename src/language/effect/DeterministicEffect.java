package language.effect;

import language.Atom;
import language.Effect;
import planner.State;
import structure.AdvancedSet;

public abstract class DeterministicEffect extends Effect {
	
	public abstract AdvancedSet<Atom> getDeleteList(State state);
	
	public abstract AdvancedSet<Atom> getAddList(State state);

}
