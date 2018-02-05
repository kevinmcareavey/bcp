package main;

public abstract class Formula {
	
	public abstract boolean satisfies(State state);
	
	public boolean satisfies(BeliefState beliefState) {
		if(beliefState.isEmpty()) {
			return false;
		}
		for(State state : beliefState) {
			if(!this.satisfies(state)) {
				return false;
			}
		}
		return true;
	}
	
	public AdvancedSet<State> getModels(AdvancedSet<State> states) {
		AdvancedSet<State> model = new AdvancedSet<>();
		for(State state : states) {
			if(this.satisfies(state)) {
				model.add(state);
			}
		}
		return model;
    }

}
