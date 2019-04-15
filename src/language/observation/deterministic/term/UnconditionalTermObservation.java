package language.observation.deterministic.term;

import language.observation.deterministic.TermObservation;
import planner.State;
import structure.AdvancedSet;

public abstract class UnconditionalTermObservation extends TermObservation {
	
	@Override
	public AdvancedSet<UnconditionalTermObservation> getObservations(State predictedState) {
		return new AdvancedSet<UnconditionalTermObservation>(this);
	}

}
