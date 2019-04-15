package planner.node;

import java.util.HashMap;
import java.util.Map;

import graphviz.DotNodeFormat;
import graphviz.DotNodeStyle;
import language.Action;
import language.Formula;
import language.observation.deterministic.term.UnconditionalTermObservation;
import planner.BeliefState;
import planner.Node;
import planner.Num;

public class OrNode extends Node {
	
	private Map<AndNode, UnconditionalTermObservation> parents;
	private Map<Action, AndNode> children;
	
	private boolean expanded;
	private boolean goal;
	
	public OrNode(BeliefState beliefState, Num availableSensorActions, Formula goal) {
		super(beliefState, availableSensorActions);
		
		this.setParents(new HashMap<>());
		this.setChildren(new HashMap<>());
		
		this.setExpanded(false);
		
		if(goal.satisfies(beliefState)) {
			this.setGoal(true);
		} else {
			this.setGoal(false);
		}
	}
	
	public Map<AndNode, UnconditionalTermObservation> getParents() {
		return this.parents;
	}

	public void setParents(Map<AndNode, UnconditionalTermObservation> parents) {
		this.parents = parents;
	}

	public Map<Action, AndNode> getChildren() {
		return this.children;
	}

	public void setChildren(Map<Action, AndNode> children) {
		this.children = children;
	}
	
	public boolean isExpanded() {
		return this.expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
	public boolean isGoal() {
		return this.goal;
	}

	public void setGoal(boolean goal) {
		this.goal = goal;
	}
	
	public boolean isDead() {
		if(this.isExpanded() && this.getChildren().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isSolved() {
		if(this.isGoal()) {
			return true;
		}
		if(this.isExpanded() && !this.isDead()) {
			for(Map.Entry<Action, AndNode> entry : this.getChildren().entrySet()) {
				AndNode child = entry.getValue();
				if(child.isSolved()) {
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	@Override
	public double getHeuristic() {
		if(this.isGoal()) {
			return 0;
		}
		if(this.isDead()) {
			return Double.POSITIVE_INFINITY;
		}
		if(this.isExpanded()) {
			double minHeuristic = Double.POSITIVE_INFINITY;
			for(Map.Entry<Action, AndNode> entry : this.getChildren().entrySet()) {
				AndNode child = entry.getValue();
				double childHeuristic = child.getHeuristic();
				if(childHeuristic < minHeuristic) {
					minHeuristic = childHeuristic;
				}
			}
			return minHeuristic + 1; // Action cost is 1.
		}
		return 1; // Admissible heuristic (i.e. one that never overestimates the cost to reach the goal).
	}
	
	@Override
	public double getSolvedHeuristic() {
		if(this.isGoal()) {
			return 0;
		}
		if(!this.isSolved()) {
			return Double.POSITIVE_INFINITY;
		}
		double minHeuristic = Double.POSITIVE_INFINITY;
		for(Map.Entry<Action, AndNode> entry : this.getChildren().entrySet()) {
			AndNode child = entry.getValue();
			if(child.isSolved()) {
				double childHeuristic = child.getSolvedHeuristic();
				if(childHeuristic < minHeuristic) {
					minHeuristic = childHeuristic;
				}
			}
		}
		return minHeuristic + 1; // Action cost is 1.
	}
	
	@Override
	public boolean detectOrCycle(BeliefState beliefState) {
		if(this.getBeliefState().equals(beliefState)) {
			return true;
		}
		for(Map.Entry<AndNode, UnconditionalTermObservation> entry : this.getParents().entrySet()) {
			AndNode parent = entry.getKey();
			if(parent.detectOrCycle(beliefState)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean detectAndCycle(Action parentAction, BeliefState beliefState) {
		for(Map.Entry<AndNode, UnconditionalTermObservation> entry : this.getParents().entrySet()) {
			AndNode parent = entry.getKey();
			if(parent.detectAndCycle(parentAction, beliefState)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public DotNodeFormat getDotNodeFormat() {
		String label = this.getBeliefState() + ", t=" + this.getAvailableSensorActions() + ", v=" + this.getHeuristic();
		DotNodeStyle style = DotNodeStyle.OR_EXPLORED;
		if(this.getHeuristic() == Double.POSITIVE_INFINITY) {
			style = DotNodeStyle.OR_DEAD;
		} else if(this.isSolved()) {
			style = DotNodeStyle.OR_SOLVED;
		} else if(!this.isExpanded()) {
			style = DotNodeStyle.OR_LEAF;
		}
		return new DotNodeFormat(label, style);
	}

	@Override
	public String toString() {
		return "OR(" + this.getAvailableSensorActions() + ", " + this.getBeliefState() + ")";
	}

}
