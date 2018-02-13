package graphviz;

public class DirectedEdge {
	
	private Node left;
	private Node right;
	private String label;
	private EdgeStyle style;
	
	public DirectedEdge(Node left, Node right, String label, EdgeStyle style) {
		this.setLeft(left);
		this.setRight(right);
		this.setLabel(label);
		this.setStyle(style);
	}
	
	public DirectedEdge(Node left, Node right, EdgeStyle style) {
		this(left, right, null, style);
	}

	public Node getLeft() {
		return this.left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return this.right;
	}

	public void setRight(Node right) {
		this.right = right;
	}
	
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public EdgeStyle getStyle() {
		return this.style;
	}

	public void setStyle(EdgeStyle style) {
		this.style = style;
	}

	@Override
	public String toString() {
		String output = this.getLeft().getId() + " -> " + this.getRight().getId() + " [";
		String delim = "";
		if(this.getLabel() != null) {
			output += "label=\"" + this.getLabel() + "\"";
			delim = " ";
		}
		switch(this.getStyle()) {
			case DOTTED:
				output += delim + "style=dotted";
				break;
			case NONE:
				output += delim + "style=bold dir=none";
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		result = prime * result + ((style == null) ? 0 : style.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DirectedEdge other = (DirectedEdge) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		if (style != other.style)
			return false;
		return true;
	}

}
