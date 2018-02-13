package language.observation;

import language.Observation;
import language.observation.deterministic.term.UnconditionalTermObservation;
import search.State;
import structure.AdvancedSet;

public abstract class DeterministicObservation extends Observation {
	
	public abstract AdvancedSet<UnconditionalTermObservation> getObservations(State predictedState);

}
