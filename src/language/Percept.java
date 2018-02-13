package language;

public class Percept extends Name {

	public Percept(String label) {
		super(label);
	}

	@Override
	public String toString() {
		return this.getLabel();
//		return "[Percept2: " + this.getLabel() + "]";
	}

}
