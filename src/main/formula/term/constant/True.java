package main.formula.term.constant;

import main.State;
import main.Symbol;
import main.formula.term.Constant;

public class True extends Constant {
	
	@Override
	public boolean satisfies(State state) {
		return true;
	}
	
	@Override
	public String toString() {
		return Symbol.TRUE;
//		return "[Constant: " + Symbol.TRUE + "]";
	}

}
