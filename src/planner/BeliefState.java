package planner;

import structure.AdvancedSet;

public class BeliefState extends AdvancedSet<State> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2101658785305839044L;
	
	public BeliefState(State... states) {
		super(states);
	}

	@Override
	public String toString() {
		if(this.size() == 8) {
			return "S";
		} else {
			return super.toString();
		}
	}

}
