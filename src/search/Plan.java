package search;

import java.util.HashMap;
import java.util.Map;

import graphviz.DirectedEdge;
import graphviz.DirectedGraph;
import graphviz.EdgeStyle;
import graphviz.Node;
import graphviz.NodeStyle;
import language.Action;
import language.action.NoAction;
import language.observation.deterministic.term.UnconditionalTermObservation;
import language.observation.deterministic.term.unconditional.constant.NoObservation;

public class Plan {
	
	private Action action;
	private Map<UnconditionalTermObservation, Plan> subplans;
	
	public Plan() {
		this.setAction(new NoAction());
		this.setSubplans(null);
	}
	
	public Action getAction() {
		return this.action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Map<UnconditionalTermObservation, Plan> getSubplans() {
		return this.subplans;
	}

	public void setSubplans(Map<UnconditionalTermObservation, Plan> subplans) {
		this.subplans = subplans;
	}
	
	public void addSubplan(UnconditionalTermObservation observation, Plan plan) {
		if(this.getSubplans() == null) {
			this.setSubplans(new HashMap<UnconditionalTermObservation, Plan>());
		}
		this.getSubplans().put(observation, plan);
	}
	
	public int depth() {
		int maxDepth = 0;
		if(this.getSubplans() != null) {
			for(Map.Entry<UnconditionalTermObservation, Plan> entry : this.getSubplans().entrySet()) {
				Plan plan = entry.getValue();
				int childDepth = plan.depth();
				if(childDepth > maxDepth) {
					maxDepth = childDepth;
				}
			}
		}
		return maxDepth + 1;
	}
	
	public int nodes() {
		int sum = 0;
		if(this.getSubplans() != null) {
			for(Map.Entry<UnconditionalTermObservation, Plan> entry : this.getSubplans().entrySet()) {
				Plan plan = entry.getValue();
				sum += plan.nodes();
			}
		}
		return sum + 1;
	}
	
	public int leafs() {
		if(this.getSubplans() == null || this.getSubplans().isEmpty()) {
			return 1;
		} else {
			int sum = 0;
			if(this.getSubplans() != null) {
				for(Map.Entry<UnconditionalTermObservation, Plan> entry : this.getSubplans().entrySet()) {
					Plan plan = entry.getValue();
					sum += plan.leafs();
				}
			}
			return sum;
		}
	}
	
	public DirectedGraph getDirectedGraph() {
		Node parent = new Node(Node.COUNTER, this.getAction().getName().toString(), NodeStyle.SOLID);
		return this.getDirectedGraph(new DirectedGraph("plan"), parent);
	}
	
	private DirectedGraph getDirectedGraph(DirectedGraph directedGraph, Node parent) {
		if(this.getSubplans() != null) {
			for(Map.Entry<UnconditionalTermObservation, Plan> entry : this.getSubplans().entrySet()) {
				UnconditionalTermObservation observation = entry.getKey();
				Plan plan = entry.getValue();
				Node child = new Node(Node.COUNTER, plan.getAction().getName().toString(), NodeStyle.SOLID);
				if(this.getSubplans().size() > 1) {
					directedGraph.addEdge(new DirectedEdge(parent, child, observation.toString(), EdgeStyle.DOTTED));
				} else {
					directedGraph.addEdge(new DirectedEdge(parent, child, EdgeStyle.SOLID));
				}
				directedGraph = plan.getDirectedGraph(directedGraph, child);
			}
		}
		return directedGraph;
	}
	
	@Override
	public String toString() {
		String output = "[" + action.getName();
		if(subplans != null) {
			output += ", ";
			String delim = "";
			for(Map.Entry<UnconditionalTermObservation, Plan> entry : this.getSubplans().entrySet()) {
				UnconditionalTermObservation observation = entry.getKey();
				Plan plan = entry.getValue();
//				if(this.getSubplans().size() == 1) {
				if(observation instanceof NoObservation) {
					output += delim + plan.toString().substring(1, plan.toString().length() - 1);
				} else {
					output += delim + "if " + observation + " then " + plan;
				}
				delim = ", ";
			}
		}
		output += "]";
		return output;
	}

}
