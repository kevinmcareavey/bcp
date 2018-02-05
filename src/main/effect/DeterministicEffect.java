package main.effect;

import main.AdvancedSet;
import main.Atom;
import main.Effect;
import main.State;

public abstract class DeterministicEffect extends Effect {
	
	public abstract AdvancedSet<Atom> getDeleteList(State state);
	
	public abstract AdvancedSet<Atom> getAddList(State state);

}
