package language.action;

import language.Action;
import language.Name;
import language.Symbol;
import language.effect.deterministic.term.unconditional.constant.NullEffect;

public class NoAction extends Action {

	public NoAction() {
		super(new Name(Symbol.NOOP), new NullEffect());
	}

}
