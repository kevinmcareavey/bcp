package language;

public class Name {
	
	private String label;
	
	public Name(String label) {
		this.setLabel(label);
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	/*
	 * A <name> is a string of characters starting with an alphabetic character followed by a 
	 * possibly empty sequence of alphanumeric characters, hyphens (-), and underscore characters (_).
	 */
	public static boolean isValid(String label) {
		return !Symbol.isValid(label) && label.matches("^[a-zA-Z][a-zA-Z0-9_-]*$");
	}

	@Override
	public String toString() {
		return this.getLabel();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		Name other = (Name) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

}
