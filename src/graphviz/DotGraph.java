package graphviz;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DotGraph {
	
	private String label;
	
	private Map<DotNode, DotNodeFormat> nodes;
	private Map<DotEdge, DotEdgeFormat> edges;
	
	public DotGraph(String label) {
		this.setLabel(label);
		this.setNodes(new HashMap<>());
		this.setEdges(new HashMap<>());
	}
	
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public Map<DotNode, DotNodeFormat> getNodes() {
		return this.nodes;
	}

	public void setNodes(Map<DotNode, DotNodeFormat> nodes) {
		this.nodes = nodes;
	}

	public Map<DotEdge, DotEdgeFormat> getEdges() {
		return this.edges;
	}

	public void setEdges(Map<DotEdge, DotEdgeFormat> edges) {
		this.edges = edges;
	}

//	public void setEdges(AdvancedOrderedSet<DotDirectedEdge> edges) {
//		AdvancedOrderedSet<DotNode> nodes = new AdvancedOrderedSet<>();
//		for(DotDirectedEdge directedEdge : edges) {
//			nodes.add(directedEdge.getLeft());
//			nodes.add(directedEdge.getRight());
//		}
//		this.setNodes(nodes);
//		this.edges = edges;
//	}
	
//	public void addEdge(DotDirectedEdge edge) {
//		nodes.add(edge.getLeft());
//		nodes.add(edge.getRight());
//		this.getEdges().add(edge);
//	}
	
	public DotNodeFormat getFormat(DotNode node) {
		return this.getNodes().get(node);
	}
	
	public void setFormat(DotNode node, DotNodeFormat format) {
		this.getNodes().put(node, format);
	}
	
	public void addEdge(DotEdge edge, DotEdgeFormat format) {
		this.getEdges().put(edge, format);
	}
	
	public void writeToPdf(String filename) throws IOException, InterruptedException {
		String basename = filename.substring(0, filename.length() - 4);
    	
    	BufferedWriter writer = new BufferedWriter(new FileWriter(basename + ".dot"));
    	writer.write(this.toString());
		writer.close();
		
		Process process;
		
		String[] dot = {"/usr/local/bin/dot", "-Tpdf", basename + ".dot", "-o", filename};
		process = Runtime.getRuntime().exec(dot);
		process.waitFor();
		if(process.exitValue() != 0) {
			throw new IOException();
		}
		
		String[] rm = {"/bin/rm", basename + ".dot"};
		process = Runtime.getRuntime().exec(rm);
		process.waitFor();
		if(process.exitValue() != 0) {
			throw new IOException();
		}
	}

	@Override
	public String toString() {
		String output = "digraph " + this.getLabel() + " {\n";
		for(Map.Entry<DotNode, DotNodeFormat> entry : this.getNodes().entrySet()) {
			DotNode node = entry.getKey();
			DotNodeFormat format = entry.getValue();
			output += "\t" + node + " " + format + ";\n";
		}
		for(Map.Entry<DotEdge, DotEdgeFormat> entry : this.getEdges().entrySet()) {
			DotEdge edge = entry.getKey();
			DotEdgeFormat format = entry.getValue();
			output += "\t" + edge + " " + format + ";\n";
		}
		output += "}";
		return output;
	}

}
