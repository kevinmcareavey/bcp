package language;

import java.util.List;

import structure.AdvancedSet;

public class DomainProblem {
	
	private Domain domain;
	private Problem problem;
	
	public DomainProblem(Domain domain, Problem problem) {
		this.setDomain(domain);
		this.setProblem(problem);
	}
	
	public DomainProblem(AdvancedSet<Atom> predicates, List<Action> actions, Init init, Formula goal) {
		this(new Domain(predicates, actions), new Problem(init, goal));
	}

	public Domain getDomain() {
		return this.domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Problem getProblem() {
		return this.problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

}
