package search;

import java.util.LinkedList;

public class Path extends LinkedList<BeliefState> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1712984602914857941L;

	public Path append(BeliefState beliefState) {
		Path path = new Path();
		path.addAll(this);
		path.add(beliefState);
		return path;
	}
	
	public Path prepend(BeliefState beliefState) {
		Path path = new Path();
		path.add(beliefState);
		path.addAll(this);
		return path;
	}

}
