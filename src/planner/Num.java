package planner;

import exception.InvalidNumException;
import planner.num.FiniteNum;

public abstract class Num implements Comparable<Num> {
	
	public static Num getMin() throws InvalidNumException {
		return new FiniteNum(0);
	}
	
	public abstract boolean isMin();
	
	public abstract Num decrement() throws InvalidNumException;

}
