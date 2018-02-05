package main.formula.term.constant;

import main.State;
import main.Symbol;
import main.formula.term.Constant;

public class False extends Constant {
	
	@Override
	public boolean satisfies(State state) {
		return false;
	}
	
	@Override
	public String toString() {
		return Symbol.FALSE;
//		return "[Constant: " + Symbol.FALSE + "]";
	}

}
