package language;

import language.effect.deterministic.term.unconditional.constant.NullEffect;
import language.formula.term.constant.True;
import language.observation.deterministic.term.unconditional.constant.NoObservation;

public class Action {
	
	private Name name;
	private Formula precondition;
	private Effect effect;
	private Observation observation;
	
	public Action(Name name, Formula precondition, Effect effect, Observation observation) {
		this.setName(name);
		this.setPrecondition(precondition);
		this.setEffect(effect);
		this.setObservation(observation);
	}
	
	public Action(Name name, Formula precondition, Effect effect) {
		this(name, precondition, effect, new NoObservation());
	}
	
	public Action(Name name, Formula precondition, Observation observation) {
		this(name, precondition, new NullEffect(), observation);
	}
	
	public Action(Name name, Effect effect, Observation observation) {
		this(name, new True(), effect, observation);
	}
	
	public Action(Name name, Effect effect) {
		this(name, new True(), effect, new NoObservation());
	}
	
	public Action(Name name, Observation observation) {
		this(name, new True(), new NullEffect(), observation);
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
	
	public Observation getObservation() {
		return this.observation;
	}

	public void setObservation(Observation observation) {
		this.observation = observation;
	}
	
	public String pddl() {
		String output = "( " + Symbol.ACTION;
		if(!(this.getPrecondition() instanceof True)) {
			output += "\n\t" + Symbol.PRECONDITION + " " + this.getPrecondition();
		}
		output += "\n\t" + Symbol.EFFECT + " " + this.getEffect();
		if(!(this.getObservation() instanceof NoObservation)) {
			output += "\n\t" + Symbol.OBSERVATION + " " + this.getObservation();
		}
		output += "\n)";
		return output;
	}
	
	@Override
	public String toString() {
		String output = "(" + Symbol.ACTION + " " + this.getName();
		if(!(this.getPrecondition() instanceof True)) {
			output += " " + Symbol.PRECONDITION + " " + this.getPrecondition();
		}
		if(!(this.getEffect() instanceof NullEffect)) {
			output += " " + Symbol.EFFECT + " " + this.getEffect();
		}
		if(!(this.getObservation() instanceof NoObservation)) {
			output += " " + Symbol.OBSERVATION + " " + this.getObservation();
		}
		output += ")";
		return output;
//		return "[Action: " + this.getName() + " " + this.getPrecondition() + " " + this.getEffect() + " " + this.getObservation() + "]";
	}

}
