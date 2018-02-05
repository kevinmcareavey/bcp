package main;

import exceptions.EmptyBeliefStateException;
import exceptions.InconsistencyException;

public class AndOrSearch {
	
	public Plan search(Problem problem) throws InconsistencyException, EmptyBeliefStateException {
		return this.orSearch(problem.getInitialBeliefState(), problem, new Path());
	}
	
	private Plan orSearch(BeliefState beliefState, Problem problem, Path path) throws InconsistencyException, EmptyBeliefStateException {
		if(problem.isGoal(beliefState)) {
			return new Plan();
		}
		
		if(path.contains(beliefState)) {
			return null;
		}
		
		for(Action action : problem.getExecutableActions(beliefState)) {
			Plan plan = this.andSearch(problem.getSuccessorBeliefState(beliefState, action), problem, path.prepend(beliefState));
			if(plan != null) {
				plan.setAction(action);
				return plan;
			}
		}
		
		return null;
	}
	
	private Plan andSearch(BeliefState beliefState, Problem problem, Path path) throws InconsistencyException, EmptyBeliefStateException {
		Plan plan = new Plan();
		for(Percept percept : problem.getPercepts(beliefState)) {
			BeliefState updatedBeliefState = problem.updateBeliefState(beliefState, percept);
			Plan subplan = this.orSearch(updatedBeliefState, problem, path);
			if(subplan == null) {
				return null;
			}
			plan.addSubplan(percept, subplan);
		}
		return plan;
	}

}
