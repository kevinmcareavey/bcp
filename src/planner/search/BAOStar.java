package planner.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import exception.InconsistencyException;
import exception.InvalidNumException;
import exception.UnrecognisedEffect;
import exception.UnrecognisedObservation;
import language.Action;
import language.DomainProblem;
import language.observation.deterministic.term.UnconditionalTermObservation;
import planner.BeliefState;
import planner.Num;
import planner.Plan;
import planner.Search;
import planner.SearchGraph;
import planner.node.AndNode;
import planner.node.OrNode;
import structure.AdvancedSet;

/*
 * Optimal heuristic backward chaining (top-down) search for AND/OR graphs.
 */
public class BAOStar extends Search {
	
	private SearchGraph searchGraph;
	private Num bound;
	
	private LinkedHashSet<UnconditionalTermObservation> order;
	
	public BAOStar(DomainProblem domainProblem, Num bound) {
		super(domainProblem);
		this.setBound(bound);
		this.setOrder(new LinkedHashSet<>());
	}
	
	public SearchGraph getSearchGraph() {
		return this.searchGraph;
	}

	public void setSearchGraph(SearchGraph searchGraph) {
		this.searchGraph = searchGraph;
	}
	
	public Num getBound() {
		return this.bound;
	}

	public void setBound(Num bound) {
		this.bound = bound;
	}
	
	public LinkedHashSet<UnconditionalTermObservation> getOrder() {
		return this.order;
	}

	public void setOrder(LinkedHashSet<UnconditionalTermObservation> order) {
		this.order = order;
	}
	
	public Plan search() throws UnrecognisedEffect, InconsistencyException, UnrecognisedObservation, IOException, InterruptedException, InvalidNumException {
		this.setSearchGraph(new SearchGraph(this.getDomainProblem().getProblem().getInit().getBeliefState(), this.getDomainProblem().getProblem().getGoal(), this.getBound()));
		
//		this.getSearchGraph().getDotGraph().writeToPdf(System.getProperty("user.home") + "/trace-" + String.format("%05d", 0) + ".pdf");
	
//		int i = 1;
		while(!this.getSearchGraph().getRoot().isSolved() && this.getSearchGraph().getRoot().getHeuristic() != Double.POSITIVE_INFINITY) {
			OrNode leaf = this.chooseLeaf(this.getSearchGraph().getRoot());
			if(leaf == null) {
				break;
			}
			this.expand(leaf);
			
//			this.getSearchGraph().getDotGraph().writeToPdf(System.getProperty("user.home") + "/trace-" + String.format("%05d", i) + ".pdf");
//			i++;
		}
		
		return this.extractSolution(this.getSearchGraph().getRoot());
	}
	
	private OrNode chooseLeaf(OrNode orNode) {
		if(orNode == null) {
			return null;
		} else if(!orNode.isExpanded()) {
			return orNode;
		} else {
			AndNode bestAndNode = null;
			double bestAndNodeHeuristic = Double.POSITIVE_INFINITY;
			for(Map.Entry<Action, AndNode> entry : orNode.getChildren().entrySet()) {
				AndNode child = entry.getValue();
				if(child.getHeuristic() != Double.POSITIVE_INFINITY) {
					double childHeuristic = child.getHeuristic();
					if(bestAndNode == null || childHeuristic < bestAndNodeHeuristic) {
						bestAndNode = child;
						bestAndNodeHeuristic = childHeuristic;
					}
				}
			}
			
			OrNode bestOrNode = null;
			double bestOrNodeHeuristic = Double.POSITIVE_INFINITY;
			for(Map.Entry<UnconditionalTermObservation, OrNode> entry : bestAndNode.getChildren().entrySet()) {
				this.getOrder().add(entry.getKey());
				OrNode child = entry.getValue();
				if(!child.isSolved() && child.getHeuristic() != Double.POSITIVE_INFINITY) {
					double childHeuristic = child.getHeuristic();
					if(bestOrNode == null || childHeuristic < bestOrNodeHeuristic) {
						bestOrNode = child;
						bestOrNodeHeuristic = childHeuristic;
					}
				}
			}
			
			return this.chooseLeaf(bestOrNode);
		}
	}
	
	private void expand(OrNode orNode) throws UnrecognisedEffect, InconsistencyException, UnrecognisedObservation, InvalidNumException {
		Num priorSensorActions = orNode.getAvailableSensorActions();
		BeliefState beliefState = orNode.getBeliefState();
		
		List<Action> executableActions = this.getCautiouslyExecutable(beliefState);
		for(Action action : executableActions) {
			BeliefState predictedBeliefState = this.getPredictedBeliefState(beliefState, action);
			
			if(!orNode.detectAndCycle(action, predictedBeliefState)) {
				Map<BeliefState, AdvancedSet<UnconditionalTermObservation>> grandchildren = this.expand(orNode, action, predictedBeliefState);
				if(grandchildren != null && !grandchildren.isEmpty()) {
					
					Num revisedSensorActions = priorSensorActions;
					if(grandchildren.size() > 1) {
						if(priorSensorActions.isMin()) {
							break;
						}
						revisedSensorActions = revisedSensorActions.decrement();
					}
					
					this.getSearchGraph().addOrEdge(priorSensorActions, beliefState, action, predictedBeliefState);
					
					for(Map.Entry<BeliefState, AdvancedSet<UnconditionalTermObservation>> entry : grandchildren.entrySet()) {
						BeliefState updatedBeliefState = entry.getKey();
						for(UnconditionalTermObservation predictedObservation : entry.getValue()) {
							this.getSearchGraph().addAndEdge(priorSensorActions, action, predictedBeliefState, predictedObservation, revisedSensorActions, updatedBeliefState);
						}
					}
				}
			}
		}
		
		orNode.setExpanded(true);
	}
	
	private Map<BeliefState, AdvancedSet<UnconditionalTermObservation>> expand(OrNode orNode, Action parentAction, BeliefState predictedBeliefState) throws UnrecognisedObservation {
		Map<BeliefState, AdvancedSet<UnconditionalTermObservation>> tracked = new HashMap<>();
		
		AdvancedSet<UnconditionalTermObservation> predictedObservations = this.getPredictedObservations(parentAction, predictedBeliefState);
		for(UnconditionalTermObservation predictedObservation : predictedObservations) {
			BeliefState updatedBeliefState = this.getUpdatedBeliefState(parentAction, predictedBeliefState, predictedObservation);
			
			if(orNode.detectOrCycle(updatedBeliefState)) {
				return null;
			}
			
			if(!tracked.containsKey(updatedBeliefState)) {
				tracked.put(updatedBeliefState, new AdvancedSet<>());
			}
			
			tracked.get(updatedBeliefState).add(predictedObservation);
		}
		
		return tracked;
	}
	
	/*
	 * Extract history-based policy graph (i.e. fully expanded tree).
	 * 
	 * To do: implement extraction of tracking-based policies graph (i.e. more compact DAG).
	 */
	private Plan extractSolution(OrNode orNode) {
		if(orNode == null || !orNode.isSolved()) {
			return null;
		}
		
		Plan plan = new Plan();
		
		if(!orNode.getChildren().isEmpty()) {
			AndNode bestAndNode = null;
			double bestAndNodeHeuristic = Double.POSITIVE_INFINITY;
			for(Map.Entry<Action, AndNode> entry : orNode.getChildren().entrySet()) {
				AndNode child = entry.getValue();
				if(child.isSolved()) {
					double childHeuristic = child.getSolvedHeuristic();
					if(bestAndNode == null || childHeuristic < bestAndNodeHeuristic) {
						bestAndNode = child;
						bestAndNodeHeuristic = childHeuristic;
					}
				}
			}
			
			plan.setAction(bestAndNode.getParentAction());
			
			for(Map.Entry<UnconditionalTermObservation, OrNode> entry : bestAndNode.getChildren().entrySet()) {
				UnconditionalTermObservation observation = entry.getKey();
				OrNode grandchild = entry.getValue();
				plan.addSubplan(observation, this.extractSolution(grandchild));
			}
		}
		
		return plan;
	}
	
}
