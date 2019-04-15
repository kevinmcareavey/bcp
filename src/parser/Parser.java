package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import exception.DomainMismatchException;
import language.Action;
import language.Atom;
import language.Domain;
import language.DomainProblem;
import language.Effect;
import language.Formula;
import language.Init;
import language.Name;
import language.Observation;
import language.Percept;
import language.Problem;
import language.Symbol;
import language.effect.DeterministicEffect;
import language.effect.deterministic.ConjunctiveEffect;
import language.effect.deterministic.TermEffect;
import language.effect.deterministic.term.ConditionalTermEffect;
import language.effect.deterministic.term.UnconditionalTermEffect;
import language.effect.deterministic.term.unconditional.ConstantEffect;
import language.effect.deterministic.term.unconditional.LiteralEffect;
import language.effect.deterministic.term.unconditional.constant.NullEffect;
import language.effect.deterministic.term.unconditional.literal.NegativeLiteralEffect;
import language.effect.deterministic.term.unconditional.literal.PositiveLiteralEffect;
import language.effect.nondeterministic.OneOfEffect;
import language.formula.ConjunctiveFormula;
import language.formula.Term;
import language.formula.term.Constant;
import language.formula.term.Literal;
import language.formula.term.constant.False;
import language.formula.term.constant.True;
import language.formula.term.literal.NegativeLiteral;
import language.formula.term.literal.PositiveLiteral;
import language.init.DeterministicInit;
import language.init.deterministic.ConjunctiveInit;
import language.init.deterministic.PositiveLiteralInit;
import language.init.nondeterministic.OneOfInit;
import language.observation.DeterministicObservation;
import language.observation.deterministic.ConjunctiveObservation;
import language.observation.deterministic.TermObservation;
import language.observation.deterministic.term.ConditionalTermObservation;
import language.observation.deterministic.term.UnconditionalTermObservation;
import language.observation.deterministic.term.unconditional.ConstantObservation;
import language.observation.deterministic.term.unconditional.LiteralObservation;
import language.observation.deterministic.term.unconditional.constant.NoObservation;
import language.observation.deterministic.term.unconditional.literal.NegativeLiteralObservation;
import language.observation.deterministic.term.unconditional.literal.PositiveLiteralObservation;
import language.observation.nondeterministic.OneOfObservation;
import structure.AdvancedSet;

public class Parser {
	
	private Deque<String> tokens;
	
	public DomainProblem read(String combined) throws ParseException, IOException, DomainMismatchException {
		Parser parser = new Parser();
		return parser.parse(this.dump(combined));
	}
	
	public Domain readOperator(String operator) throws ParseException, IOException {
		Parser parser = new Parser();
		return parser.parseDomain(this.dump(operator));
	}
	
	public Problem readFact(String fact) throws ParseException, IOException {
		Parser parser = new Parser();
		return parser.parseProblem(this.dump(fact));
	}
	
	public String dump(String file) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String input = "";
		for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
			input += line;
		}
		bufferedReader.close();
		return input;
	}
	
	public DomainProblem parse(String s) throws ParseException, DomainMismatchException {
		tokens = tokenize(s);
		
		DomainProblem t = domainProblem();
		
		if(!tokens.isEmpty()) {
			throw new ParseException("unprocessed tokens remain after parsing", tokens.size());
		}
		
		return t;
	}
	
	public Domain parseDomain(String s) throws ParseException {
		tokens = tokenize(s);
		
		Domain t = domain();
		
		if(!tokens.isEmpty()) {
			throw new ParseException("unprocessed tokens remain after parsing", tokens.size());
		}
		
		return t;
	}
	
	public Problem parseProblem(String s) throws ParseException {
		tokens = tokenize(s);
		
		Problem t = problem();
		
		if(!tokens.isEmpty()) {
			throw new ParseException("unprocessed tokens remain after parsing", tokens.size());
		}
		
		return t;
	}
	
	private Deque<String> tokenize(String s) {
		Deque<String> input = new ArrayDeque<String>();
		
		s = s.replace(Symbol.LEFT_PARENTHESIS, " " + Symbol.LEFT_PARENTHESIS + " ");
		s = s.replace(Symbol.RIGHT_PARENTHESIS, " " + Symbol.RIGHT_PARENTHESIS + " ");
		
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
	
	private DomainProblem domainProblem() throws ParseException, DomainMismatchException {
		return new DomainProblem(domain(), problem());
	}
	
	private Domain domain() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
			expect(Symbol.DEFINE);
			expect(Symbol.LEFT_PARENTHESIS);
				expect(Symbol.DOMAIN);
				Name domainName = name();
			expect(Symbol.RIGHT_PARENTHESIS);
			expect(Symbol.LEFT_PARENTHESIS);
				expect(Symbol.PREDICATES);
				AdvancedSet<Atom> predicates = predicates();
			expect(Symbol.RIGHT_PARENTHESIS);
			List<Action> actions = actions();
		expect(Symbol.RIGHT_PARENTHESIS);
		return new Domain(domainName, predicates, actions);
	}
	
	private Problem problem() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
			expect(Symbol.DEFINE);
			expect(Symbol.LEFT_PARENTHESIS);
				expect(Symbol.PROBLEM);
				Name problemName = name();
			expect(Symbol.RIGHT_PARENTHESIS);
			expect(Symbol.LEFT_PARENTHESIS);
				expect(Symbol.DOMAIN_REF);
				Name domainName = name();
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
		return new Problem(problemName, domainName, init, goal);
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
		Effect effect = null;
		if(next().equals(Symbol.EFFECT)) {
			consume();
			effect = effect();
		}
		Observation observation = null;
		if(effect == null) {
			expect(Symbol.OBSERVATION);
			observation = observation();
		} else if(next().equals(Symbol.OBSERVATION)) {
			consume();
			observation = observation();
		}
		expect(Symbol.RIGHT_PARENTHESIS);
		if(formula == null) {
			formula = new True();
		}
		if(effect == null) {
			effect = new NullEffect();
		}
		if(observation == null) {
			observation = new NoObservation();
		}
		return new Action(name, formula, effect, observation);
	}
	
	private Observation observation() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			if(afterNext().equals(Symbol.NONDETERMINISM)) {
				return oneOfObservation();
			} else if(afterNext().equals(Symbol.CONJUNCTION)) {
				return conjunctiveObservation();
			} else if(afterNext().equals(Symbol.CONDITIONAL)) {
				return conditionalTermObservation();
			} else if(afterNext().equals(Symbol.NEGATION)) {
				return negativeLiteralObservation();
			} else {
				throw new ParseException("expected '" + Symbol.NONDETERMINISM + "' or '" + Symbol.CONJUNCTION + "' or '" + Symbol.CONDITIONAL + "' or '" + Symbol.NEGATION + "', found '" + next() + "'", tokens.size());
			}
		} else {
			return deterministicObservation();
		}
	}
	
	private OneOfObservation oneOfObservation() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.NONDETERMINISM);
		AdvancedSet<DeterministicObservation> alternatives = new AdvancedSet<>();
		while(!end()) {
			alternatives.add(deterministicObservation());
		}
		if(alternatives.isEmpty()) {
			throw new ParseException("empty non-deterministic observation", tokens.size());
		}
		expect(Symbol.RIGHT_PARENTHESIS);
		return new OneOfObservation(alternatives);
	}
	
	private DeterministicObservation deterministicObservation() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			if(afterNext().equals(Symbol.CONJUNCTION)) {
				return conjunctiveObservation();
			} else if(afterNext().equals(Symbol.CONDITIONAL)) {
				return conditionalTermObservation();
			} else if(afterNext().equals(Symbol.NEGATION)) {
				return negativeLiteralObservation();
			} else {
				throw new ParseException("expected '" + Symbol.CONJUNCTION + "' or '" + Symbol.CONDITIONAL + "' or '" + Symbol.NEGATION + "', found '" + next() + "'", tokens.size());
			}
		} else {
			return termObservation();
		}
	}
	
	private ConjunctiveObservation conjunctiveObservation() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.CONJUNCTION);
		AdvancedSet<TermObservation> conjuncts = new AdvancedSet<>();
		while(!end()) {
			conjuncts.add(termObservation());
		}
		if(conjuncts.isEmpty()) {
			throw new ParseException("empty conjunctive observation", tokens.size());
		}
		expect(Symbol.RIGHT_PARENTHESIS);
		return new ConjunctiveObservation(conjuncts);
	}
	
	private TermObservation termObservation() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			if(afterNext().equals(Symbol.CONDITIONAL)) {
				return conditionalTermObservation();
			} else if(afterNext().equals(Symbol.NEGATION)) {
				return negativeLiteralObservation();
			} else {
				throw new ParseException("expected '" + Symbol.CONDITIONAL + "' or '" + Symbol.NEGATION + "', found '" + next() + "'", tokens.size());
			}
		} else {
			return unconditionalTermObservation();
		}
	}
	
	private ConditionalTermObservation conditionalTermObservation() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.CONDITIONAL);
		Formula formula = formula();
		UnconditionalTermObservation unconditionalTermObservation = unconditionalTermObservation();
		expect(Symbol.RIGHT_PARENTHESIS);
		return new ConditionalTermObservation(formula, unconditionalTermObservation);
	}
	
	private UnconditionalTermObservation unconditionalTermObservation() throws ParseException {
		if(Symbol.isValidConstantObservation(next())) {
			return constantObservation();
		} else {
			return literalObservation();
		}
	}
	
	private LiteralObservation literalObservation() throws ParseException {
		if(next().equals(Symbol.LEFT_PARENTHESIS)) {
			return negativeLiteralObservation();
		} else {
			return new PositiveLiteralObservation(percept());
		}
	}
	
	private NegativeLiteralObservation negativeLiteralObservation() throws ParseException {
		expect(Symbol.LEFT_PARENTHESIS);
		expect(Symbol.NEGATION);
		Percept percept = percept();
		expect(Symbol.RIGHT_PARENTHESIS);
		return new NegativeLiteralObservation(percept);
	}
	
	private ConstantObservation constantObservation() throws ParseException {
		expect(Symbol.NOOB);
		return new NoObservation();
	}
	
	private Percept percept() throws ParseException {
		return new Percept(name().getLabel());
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
