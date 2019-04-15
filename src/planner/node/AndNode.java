package planner.node;

import java.util.HashMap;
import java.util.Map;

import graphviz.DotNodeFormat;
import graphviz.DotNodeStyle;
import language.Action;
import language.observation.deterministic.term.UnconditionalTermObservation;
import planner.BeliefState;
import planner.Node;
import planner.Num;
import structure.AdvancedSet;

public class AndNode extends Node {
	
	private AdvancedSet<OrNode> parents;
	private Action parentAction;
	private Map<UnconditionalTermObservation, OrNode> children;

	public AndNode(Action parentAction, BeliefState beliefState, Num availableSensorActions) {
		super(beliefState, availableSensorActions);
		
		this.setParents(new AdvancedSet<>());
		this.setParentAction(parentAction);
		this.setChildren(new HashMap<>());
	}
	
	public AdvancedSet<OrNode> getParents() {
		return this.parents;
	}

	public void setParents(AdvancedSet<OrNode> parents) {
		this.parents = parents;
	}
	
	public Action getParentAction() {
		return this.parentAction;
	}

	public void setParentAction(Action parentAction) {
		this.parentAction = parentAction;
	}

	public Map<UnconditionalTermObservation, OrNode> getChildren() {
		return this.children;
	}

	public void setChildren(Map<UnconditionalTermObservation, OrNode> children) {
		this.children = children;
	}
	
	public boolean isDead() {
		if(this.getChildren().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isSolved() {
		if(this.isDead()) {
			return false;
		}
		for(Map.Entry<UnconditionalTermObservation, OrNode> entry : this.getChildren().entrySet()) {
			OrNode child = entry.getValue();
			
			if(!child.isSolved()) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Max can be replaced with alternative aggregation, e.g. sum or weighted sum.
	 */
	@Override
	public double getHeuristic() {
		if(this.isDead()) {
			return Double.POSITIVE_INFINITY;
		}
		double maxHeuristic = 0;
		for(Map.Entry<UnconditionalTermObservation, OrNode> entry : this.getChildren().entrySet()) {
			OrNode child = entry.getValue();
			
			double childHeuristic = child.getHeuristic();
			if(childHeuristic > maxHeuristic) {
				maxHeuristic = childHeuristic;
			}
		}
		return maxHeuristic;
	}
	
	@Override
	public double getSolvedHeuristic() {
		if(!this.isSolved()) {
			return Double.POSITIVE_INFINITY;
		}
		double maxHeuristic = 0;
		for(Map.Entry<UnconditionalTermObservation, OrNode> entry : this.getChildren().entrySet()) {
			OrNode child = entry.getValue();
			
			if(child.isSolved()) {
				double childHeuristic = child.getSolvedHeuristic();
				if(childHeuristic > maxHeuristic) {
					maxHeuristic = childHeuristic;
				}
			}
		}
		return maxHeuristic;
	}
	
	@Override
	public boolean detectOrCycle(BeliefState beliefState) {
		for(OrNode parent : this.getParents()) {
			if(parent.detectOrCycle(beliefState)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean detectAndCycle(Action parentAction, BeliefState beliefState) {
		if(this.getParentAction().equals(parentAction) && this.getBeliefState().equals(beliefState)) {
			return true;
		}
		for(OrNode parent : this.getParents()) {
			if(parent.detectAndCycle(parentAction, beliefState)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public DotNodeFormat getDotNodeFormat() {
		String label = this.getParentAction() + ", " + this.getBeliefState() + ", t=" + this.getAvailableSensorActions() + ", v=" + this.getHeuristic();
		DotNodeStyle style = DotNodeStyle.AND_EXPLORED;
		if(this.getHeuristic() == Double.POSITIVE_INFINITY) {
			style = DotNodeStyle.AND_DEAD;
		} else if(this.isSolved()) {
			style = DotNodeStyle.AND_SOLVED;
		}
		return new DotNodeFormat(label, style);
	}
	
	@Override
	public String toString() {
		return "AND(" + this.getParentAction() + ", " + this.getBeliefState() + ")";
	}

}
