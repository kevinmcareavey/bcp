package graphviz;

import structure.AdvancedOrderedSet;

public class DirectedGraph {
	
	private String label;
	private AdvancedOrderedSet<Node> nodes;
	private AdvancedOrderedSet<DirectedEdge> edges;
	
	public DirectedGraph(String label, AdvancedOrderedSet<DirectedEdge> edges) {
		this.setLabel(label);
		this.setEdges(edges);
	}
	
	public DirectedGraph(String label) {
		this(label, new AdvancedOrderedSet<>());
	}
	
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public AdvancedOrderedSet<Node> getNodes() {
		return this.nodes;
	}
	
	public void setNodes(AdvancedOrderedSet<Node> nodes) {
		this.nodes = nodes;
	}
	
	public AdvancedOrderedSet<DirectedEdge> getEdges() {
		return this.edges;
	}

	public void setEdges(AdvancedOrderedSet<DirectedEdge> edges) {
		AdvancedOrderedSet<Node> nodes = new AdvancedOrderedSet<>();
		for(DirectedEdge directedEdge : edges) {
			nodes.add(directedEdge.getLeft());
			nodes.add(directedEdge.getRight());
		}
		this.setNodes(nodes);
		this.edges = edges;
	}
	
	public void addEdge(DirectedEdge edge) {
		nodes.add(edge.getLeft());
		nodes.add(edge.getRight());
		this.getEdges().add(edge);
	}

	@Override
	public String toString() {
		String output = "digraph " + this.getLabel() + " {\n";
		for(Node node : this.getNodes()) {
			output += "\t" + node + "\n";
		}
		for(DirectedEdge directedEdge : this.getEdges()) {
			output += "\t" + directedEdge + "\n";
		}
		output += "}";
		return output;
	}

}
