package planner;

import java.util.ArrayList;
import java.util.List;

import exception.InconsistencyException;
import exception.UnrecognisedEffect;
import exception.UnrecognisedObservation;
import language.Action;
import language.Atom;
import language.DomainProblem;
import language.effect.DeterministicEffect;
import language.effect.nondeterministic.OneOfEffect;
import language.observation.DeterministicObservation;
import language.observation.deterministic.term.UnconditionalTermObservation;
import language.observation.nondeterministic.OneOfObservation;
import structure.AdvancedSet;

public abstract class Search {
	
	private DomainProblem domainProblem;
	
	public Search(DomainProblem domainProblem) {
		this.setDomainProblem(domainProblem);
	}
	
	public DomainProblem getDomainProblem() {
		return this.domainProblem;
	}

	public void setDomainProblem(DomainProblem domainProblem) {
		this.domainProblem = domainProblem;
	}
	
	/*
	 * Assumption: an action is "cautiously executable" in a belief state if it is executable in EVERY state in the belief state
	 */
	protected List<Action> getCautiouslyExecutable(BeliefState beliefState) {
		List<Action> subset = new ArrayList<>();
		for(Action action : this.getDomainProblem().getDomain().getActions()) {
			if(this.isCautiouslyExecutable(action, beliefState)) {
				subset.add(action);
			}
		}
		return subset;
	}
	
	/*
	 * Assumption: an action is "cautiously executable" in a belief state if it is executable in EVERY state in the belief state
	 */
	private boolean isCautiouslyExecutable(Action action, BeliefState beliefState) {
		if(beliefState.isEmpty()) {
			return false;
		}
		for(State state : beliefState) {
			if(!action.getPrecondition().satisfies(state)) {
				return false;
			}
		}
		return true;
	}
	
	public BeliefState getPredictedBeliefState(BeliefState beliefState, Action action) throws UnrecognisedEffect, InconsistencyException {
		if(action.getEffect() instanceof DeterministicEffect) {
			DeterministicEffect deterministicEffect = (DeterministicEffect) action.getEffect();
			return this.getPredictedBeliefState(beliefState, deterministicEffect);
		} else if(action.getEffect() instanceof OneOfEffect) {
			OneOfEffect oneOfEffect = (OneOfEffect) action.getEffect();
			return this.getPredictedBeliefState(beliefState, oneOfEffect);
		} else {
			throw new UnrecognisedEffect(action.getEffect());
		}
	}
	
	private BeliefState getPredictedBeliefState(BeliefState beliefState, OneOfEffect oneOfEffect) throws InconsistencyException {
		BeliefState predictedBeliefState = new BeliefState();
		for(DeterministicEffect deterministicEffect : oneOfEffect.getAlternatives()) {
			predictedBeliefState.addAll(this.getPredictedBeliefState(beliefState, deterministicEffect));
		}
		return predictedBeliefState;
	}
	
	private BeliefState getPredictedBeliefState(BeliefState beliefState, DeterministicEffect deterministicEffect) throws InconsistencyException {
		BeliefState predictedBeliefState = new BeliefState();
		for(State state : beliefState) {
			predictedBeliefState.add(this.getPredictedState(state, deterministicEffect));
		}
		return predictedBeliefState;
	}
	
	private State getPredictedState(State state, DeterministicEffect deterministicEffect) throws InconsistencyException {
		State predictedState = new State();
		predictedState.addAll(state);
		AdvancedSet<Atom> deleteList = deterministicEffect.getDeleteList(state);
		AdvancedSet<Atom> addList = deterministicEffect.getAddList(state);
		if(deleteList.intersects(addList)) {
			throw new InconsistencyException(deterministicEffect);
		}
		predictedState.removeAll(deleteList);
		predictedState.addAll(addList);
		return predictedState;
	}
	
	protected AdvancedSet<UnconditionalTermObservation> getPredictedObservations(Action action, BeliefState predictedBeliefState) throws UnrecognisedObservation {
		if(action.getObservation() instanceof DeterministicObservation) {
			DeterministicObservation deterministicObservation = (DeterministicObservation) action.getObservation();
			AdvancedSet<UnconditionalTermObservation> predictedObservations = new AdvancedSet<UnconditionalTermObservation>();
			for(State predictedState : predictedBeliefState) {
				predictedObservations.addAll(deterministicObservation.getObservations(predictedState));
			}
			return predictedObservations;
		} else if(action.getObservation() instanceof OneOfObservation) {
			OneOfObservation oneOfObservation = (OneOfObservation) action.getObservation();
			AdvancedSet<UnconditionalTermObservation> predictedObservations = new AdvancedSet<UnconditionalTermObservation>();
			for(State predictedState : predictedBeliefState) {
				for(DeterministicObservation deterministicObservation : oneOfObservation.getAlternatives()) {
					predictedObservations.addAll(deterministicObservation.getObservations(predictedState));
				}
			}
			return predictedObservations;
		} else {
			throw new UnrecognisedObservation(action.getObservation());
		}
	}
	
	private AdvancedSet<UnconditionalTermObservation> getPredictedObservations(Action action, State predictedState) throws UnrecognisedObservation {
		if(action.getObservation() instanceof DeterministicObservation) {
			DeterministicObservation deterministicObservation = (DeterministicObservation) action.getObservation();
			return deterministicObservation.getObservations(predictedState);
		} else if(action.getObservation() instanceof OneOfObservation) {
			OneOfObservation oneOfObservation = (OneOfObservation) action.getObservation();
			AdvancedSet<UnconditionalTermObservation> predictedObservations = new AdvancedSet<UnconditionalTermObservation>();
			for(DeterministicObservation deterministicObservation : oneOfObservation.getAlternatives()) {
				predictedObservations.addAll(deterministicObservation.getObservations(predictedState));
			}
			return predictedObservations;
		} else {
			throw new UnrecognisedObservation(action.getObservation());
		}
	}
	
	protected BeliefState getUpdatedBeliefState(Action action, BeliefState predictedBeliefState, UnconditionalTermObservation observation) throws UnrecognisedObservation {
		BeliefState updatedBeliefState = new BeliefState();
		for(State predictedState : predictedBeliefState) {
			if(this.getPredictedObservations(action, predictedState).contains(observation)) {
				updatedBeliefState.add(predictedState);
			}
		}
		return updatedBeliefState;
	}

}
