package language;

import java.util.List;

import structure.AdvancedSet;

public class Domain {
	
	private AdvancedSet<Atom> predicates;
	private List<Action> actions;
	
	public Domain(AdvancedSet<Atom> predicates, List<Action> actions) {
		this.setPredicates(predicates);
		this.setActions(actions);
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

}
