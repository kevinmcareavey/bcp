package language;

public class Problem {
	
	private Name name;
	private Name domainName;
	private Init init;
	private Formula goal;
	
	public Problem(Name name, Name domainName, Init init, Formula goal) {
		this.setName(name);
		this.setDomainName(domainName);
		this.setInit(init);
		this.setGoal(goal);
	}
	
	public Name getName() {
		return this.name;
	}

	public void setName(Name name) {
		this.name = name;
	}
	
	public Name getDomainName() {
		return this.domainName;
	}

	public void setDomainName(Name domainName) {
		this.domainName = domainName;
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

	@Override
	public String toString() {
		String output = "(" + Symbol.DEFINE + " (" + Symbol.PROBLEM + " " + this.getName() + ")";
		output += "\n\t(" + Symbol.DOMAIN_REF + " " + this.getDomainName() + ")";
		output += "\n\t(" + Symbol.INIT + " " + this.getInit() + ")";
		output += "\n\t(" + Symbol.GOAL + " " + this.getGoal() + ")";
		output += "\n)";
		return output;
	}

}
