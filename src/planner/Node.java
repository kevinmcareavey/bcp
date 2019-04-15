package planner;

import graphviz.DotNode;
import graphviz.DotNodeFormat;
import language.Action;

public abstract class Node {
	
	private BeliefState beliefState;
	private Num availableSensorActions;
	
	private DotNode dotNode;
	
	public Node(BeliefState beliefState, Num availableSensorActions) {
		this.setBeliefState(beliefState);
		this.setAvailableSensorActions(availableSensorActions);
		
		this.setDotNode(new DotNode());
	}
	
	public Num getAvailableSensorActions() {
		return this.availableSensorActions;
	}

	public void setAvailableSensorActions(Num availableSensorActions) {
		this.availableSensorActions = availableSensorActions;
	}
	
	public BeliefState getBeliefState() {
		return this.beliefState;
	}

	public void setBeliefState(BeliefState beliefState) {
		this.beliefState = beliefState;
	}
	
	public DotNode getDotNode() {
		return this.dotNode;
	}

	public void setDotNode(DotNode dotNode) {
		this.dotNode = dotNode;
	}
	
	public abstract boolean isSolved();
	public abstract double getHeuristic();
	public abstract double getSolvedHeuristic();
	
	public abstract boolean detectOrCycle(BeliefState beliefState);
	public abstract boolean detectAndCycle(Action parentAction, BeliefState beliefState);
	
	public abstract DotNodeFormat getDotNodeFormat();

}
