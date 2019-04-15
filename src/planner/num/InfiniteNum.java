package planner.num;

import exception.InvalidNumException;
import language.Symbol;
import planner.Num;

public class InfiniteNum extends Num {
	
	@Override
	public boolean isMin() {
		return false;
	}
	
	@Override
	public Num decrement() throws InvalidNumException {
		return this;
	}
	
	@Override
	public int compareTo(Num other) {
		if(other instanceof InfiniteNum) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public String toString() {
		return Symbol.INFINITY;
	}

}
