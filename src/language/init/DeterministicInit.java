package language.init;

import language.Init;
import planner.BeliefState;
import planner.State;

public abstract class DeterministicInit extends Init {
	
	public abstract State getState();
	
	@Override
	public BeliefState getBeliefState() {
		return new BeliefState(this.getState());
	}

}
