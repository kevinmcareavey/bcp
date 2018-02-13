package language.observation.deterministic.term.unconditional;

import language.Percept;
import language.observation.deterministic.term.UnconditionalTermObservation;

public abstract class LiteralObservation extends UnconditionalTermObservation {
	
	private Percept percept;
	
	public LiteralObservation(Percept percept) {
		this.setPercept(percept);
	}

	public Percept getPercept() {
		return this.percept;
	}

	public void setPercept(Percept percept) {
		this.percept = percept;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((percept == null) ? 0 : percept.hashCode());
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
		LiteralObservation other = (LiteralObservation) obj;
		if (percept == null) {
			if (other.percept != null)
				return false;
		} else if (!percept.equals(other.percept))
			return false;
		return true;
	}

}
