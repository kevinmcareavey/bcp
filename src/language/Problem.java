package language;

public class Problem {
	
	private Init init;
	private Formula goal;
	
	public Problem(Init init, Formula goal) {
		this.setInit(init);
		this.setGoal(goal);
	}

	public Init getInit() {
		return this.init;
	}

	public void setInit(Init init) {
		this.init = init;
	}

	public Formula getGoal() {
		return this.goal;
	}

	public void setGoal(Formula goal) {
		this.goal = goal;
	}

}
