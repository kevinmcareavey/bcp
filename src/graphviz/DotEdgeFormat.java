package graphviz;

public class DotEdgeFormat {
	
	private DotEdgeStyle style;
	private String annotation;
	
	public DotEdgeFormat(DotEdgeStyle style, String annotation) {
		this.setAnnotation(annotation);
		this.setStyle(style);
	}
	
	public DotEdgeFormat(DotEdgeStyle style) {
		this(style, null);
	}
	
	public DotEdgeStyle getStyle() {
		return this.style;
	}

	public void setStyle(DotEdgeStyle style) {
		this.style = style;
	}
	
	public String getAnnotation() {
		return this.annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	@Override
	public String toString() {
		String output = "[";
		String delim = "";
		if(this.getAnnotation() != null) {
			output += "label=\"" + this.getAnnotation() + "\"";
			delim = " ";
		}
		switch(this.getStyle()) {
			case AND:
				output += delim + "style=dotted";
				break;
			case AND_DEAD:
				output += delim + "style=dotted color=red";
				break;
			case AND_SOLVED:
				output += delim + "style=dotted color=green3";
				break;
			case OR:
				output += delim + "style=solid";
				break;
			case OR_DEAD:
				output += delim + "style=solid color=red";
				break;
			case OR_SOLVED:
				output += delim + "style=solid color=green3";
				break;
			case START:
				output += delim + "style=solid dir=none";
				break;
			default:
				break;
		}
		output += "]";
		return output;
	}

}
