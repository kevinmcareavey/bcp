package language.formula.term.constant;

import language.Symbol;
import language.formula.term.Constant;
import planner.State;

public class False extends Constant {
	
	@Override
	public boolean satisfies(State state) {
		return false;
	}
	
	@Override
	public String toString() {
		return Symbol.FALSE;
	}

}
