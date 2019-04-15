package graphviz;

public class DotEdge {
	
	private DotNode left;
	private DotNode right;
	
	public DotEdge(DotNode left, DotNode right) {
		this.setLeft(left);
		this.setRight(right);
	}

	public DotNode getLeft() {
		return this.left;
	}

	public void setLeft(DotNode left) {
		this.left = left;
	}

	public DotNode getRight() {
		return this.right;
	}

	public void setRight(DotNode right) {
		this.right = right;
	}

	@Override
	public String toString() {
//		String output = this.getLeft().getId() + " -> " + this.getRight().getId() + " [";
//		String delim = "";
//		if(this.getLabel() != null) {
//			output += "label=\"" + this.getLabel() + "\"";
//			delim = " ";
//		}
//		switch(this.getStyle()) {
//			case DOTTED:
//				output += delim + "style=dotted";
//				break;
//			case NONE:
//				output += delim + "style=bold dir=none";
//				break;
//			case SOLID:
//				output += delim + "style=solid";
//				break;
//			default:
//				break;
//		}
//		output += "];";
//		return output;
		return this.getLeft() + " -> " + this.getRight();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DotEdge)) {
			return false;
		}
		DotEdge other = (DotEdge) obj;
		if (left == null) {
			if (other.left != null) {
				return false;
			}
		} else if (!left.equals(other.left)) {
			return false;
		}
		if (right == null) {
			if (other.right != null) {
				return false;
			}
		} else if (!right.equals(other.right)) {
			return false;
		}
		return true;
	}

}
