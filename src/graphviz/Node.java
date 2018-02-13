package graphviz;

public class Node {
	
	public static int COUNTER = 0;
	
	private int id;
	private String label;
	private NodeStyle style;
	
	public Node(int id, String label, NodeStyle style) {
		COUNTER++;
		this.setId(id);
		this.setLabel(label);
		this.setStyle(style);
	}
	
	public Node(int id, NodeStyle style) {
		this(id, null, style);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public NodeStyle getStyle() {
		return this.style;
	}

	public void setStyle(NodeStyle style) {
		this.style = style;
	}

	@Override
	public String toString() {
		String output = this.getId() + " [";
		String delim = "";
		if(this.getLabel() != null) {
			output += "label=\"" + this.getLabel() + "\"";
			delim = " ";
		}
		switch(this.getStyle()) {
			case BOLD:
				output += delim + "style=bold fontname=bold";
				break;
			case DOTTED:
				output += delim + "style=dotted";
				break;
			case FILLED:
				output += delim + "style=filled fontname=bold fillcolor=black fontcolor=white";
				break;
			case SOLID:
				output += delim + "style=solid";
				break;
			default:
				break;
		}
		output += "];";
		return output;
	}

}
