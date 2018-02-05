package parser;

import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import main.Action;
import main.AdvancedSet;
import main.Atom;
import main.Effect;
import main.Formula;
import main.Init;
import main.Name;
import main.Problem;
import main.Symbol;
import main.effect.DeterministicEffect;
import main.effect.deterministic.ConjunctiveEffect;
import main.effect.deterministic.TermEffect;
import main.effect.deterministic.term.ConditionalTermEffect;
import main.effect.deterministic.term.UnconditionalTermEffect;
import main.effect.deterministic.term.unconditional.ConstantEffect;
import main.effect.deterministic.term.unconditional.LiteralEffect;
import main.effect.deterministic.term.unconditional.constant.NullEffect;
import main.effect.deterministic.term.unconditional.literal.NegativeLiteralEffect;
import main.effect.deterministic.term.unconditional.literal.PositiveLiteralEffect;
import main.effect.nondeterministic.OneOfEffect;
import main.formula.ConjunctiveFormula;
import main.formula.Term;
import main.formula.term.Constant;
import main.formula.term.Literal;
import main.formula.term.constant.False;
import main.formula.term.constant.True;
import main.formula.term.literal.NegativeLiteral;
import main.formula.term.literal.PositiveLiteral;
import main.init.DeterministicInit;
import main.init.deterministic.ConjunctiveInit;
import main.init.deterministic.PositiveLiteralInit;
import main.init.nondeterministic.OneOfInit;

public class Parser {
	
	private Deque<String> tokens;
	
	public Problem parse(String s) throws ParseException {
		tokens = tokenize(s);
		
		Problem t = problem();
		
		if(!tokens.isEmpty()) {
			throw new ParseException("unprocessed tokens remain after parsing", tokens.size());
		}
		
		return t;
	}
	
	private Deque<String> tokenize(String s) {
		Deque<String> input = new ArrayDeque<String>();
		
		s = s.replace(Symbol.LEFT_PARENTHESIS, " ( ");
		s = s.replace(Symbol.RIGHT_PARENTHESIS, " ) ");
		
		s = s.trim();
		
		String[] tokens = s.split("\\s+");
		for(String token : tokens) {
			if(!token.isEmpty()) {
				input.addLast(token);
			}
		}
		
		return input;
	}
	
	private String next() throws ParseException {
		if(tokens.isEmpty()) {
			throw new ParseException("unexpected end of input", tokens.size());
		}
		
		while(tokens.getFirst().isEmpty()) {
			consume();
		}
		
		return tokens.getFirst();
	}
	
	private String afterNext() throws ParseException {
		String next = next();
		consume();
		String afterNext = next();
		tokens.addFirst(next);
		return afterNext;
	}
	
	private boolean end() throws ParseException {
		return tokens.isEmpty() || next().equals(Symbol.RIGHT_PARENTHESIS);
	}
	
	private void consume() {
		tokens.removeFirst();
	}
	
	private void expect(String s) throws ParseException {
		if(next().equals(s)) {
			consume();
		} else {
			throw new ParseException("expected '" + s + "', found '" + next() + "'", tokens.size());
		}
	}
	
	private Problem problem() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
			expect(Symbol.DEFINE);
			expect(Symbol.LEFT_PARENTHESIS);
				expect(Symbol.DOMAIN);
				name();
			expect(Symbol.RIGHT_PARENTHESIS);
			expect(Symbol.LEFT_PARENTHESIS);
				expect(Symbol.PREDICATES);
				AdvancedSet<Atom> domain = predicates();
			expect(Symbol.RIGHT_PARENTHESIS);
			List<Action> actions = actions();
		expect(Symbol.RIGHT_PARENTHESIS);
		
		expect(Symbol.LEFT_PARENTHESIS);
			expect(Symbol.DEFINE);
			expect(Symbol.LEFT_PARENTHESIS);
				expect(Symbol.PROBLEM);
				name();
			expect(Symbol.RIGHT_PARENTHESIS);
			expect(Symbol.LEFT_PARENTHESIS);
				expect(Symbol.DOMAIN_REF);
				name();
			expect(Symbol.RIGHT_PARENTHESIS);
			expect(Symbol.LEFT_PARENTHESIS);
				expect(Symbol.INIT);
				Init init = init();
			expect(Symbol.RIGHT_PARENTHESIS);
			expect(Symbol.LEFT_PARENTHESIS);
				expect(Symbol.GOAL);
				Formula goal = formula();
			expect(Symbol.RIGHT_PARENTHESIS);
		expect(Symbol.RIGHT_PARENTHESIS);
		
		return new Problem(domain, actions, init, goal);
	}
	
	private AdvancedSet<Atom> predicates() throws ParseException {
		AdvancedSet<Atom> atoms = new AdvancedSet<>();
		while(!end()) {
			atoms.add(atom());
		}
		if(atoms.isEmpty()) {
			throw new ParseException("empty list of predicates", tokens.size());
		}
		return atoms;
	}
	
	private Init init() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			if(afterNext().equals(Symbol.NONDETERMINISM)) {
				return oneOfInit();
			} else if(afterNext().equals(Symbol.CONJUNCTION)) {
				return conjunctiveInit();
			} else {
				throw new ParseException("expected '" + Symbol.NONDETERMINISM + "' or '" + Symbol.CONJUNCTION + "', found '" + next() + "'", tokens.size());
			}
		} else {
			return positiveLiteralInit();
		}
	}
	
	private OneOfInit oneOfInit() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.NONDETERMINISM);
		AdvancedSet<DeterministicInit> alternatives = new AdvancedSet<>();
		while(!end()) {
			alternatives.add(deterministicInit());
		}
		if(alternatives.isEmpty()) {
			throw new ParseException("empty non-deterministic init", tokens.size());
		}
		expect(Symbol.RIGHT_PARENTHESIS);
		return new OneOfInit(alternatives);
	}
	
	private DeterministicInit deterministicInit() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			return conjunctiveInit();
		} else {
			return positiveLiteralInit();
		}
	}
	
	private ConjunctiveInit conjunctiveInit() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.CONJUNCTION);
		AdvancedSet<PositiveLiteralInit> alternatives = new AdvancedSet<>();
		while(!end()) {
			alternatives.add(positiveLiteralInit());
		}
		if(alternatives.isEmpty()) {
			throw new ParseException("empty conjunctive init", tokens.size());
		}
		expect(Symbol.RIGHT_PARENTHESIS);
		return new ConjunctiveInit(alternatives);
	}
	
	private PositiveLiteralInit positiveLiteralInit() throws ParseException {
		return new PositiveLiteralInit(atom());
	}
	
	private List<Action> actions() throws ParseException {
		List<Action> actions = new ArrayList<>();
		while(!end()) {
			actions.add(action());
		}
		if(actions.isEmpty()) {
			throw new ParseException("empty list of actions", tokens.size());
		}
		return actions;
	}
	
	private Action action() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.ACTION);
		Name name = name();
		Formula formula = null;
		if(next().equals(Symbol.PRECONDITION)) {
			consume();
			formula = formula();
		}
		expect(Symbol.EFFECT);
		Effect effect = effect();
		expect(Symbol.RIGHT_PARENTHESIS);
		if(formula == null) {
			return new Action(name, effect);
		} else {
			return new Action(name, formula, effect);
		}
	}
	
	private Effect effect() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			if(afterNext().equals(Symbol.NONDETERMINISM)) {
				return oneOfEffect();
			} else if(afterNext().equals(Symbol.CONJUNCTION)) {
				return conjunctiveEffect();
			} else if(afterNext().equals(Symbol.CONDITIONAL)) {
				return conditionalTermEffect();
			} else if(afterNext().equals(Symbol.NEGATION)) {
				return negativeLiteralEffect();
			} else {
				throw new ParseException("expected '" + Symbol.NONDETERMINISM + "' or '" + Symbol.CONJUNCTION + "' or '" + Symbol.CONDITIONAL + "' or '" + Symbol.NEGATION + "', found '" + next() + "'", tokens.size());
			}
		} else {
			return deterministicEffect();
		}
	}
	
	private OneOfEffect oneOfEffect() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.NONDETERMINISM);
		AdvancedSet<DeterministicEffect> alternatives = new AdvancedSet<>();
		while(!end()) {
			alternatives.add(deterministicEffect());
		}
		if(alternatives.isEmpty()) {
			throw new ParseException("empty non-deterministic effect", tokens.size());
		}
		expect(Symbol.RIGHT_PARENTHESIS);
		return new OneOfEffect(alternatives);
	}
	
	private DeterministicEffect deterministicEffect() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			if(afterNext().equals(Symbol.CONJUNCTION)) {
				return conjunctiveEffect();
			} else if(afterNext().equals(Symbol.CONDITIONAL)) {
				return conditionalTermEffect();
			} else if(afterNext().equals(Symbol.NEGATION)) {
				return negativeLiteralEffect();
			} else {
				throw new ParseException("expected '" + Symbol.CONJUNCTION + "' or '" + Symbol.CONDITIONAL + "' or '" + Symbol.NEGATION + "', found '" + next() + "'", tokens.size());
			}
		} else {
			return termEffect();
		}
	}
	
	private ConjunctiveEffect conjunctiveEffect() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.CONJUNCTION);
		AdvancedSet<TermEffect> conjuncts = new AdvancedSet<>();
		while(!end()) {
			conjuncts.add(termEffect());
		}
		if(conjuncts.isEmpty()) {
			throw new ParseException("empty conjunctive effect", tokens.size());
		}
		expect(Symbol.RIGHT_PARENTHESIS);
		return new ConjunctiveEffect(conjuncts);
	}
	
	private TermEffect termEffect() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			if(afterNext().equals(Symbol.CONDITIONAL)) {
				return conditionalTermEffect();
			} else if(afterNext().equals(Symbol.NEGATION)) {
				return negativeLiteralEffect();
			} else {
				throw new ParseException("expected '" + Symbol.CONDITIONAL + "' or '" + Symbol.NEGATION + "', found '" + next() + "'", tokens.size());
			}
		} else {
			return unconditionalTermEffect();
		}
	}
	
	private ConditionalTermEffect conditionalTermEffect() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.CONDITIONAL);
		Formula formula = formula();
		UnconditionalTermEffect unconditionalTermEffect = unconditionalTermEffect();
		expect(Symbol.RIGHT_PARENTHESIS);
		return new ConditionalTermEffect(formula, unconditionalTermEffect);
	}
	
	private UnconditionalTermEffect unconditionalTermEffect() throws ParseException {
		if(Symbol.isValidConstantEffect(next())) {
			return constantEffect();
		} else {
			return literalEffect();
		}
	}
	
	private LiteralEffect literalEffect() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			return negativeLiteralEffect();
		} else {
			return new PositiveLiteralEffect(atom());
		}
	}
	
	private NegativeLiteralEffect negativeLiteralEffect() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.NEGATION);
		Atom atom = atom();
		expect(Symbol.RIGHT_PARENTHESIS);
		return new NegativeLiteralEffect(atom);
	}
	
	private ConstantEffect constantEffect() throws ParseException {
		expect(Symbol.NULL);
		return new NullEffect();
	}
	
	private Formula formula() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			if(afterNext().equals(Symbol.CONJUNCTION)) {
				return conjunctiveFormula();
			} else if(afterNext().equals(Symbol.NEGATION)) {
				return negativeLiteral();
			} else {
				throw new ParseException("expected '" + Symbol.CONJUNCTION + "' or '" + Symbol.NEGATION + "', found '" + next() + "'", tokens.size());
			}
		} else {
			return term();
		}
	}
	
	private ConjunctiveFormula conjunctiveFormula() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.CONJUNCTION);
		List<Term> conjuncts = new ArrayList<>();
		while(!end()) {
			conjuncts.add(term());
		}
		if(conjuncts.isEmpty()) {
			throw new ParseException("empty conjunctive formula", tokens.size());
		}
		expect(Symbol.RIGHT_PARENTHESIS);
		return new ConjunctiveFormula(conjuncts);
	}
	
	private Term term() throws ParseException {
		if(Symbol.isValidConstant(next())) {
			return constant();
		} else {
			return literal();
		}
	}
	
	private Literal literal() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			return negativeLiteral();
		} else {
			return new PositiveLiteral(atom());
		}
	}
	
	private NegativeLiteral negativeLiteral() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.NEGATION);
		Atom atom = atom();
		expect(Symbol.RIGHT_PARENTHESIS);
		return new NegativeLiteral(atom);
	}
	
	private Atom atom() throws ParseException {
		return new Atom(name().getLabel());
	}
	
	private Constant constant() throws ParseException {
		if(next().equals(Symbol.TRUE)) {
			consume();
			return new True();
		} else if(next().equals(Symbol.FALSE)) {
			consume();
			return new False();
		} else {
			throw new ParseException("expected '" + Symbol.TRUE + "' or '" + Symbol.FALSE + "', found '" + next() + "'", tokens.size());
		}
	}
	
	private Name name() throws ParseException {
		if(Name.isValid(next())) {
			Name name = new Name(next());
			consume();
			return name;
		} else {
			throw new ParseException("invalid name '" + next() + "'", tokens.size());
		}
	}
	
}
