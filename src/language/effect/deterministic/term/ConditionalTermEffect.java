package language.effect.deterministic.term;

import language.Atom;
import language.Formula;
import language.Symbol;
import language.effect.deterministic.TermEffect;
import planner.State;
import structure.AdvancedSet;

public class ConditionalTermEffect extends TermEffect {
	
	private Formula condition;
	private UnconditionalTermEffect effect;
	
	public ConditionalTermEffect(Formula condition, UnconditionalTermEffect effect) {
		this.setCondition(condition);
		this.setEffect(effect);
	}

	public Formula getCondition() {
		return this.condition;
	}

	public void setCondition(Formula condition) {
		this.condition = condition;
	}

	public UnconditionalTermEffect getEffect() {
		return this.effect;
	}

	public void setEffect(UnconditionalTermEffect effect) {
		this.effect = effect;
	}
	
	@Override
	public AdvancedSet<Atom> getDeleteList(State state) {
		if(this.getCondition().satisfies(state)) {
			return this.getEffect().getDeleteList(state);
		} else {
			return new AdvancedSet<Atom>();
		}
	}
	
	@Override
	public AdvancedSet<Atom> getAddList(State state) {
		if(this.getCondition().satisfies(state)) {
			return this.getEffect().getAddList(state);
		} else {
			return new AdvancedSet<Atom>();
		}
	}

	@Override
	public String toString() {
		return "(" + Symbol.CONDITIONAL + " " + this.getCondition() + " " + this.getEffect() + ")";
//		return "[ConditionalTermEffect: " + this.getCondition() + " " + this.getEffect() + "]";
	}

}
