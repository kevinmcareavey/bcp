package search;

import java.util.ArrayList;
import java.util.List;

import exception.EmptyBeliefStateException;
import exception.InconsistencyException;
import exception.UnrecognisedEffect;
import exception.UnrecognisedObservation;
import graphviz.DirectedEdge;
import graphviz.DirectedGraph;
import graphviz.EdgeStyle;
import graphviz.Node;
import graphviz.NodeStyle;
import language.Action;
import language.Atom;
import language.DomainProblem;
import language.Formula;
import language.effect.DeterministicEffect;
import language.effect.nondeterministic.OneOfEffect;
import language.observation.DeterministicObservation;
import language.observation.deterministic.term.UnconditionalTermObservation;
import language.observation.nondeterministic.OneOfObservation;
import structure.AdvancedSet;

public class AndOrSearch {
	
	private DirectedGraph trace;
	
	/*
	 * Assumption: an action is "cautiously executable" in a belief state if it is executable in EVERY state in the belief state
	 */
	private List<Action> getCautiouslyExecutable(List<Action> actions, BeliefState beliefState) {
		List<Action> subset = new ArrayList<>();
		for(Action action : actions) {
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
	
	private AdvancedSet<UnconditionalTermObservation> getPredictedObservations(Action action, BeliefState predictedBeliefState) throws UnrecognisedObservation {
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
	
	private BeliefState getUpdatedBeliefState(Action action, BeliefState predictedBeliefState, UnconditionalTermObservation observation) throws UnrecognisedObservation {
		BeliefState updatedBeliefState = new BeliefState();
		for(State predictedState : predictedBeliefState) {
			if(this.getPredictedObservations(action, predictedState).contains(observation)) {
				updatedBeliefState.add(predictedState);
			}
		}
		return updatedBeliefState;
	}
	
	public Plan search(DomainProblem domainProblem) throws InconsistencyException, EmptyBeliefStateException, UnrecognisedEffect, UnrecognisedObservation {
		this.setTrace(new DirectedGraph("trace"));
		Node startNode = new Node(Node.COUNTER, "START", NodeStyle.BOLD);
		Node beliedStateNode = new Node(Node.COUNTER, domainProblem.getProblem().getInit().getBeliefState().toString(), NodeStyle.SOLID);
		this.getTrace().addEdge(new DirectedEdge(startNode, beliedStateNode, EdgeStyle.NONE));
		
		return this.orSearch(domainProblem.getDomain().getActions(), domainProblem.getProblem().getGoal(), new Path(), domainProblem.getProblem().getInit().getBeliefState(), beliedStateNode);
	}
	
	private Plan orSearch(List<Action> actions, Formula goal, Path path, BeliefState beliefState, Node beliedStateNode) throws InconsistencyException, EmptyBeliefStateException, UnrecognisedEffect, UnrecognisedObservation {
		if(goal.satisfies(beliefState)) {
			this.getTrace().addEdge(new DirectedEdge(beliedStateNode, new Node(Node.COUNTER, "SUCCESS", NodeStyle.BOLD), EdgeStyle.NONE));
			return new Plan();
		}
		
		if(path.contains(beliefState)) {
			this.getTrace().addEdge(new DirectedEdge(beliedStateNode, new Node(Node.COUNTER, "FAIL", NodeStyle.FILLED), EdgeStyle.NONE));
			return null;
		}
		
		List<Action> executableActions = this.getCautiouslyExecutable(actions, beliefState);
		for(Action action : executableActions) {
			Path extendedPath = path.prepend(beliefState);
			
			Plan plan = this.andSearch(actions, goal, extendedPath, beliefState, action, beliedStateNode);
			if(plan != null) {
				plan.setAction(action);
				return plan;
			}
		}
		
		this.getTrace().addEdge(new DirectedEdge(beliedStateNode, new Node(Node.COUNTER, "FAIL", NodeStyle.FILLED), EdgeStyle.NONE));
		return null;
	}
	
	private Plan andSearch(List<Action> actions, Formula goal, Path path, BeliefState beliefState, Action action, Node beliedStateNode) throws InconsistencyException, EmptyBeliefStateException, UnrecognisedEffect, UnrecognisedObservation {
		Plan plan = new Plan();
		
		BeliefState predictedBeliefState = this.getPredictedBeliefState(beliefState, action);
		
		Node predictedBeliefStateNode = new Node(Node.COUNTER, predictedBeliefState.toString(), NodeStyle.DOTTED);
		this.getTrace().addEdge(new DirectedEdge(beliedStateNode, predictedBeliefStateNode, action.getName().toString(), EdgeStyle.SOLID));
		
		AdvancedSet<UnconditionalTermObservation> predictedObservations = this.getPredictedObservations(action, predictedBeliefState);
		for(UnconditionalTermObservation predictedObservation : predictedObservations) {
			BeliefState updatedBeliefState = this.getUpdatedBeliefState(action, predictedBeliefState, predictedObservation);
			
			Node updatedBeliefStateNode = new Node(Node.COUNTER, updatedBeliefState.toString(), NodeStyle.SOLID);
			this.getTrace().addEdge(new DirectedEdge(predictedBeliefStateNode, updatedBeliefStateNode, predictedObservation.toString(), EdgeStyle.DOTTED));
			
			Plan subplan = this.orSearch(actions, goal, path, updatedBeliefState, updatedBeliefStateNode);
			if(subplan == null) {
				return null;
			}
			plan.addSubplan(predictedObservation, subplan);
		}
		
		return plan;
	}
	
	public DirectedGraph getTrace() {
		return this.trace;
	}
	
	public void setTrace(DirectedGraph trace) {
		this.trace = trace;
	}

}
