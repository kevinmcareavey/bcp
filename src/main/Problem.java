package main;

import java.util.ArrayList;
import java.util.List;

import debug.Test;
import exceptions.EmptyBeliefStateException;
import exceptions.InconsistencyException;
import main.effect.DeterministicEffect;
import main.effect.nondeterministic.OneOfEffect;

public class Problem {
	
	private AdvancedSet<Atom> predicates;
	private Init init;
	private List<Action> actions;
	private Formula goal;
	
	public Problem(AdvancedSet<Atom> domain, List<Action> actions, Init init, Formula goal) {
		this.setPredicates(domain);
		this.setActions(actions);
		this.setInit(init);
		this.setGoal(goal);
	}
	
	public AdvancedSet<Atom> getPredicates() {
		return this.predicates;
	}

	public void setPredicates(AdvancedSet<Atom> predicates) {
		this.predicates = predicates;
	}

	public Init getInit() {
		return this.init;
	}

	public void setInit(Init init) {
		this.init = init;
	}
	
	public List<Action> getActions() {
		return this.actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public Formula getGoal() {
		return this.goal;
	}

	public void setGoal(Formula goal) {
		this.goal = goal;
	}
	
	public BeliefState getInitialBeliefState() {
		return this.getInit().getBeliefState();
	}
	
	public boolean isGoal(BeliefState beliefState) {
		return this.getGoal().satisfies(beliefState);
	}
	
	public List<Action> getExecutableActions(BeliefState beliefState) {
		List<Action> executableActions = new ArrayList<>();
		for(Action action : actions) {
			if(this.isExecutable(action, beliefState)) {
				executableActions.add(action);
			}
		}
		return executableActions;
	}
	
	/*
	 * Assumption: an action is "executable" in a belief state if it is executable in any state in the belief state
	 */
	private boolean isExecutable(Action action, BeliefState beliefState) {
		if(beliefState.isEmpty()) {
			return false;
		}
		for(State state : beliefState) {
			if(action.getPrecondition().satisfies(state)) {
				return true;
			}
		}
		return false;
	}
	
	public BeliefState getSuccessorBeliefState(BeliefState beliefState, Action action) throws InconsistencyException, EmptyBeliefStateException {
		if(action.getEffect() instanceof DeterministicEffect) {
			DeterministicEffect deterministicEffect = (DeterministicEffect) action.getEffect();
			return this.getSuccessorBeliefState(beliefState, action.getPrecondition(), deterministicEffect);
		} else if(action.getEffect() instanceof OneOfEffect) {
			OneOfEffect oneOfEffect = (OneOfEffect) action.getEffect();
			BeliefState successorBeliefState = new BeliefState();
			for(DeterministicEffect deterministicEffect : oneOfEffect.getAlternatives()) {
				successorBeliefState.addAll(this.getSuccessorBeliefState(beliefState, action.getPrecondition(), deterministicEffect));
			}
			return successorBeliefState;
		} else {
			throw new EmptyBeliefStateException();
		}
	}
	
	private BeliefState getSuccessorBeliefState(BeliefState beliefState, Formula precondition, DeterministicEffect deterministicEffect) throws InconsistencyException {
		BeliefState successorBeliefState = new BeliefState();
		for(State state : beliefState) {
			successorBeliefState.add(this.getSuccessorState(state, precondition, deterministicEffect));
		}
		return successorBeliefState;
	}
	
	/*
	 * Assumption: actions have no effect when executed in states that do not satisfy their preconditions
	 */
	private State getSuccessorState(State state, Formula precondition, DeterministicEffect deterministicEffect) throws InconsistencyException {
		if(!precondition.satisfies(state)) {
			return state;
		}
		State successorState = new State();
		successorState.addAll(state);
		AdvancedSet<Atom> deleteList = deterministicEffect.getDeleteList(state);
		AdvancedSet<Atom> addList = deterministicEffect.getAddList(state);
		if(deleteList.intersects(addList)) {
			throw new InconsistencyException(deterministicEffect);
		}
		successorState.removeAll(deleteList);
		successorState.addAll(addList);
		return successorState;
	}
	
	public Percept getPercept(State state) {
		if((state.contains(Test.vl) && state.contains(Test.dl)) || (!state.contains(Test.vl) && state.contains(Test.dr))) {
			return new Percept("see-dirt");
		} else {
			return new Percept("not-see-dirt");
		}
	}
	
	public AdvancedSet<Percept> getPercepts(BeliefState beliefState) {
		AdvancedSet<Percept> percepts = new AdvancedSet<>();
		for(State state : beliefState) {
			percepts.add(this.getPercept(state));
		}
		return percepts;
	}
	
	public BeliefState updateBeliefState(BeliefState beliefState, Percept percept) {
		BeliefState updatedBeliefState = new BeliefState();
		for(State state : beliefState) {
			if(this.getPercept(state).equals(percept)) {
				updatedBeliefState.add(state);
			}
		}
		return updatedBeliefState;
	}

}
