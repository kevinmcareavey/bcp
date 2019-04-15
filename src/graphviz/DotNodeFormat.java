package graphviz;

public class DotNodeFormat {
	
	private String label;
	private DotNodeStyle style;
	
	public DotNodeFormat(String label, DotNodeStyle style) {
		this.setLabel(label);
		this.setStyle(style);
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public DotNodeStyle getStyle() {
		return this.style;
	}

	public void setStyle(DotNodeStyle style) {
		this.style = style;
	}

	@Override
	public String toString() {
		String output = "[";
		String delim = "";
		if(this.getLabel() != null) {
			output += "label=\"" + this.getLabel() + "\"";
			delim = " ";
		}
		switch(this.getStyle()) {
			case AND_DEAD:
				output += delim + "style=\"filled,dotted\" fillcolor=red";
				break;
			case AND_EXPLORED:
				output += delim + "style=dotted";
				break;
			case AND_SOLVED:
				output += delim + "style=\"filled,dotted\" fillcolor=green3";
				break;
			case OR_DEAD:
				output += delim + "style=\"filled,solid\" fillcolor=red";
				break;
			case OR_EXPLORED:
				output += delim + "style=solid";
				break;
			case OR_LEAF:
				output += delim + "style=\"filled,solid\" fillcolor=gray";
				break;
			case OR_SOLVED:
				output += delim + "style=\"filled,solid\" fillcolor=green3";
				break;
			case START:
				output += delim + "style=\"filled,solid\" fillcolor=black fontcolor=white";
				break;
			default:
				break;
		}
		output += "]";
		return output;
	}
	
}
