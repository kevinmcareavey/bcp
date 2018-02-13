package language;

public class Atom extends Name {

	public Atom(String label) {
		super(label);
	}

	@Override
	public String toString() {
		return this.getLabel();
//		return "[Atom: " + this.getLabel() + "]";
	}

}
