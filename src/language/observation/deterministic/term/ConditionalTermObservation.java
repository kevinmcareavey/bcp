package language.observation.deterministic.term;

import language.Formula;
import language.Symbol;
import language.observation.deterministic.TermObservation;
import search.State;
import structure.AdvancedSet;

public class ConditionalTermObservation extends TermObservation {
	
	private Formula condition;
	private UnconditionalTermObservation observation;
	
	public ConditionalTermObservation(Formula condition, UnconditionalTermObservation observation) {
		this.setCondition(condition);
		this.setObservation(observation);
	}
	
	public Formula getCondition() {
		return this.condition;
	}
	
	public void setCondition(Formula condition) {
		this.condition = condition;
	}
	
	public UnconditionalTermObservation getObservation() {
		return this.observation;
	}
	
	public void setObservation(UnconditionalTermObservation observation) {
		this.observation = observation;
	}
	
	@Override
	public AdvancedSet<UnconditionalTermObservation> getObservations(State predictedState) {
		if(this.getCondition().satisfies(predictedState)) {
			return new AdvancedSet<UnconditionalTermObservation>(this.getObservation());
		} else {
			return new AdvancedSet<UnconditionalTermObservation>();
		}
	}
	
	@Override
	public String toString() {
		return "(" + Symbol.CONDITIONAL + " " + this.getCondition() + " " + this.getObservation() + ")";
//		return "[ConditionalTermObservation: " + this.getCondition() + " " + this.getObservation() + "]";
	}

}
