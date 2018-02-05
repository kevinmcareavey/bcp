package main.formula;

import java.util.Arrays;
import java.util.List;

import main.Formula;
import main.State;
import main.Symbol;

public class ConjunctiveFormula extends Formula {
	
	private List<Term> conjuncts;
	
	public ConjunctiveFormula(List<Term> conjuncts) {
		this.setTerms(conjuncts);
	}
	
	public ConjunctiveFormula(Term... conjuncts) {
		this(Arrays.asList(conjuncts));
	}

	public List<Term> getTerms() {
		return this.conjuncts;
	}

	public void setTerms(List<Term> conjuncts) {
		this.conjuncts = conjuncts;
	}
	
	@Override
	public boolean satisfies(State state) {
		for(Term conjunct : this.getTerms()) {
			if(!conjunct.satisfies(state)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		String output = "(" + Symbol.CONJUNCTION;
//		String output = "[ConjunctiveFormula:";
		for(Term conjunct : this.getTerms()) {
			output += " " + conjunct.toString();
		}
		output += ")";
//		output += "]";
		return output;
	}

}
