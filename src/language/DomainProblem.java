package language;

import java.util.List;

import exception.DomainMismatchException;
import structure.AdvancedSet;

public class DomainProblem {
	
	private Domain domain;
	private Problem problem;
	
	public DomainProblem(Domain domain, Problem problem) throws DomainMismatchException {
		if(!domain.getName().equals(problem.getDomainName())) {
			throw new DomainMismatchException(domain, problem);
		}
		this.setDomain(domain);
		this.setProblem(problem);
	}
	
	public DomainProblem(Name domainName, AdvancedSet<Atom> predicates, List<Action> actions, Name problemName, Init init, Formula goal) throws DomainMismatchException {
		this(new Domain(domainName, predicates, actions), new Problem(problemName, domainName, init, goal));
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
	
	@Override
	public String toString() {
		return this.getDomain().toString() + "\n" + this.getProblem().toString();
	}

}
