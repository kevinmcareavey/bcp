package main;

import main.effect.deterministic.term.unconditional.constant.NullEffect;
import main.formula.term.constant.True;

public class Action {
	
	public static final Action NOOP = new Action("noop", new NullEffect());
	
	private Name name;
	private Formula precondition;
	private Effect effect;
	
	public Action(Name name, Formula precondition, Effect effect) {
		this.setName(name);
		this.setPrecondition(precondition);
		this.setEffect(effect);
	}
	
	public Action(String label, Formula precondition, Effect effect) {
		this.setName(new Name(label));
		this.setPrecondition(precondition);
		this.setEffect(effect);
	}
	
	public Action(Name name, Effect effect) {
		this(name, new True(), effect);
	}
	
	public Action(String label, Effect effect) {
		this(label, new True(), effect);
	}
	
	public Name getName() {
		return this.name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Formula getPrecondition() {
		return this.precondition;
	}

	public void setPrecondition(Formula precondition) {
		this.precondition = precondition;
	}
	
	public Effect getEffect() {
		return this.effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}
	
	public String pddl() {
		String output = "(:action ";
		if(!(this.getPrecondition() instanceof True)) {
			output += "\n\t:precondition "+ this.getPrecondition();
		}
		output += "\n\t:effect " + this.getEffect() + "\n)";
		return output;
	}
	
	@Override
	public String toString() {
		return "(" + Symbol.ACTION + " " + this.getName() + " " 
				+ Symbol.PRECONDITION + " " + this.getPrecondition() + " " 
				+ Symbol.EFFECT + " " + this.getEffect() 
				+ ")";
//		return "[Action: " + this.getName() + " " + this.getPrecondition() + " " + this.getEffect() + "]";
	}

}
