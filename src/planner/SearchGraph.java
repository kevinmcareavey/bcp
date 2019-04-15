package planner;

import java.util.HashMap;
import java.util.Map;

import graphviz.DotEdge;
import graphviz.DotEdgeFormat;
import graphviz.DotEdgeStyle;
import graphviz.DotGraph;
import graphviz.DotNode;
import graphviz.DotNodeFormat;
import graphviz.DotNodeStyle;
import language.Action;
import language.Formula;
import language.observation.deterministic.term.UnconditionalTermObservation;
import planner.node.AndNode;
import planner.node.OrNode;

public class SearchGraph {
	
	private BeliefState initialBeliefState;
	private Formula goal;
	
	private Num bound;
	
	private Map<Num, Map<BeliefState, OrNode>> orNodes;
	private Map<Num, Map<Action, Map<BeliefState, AndNode>>> andNodes;
	
	public SearchGraph(BeliefState root, Formula goal, Num bound) {
		this.setInitialBeliefState(root);
		this.setGoal(goal);
		this.setBound(bound);
		
		this.setOrNodes(new HashMap<>());
		this.setAndNodes(new HashMap<>());
	}
	
	public BeliefState getInitialBeliefState() {
		return this.initialBeliefState;
	}

	public void setInitialBeliefState(BeliefState root) {
		this.initialBeliefState = root;
	}
	
	public Formula getGoal() {
		return this.goal;
	}

	public void setGoal(Formula goal) {
		this.goal = goal;
	}
	
	public Num getBound() {
		return this.bound;
	}

	public void setBound(Num bound) {
		this.bound = bound;
	}
	
	public Map<Num, Map<BeliefState, OrNode>> getOrNodes() {
		return this.orNodes;
	}

	public void setOrNodes(Map<Num, Map<BeliefState, OrNode>> orNodes) {
		this.orNodes = orNodes;
	}

	public Map<Num, Map<Action, Map<BeliefState, AndNode>>> getAndNodes() {
		return this.andNodes;
	}

	public void setAndNodes(Map<Num, Map<Action, Map<BeliefState, AndNode>>> andNodes) {
		this.andNodes = andNodes;
	}
	
	public OrNode getRoot() {
		return this.getOrNode(this.getBound(), this.getInitialBeliefState());
	}
	
	public OrNode getOrNode(Num priorSensorActions, BeliefState beliefState) {
		if(this.getOrNodes().containsKey(priorSensorActions) && this.getOrNodes().get(priorSensorActions).containsKey(beliefState)) {
			return this.getOrNodes().get(priorSensorActions).get(beliefState);
		} else {
			OrNode orNode = new OrNode(beliefState, priorSensorActions, this.getGoal());
			if(!this.getOrNodes().containsKey(priorSensorActions)) {
				this.getOrNodes().put(priorSensorActions, new HashMap<>());
			}
			this.getOrNodes().get(priorSensorActions).put(beliefState, orNode);
			return orNode;
		}
	}
	
	public AndNode getAndNode(Num priorSensorActions, Action parentAction, BeliefState beliefState) {
		if(this.getAndNodes().containsKey(priorSensorActions) 
				&& this.getAndNodes().get(priorSensorActions).containsKey(parentAction) 
				&& this.getAndNodes().get(priorSensorActions).get(parentAction).containsKey(beliefState)) {
			return this.getAndNodes().get(priorSensorActions).get(parentAction).get(beliefState);
		} else {
			AndNode andNode = new AndNode(parentAction, beliefState, priorSensorActions);
			if(!this.getAndNodes().containsKey(priorSensorActions)) {
				this.getAndNodes().put(priorSensorActions, new HashMap<>());
			}
			if(!this.getAndNodes().get(priorSensorActions).containsKey(parentAction)) {
				this.getAndNodes().get(priorSensorActions).put(parentAction, new HashMap<>());
			}
			this.getAndNodes().get(priorSensorActions).get(parentAction).put(beliefState, andNode);
			return andNode;
		}
	}
	
	public AndNode addOrEdge(Num priorSensorActions, BeliefState parent, Action action, BeliefState child) {
		OrNode parentNode = this.getOrNode(priorSensorActions, parent);
		AndNode childNode = this.getAndNode(priorSensorActions, action, child);
		
		parentNode.getChildren().put(action, childNode);
		childNode.getParents().add(parentNode);
		
		return childNode;
	}
	
	public OrNode addAndEdge(Num priorSensorActions, Action parentAction, BeliefState parent, UnconditionalTermObservation observation, Num revisedSensorActions, BeliefState child) {
		AndNode parentNode = this.getAndNode(priorSensorActions, parentAction, parent);
		OrNode childNode = this.getOrNode(revisedSensorActions, child);
		
		parentNode.getChildren().put(observation, childNode);
		childNode.getParents().put(parentNode, observation);
		
		return childNode;
	}
	
	public DotGraph getDotGraph() {
		DotGraph dotGraph = new DotGraph("trace");
		
		DotNode startNode = new DotNode();
		dotGraph.setFormat(startNode, new DotNodeFormat("START", DotNodeStyle.START));
		dotGraph.addEdge(new DotEdge(startNode, this.getRoot().getDotNode()), new DotEdgeFormat(DotEdgeStyle.START));
		
		for(Map.Entry<Num, Map<BeliefState, OrNode>> outerEntry : this.getOrNodes().entrySet()) {
			for(Map.Entry<BeliefState, OrNode> entry : outerEntry.getValue().entrySet()) {
				OrNode parent = entry.getValue();
				dotGraph.setFormat(parent.getDotNode(), parent.getDotNodeFormat());
				for(Map.Entry<Action, AndNode> innerEntry : parent.getChildren().entrySet()) {
					Action action = innerEntry.getKey();
					AndNode child = innerEntry.getValue();
					
					DotEdgeStyle style = DotEdgeStyle.OR;
					if(child.getHeuristic() == Double.POSITIVE_INFINITY) {
						style = DotEdgeStyle.OR_DEAD;
					} else if(child.isSolved()) {
						style = DotEdgeStyle.OR_SOLVED;
					}
					
					dotGraph.addEdge(new DotEdge(parent.getDotNode(), child.getDotNode()), new DotEdgeFormat(style, action.getName().toString()));
				}
			}
		}
		
		for(Map.Entry<Num, Map<Action, Map<BeliefState, AndNode>>> firstEntry : this.getAndNodes().entrySet()) {
			for(Map.Entry<Action, Map<BeliefState, AndNode>> secondEntry : firstEntry.getValue().entrySet()) {
				for(Map.Entry<BeliefState, AndNode> thirdEntry : secondEntry.getValue().entrySet()) {
					AndNode parent = thirdEntry.getValue();
					dotGraph.setFormat(parent.getDotNode(), parent.getDotNodeFormat());
					
					for(Map.Entry<UnconditionalTermObservation, OrNode> fourthEntry : parent.getChildren().entrySet()) {
						UnconditionalTermObservation observation = fourthEntry.getKey();
						OrNode child = fourthEntry.getValue();
						
						DotEdgeStyle style = DotEdgeStyle.AND;
						if(child.getHeuristic() == Double.POSITIVE_INFINITY) {
							style = DotEdgeStyle.AND_DEAD;
						} else if(child.isSolved()) {
							style = DotEdgeStyle.AND_SOLVED;
						}
						
						dotGraph.addEdge(new DotEdge(parent.getDotNode(), child.getDotNode()), new DotEdgeFormat(style, observation.toString()));
					}
				}
			}
		}
		return dotGraph;
	}

	@Override
	public String toString() {
		return this.getDotGraph().toString();
	}
	
}
