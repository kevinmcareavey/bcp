package language.formula.term.constant;

import language.Symbol;
import language.formula.term.Constant;
import search.State;

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
