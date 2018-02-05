package main.init;

import main.BeliefState;
import main.Init;
import main.State;

public abstract class DeterministicInit extends Init {
	
	public abstract State getState();
	
	@Override
	public BeliefState getBeliefState() {
		return new BeliefState(this.getState());
	}

}
