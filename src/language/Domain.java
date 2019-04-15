package language;

import java.util.List;

import language.effect.deterministic.term.unconditional.constant.NullEffect;
import language.formula.term.constant.True;
import language.observation.deterministic.term.unconditional.constant.NoObservation;
import structure.AdvancedSet;

public class Domain {
	
	private Name name;
	private AdvancedSet<Atom> predicates;
	private List<Action> actions;
	
	public Domain(Name name, AdvancedSet<Atom> predicates, List<Action> actions) {
		this.setName(name);
		this.setPredicates(predicates);
		this.setActions(actions);
	}
	
	public Name getName() {
		return this.name;
	}

	public void setName(Name name) {
		this.name = name;
	}
	
	public AdvancedSet<Atom> getPredicates() {
		return this.predicates;
	}

	public void setPredicates(AdvancedSet<Atom> predicates) {
		this.predicates = predicates;
	}
	
	public List<Action> getActions() {
		return this.actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	@Override
	public String toString() {
		String output = "(" + Symbol.DEFINE + " (" + Symbol.DOMAIN + " " + this.getName() + ")";
		if(!this.getPredicates().isEmpty()) {
			output += "\n\t(" + Symbol.PREDICATES;
			for(Atom atom : this.getPredicates()) {
				output += " " + atom;
			}
			output += ")";
		}
		for(Action action : this.getActions()) {
			output += "\n\t(" + Symbol.ACTION + " " + action.getName();
			boolean empty = true;
			if(!(action.getPrecondition() instanceof True)) {
				output += "\n\t\t" + Symbol.PRECONDITION + " " + action.getPrecondition();
				empty = false;
			}
			if(!(action.getEffect() instanceof NullEffect)) {
				output += "\n\t\t" + Symbol.EFFECT + " " + action.getEffect();
				empty = false;
			}
			if(!(action.getObservation() instanceof NoObservation)) {
				output += "\n\t\t" + Symbol.OBSERVATION + " " + action.getObservation();
				empty = false;
			}
			if(!empty) {
				output += "\n\t";
			}
			output += ")";
		}
		output += "\n)";
		return output;
	}

}
