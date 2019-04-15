package planner.num;

import exception.InvalidNumException;
import planner.Num;

public class FiniteNum extends Num {
	
	private Integer value;
	
	public FiniteNum(int value) throws InvalidNumException {
		this.setValue(value);
	}

	public Integer getValue() {
		return this.value;
	}

	public void setValue(int value) throws InvalidNumException {
		if(value < 0) {
			throw new InvalidNumException(value);
		}
		this.value = value;
	}
	
	@Override
	public boolean isMin() {
		return this.getValue() == 0;
	}
	
	@Override
	public Num decrement() throws InvalidNumException {
		int revisedValue = this.getValue() - 1;
		return new FiniteNum(revisedValue);
	}

	@Override
	public int compareTo(Num other) {
		if(other instanceof InfiniteNum) {
			return -1;
		} else {
			FiniteNum otherFinite = (FiniteNum) other;
			return this.getValue().compareTo(otherFinite.getValue());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (!(obj instanceof FiniteNum)) {
			return false;
		}
		FiniteNum other = (FiniteNum) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return this.getValue().toString();
	}

}
