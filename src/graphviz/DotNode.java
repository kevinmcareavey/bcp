package graphviz;

public class DotNode {
	
	public static int COUNTER = 0;
	
	private int id;
	
	public DotNode() {
		this.setId(COUNTER);
		COUNTER++;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return this.getId() + "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (!(obj instanceof DotNode)) {
			return false;
		}
		DotNode other = (DotNode) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
