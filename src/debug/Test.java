package debug;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exceptions.EmptyBeliefStateException;
import exceptions.InconsistencyException;
import main.Action;
import main.AdvancedSet;
import main.AndOrSearch;
import main.Atom;
import main.BeliefState;
import main.Formula;
import main.Init;
import main.Plan;
import main.Problem;
import main.State;
import main.effect.deterministic.ConjunctiveEffect;
import main.effect.deterministic.term.ConditionalTermEffect;
import main.effect.deterministic.term.unconditional.constant.NullEffect;
import main.effect.deterministic.term.unconditional.literal.NegativeLiteralEffect;
import main.effect.deterministic.term.unconditional.literal.PositiveLiteralEffect;
import main.effect.nondeterministic.OneOfEffect;
import main.formula.ConjunctiveFormula;
import main.formula.term.literal.NegativeLiteral;
import main.formula.term.literal.PositiveLiteral;
import main.init.deterministic.ConjunctiveInit;
import main.init.deterministic.PositiveLiteralInit;
import main.init.nondeterministic.OneOfInit;
import parser.Parser;

public class Test {
	
	public static Atom vl = new Atom("vacuum-left");
	public static Atom dl = new Atom("dirt-left");
	public static Atom dr = new Atom("dirt-right");
	
	private static AdvancedSet<Atom> domain = new AdvancedSet<>(vl, dl, dr);
	
	private static State s1 = new State(vl, dl, dr);
	private static State s2 = new State(dl, dr);
	private static State s3 = new State(vl, dl);
	private static State s4 = new State(dl);
	private static State s5 = new State(vl, dr);
	private static State s6 = new State(dr);
	private static State s7 = new State(vl);
	private static State s8 = new State();
	
	private static AdvancedSet<State> states = new AdvancedSet<State>(s1, s2, s3, s4, s5, s6, s7, s8);
	
	private static ConjunctiveFormula f1 = new ConjunctiveFormula(new PositiveLiteral(vl), new PositiveLiteral(dl));
	private static ConjunctiveFormula f2 = new ConjunctiveFormula(new NegativeLiteral(vl), new PositiveLiteral(dr));
	private static ConjunctiveFormula f3 = new ConjunctiveFormula(new PositiveLiteral(vl), new NegativeLiteral(dl));
	private static ConjunctiveFormula f4 = new ConjunctiveFormula(new NegativeLiteral(vl), new NegativeLiteral(dr));
	
	private static ConditionalTermEffect w1 = new ConditionalTermEffect(f1, new NegativeLiteralEffect(dl));
	private static ConditionalTermEffect w2 = new ConditionalTermEffect(f1, new NegativeLiteralEffect(dr));
	private static ConditionalTermEffect w3 = new ConditionalTermEffect(f2, new NegativeLiteralEffect(dr));
	private static ConditionalTermEffect w4 = new ConditionalTermEffect(f2, new NegativeLiteralEffect(dl));
	private static ConditionalTermEffect w5 = new ConditionalTermEffect(f3, new NullEffect());
	private static ConditionalTermEffect w6 = new ConditionalTermEffect(f3, new PositiveLiteralEffect(dl));
	private static ConditionalTermEffect w7 = new ConditionalTermEffect(f4, new NullEffect());
	private static ConditionalTermEffect w8 = new ConditionalTermEffect(f4, new PositiveLiteralEffect(dr));
	
	public static Action suck = new Action(
			"suck", 
			new OneOfEffect(
					new ConjunctiveEffect(w1, w3, w5, w7),
					new ConjunctiveEffect(w1, w2, w3, w5, w7),
					new ConjunctiveEffect(w1, w3, w4, w5, w7),
					new ConjunctiveEffect(w1, w2, w3, w4, w5, w7),
					new ConjunctiveEffect(w1, w3, w6, w7),
					new ConjunctiveEffect(w1, w2, w3, w6, w7),
					new ConjunctiveEffect(w1, w3, w4, w6, w7),
					new ConjunctiveEffect(w1, w2, w3, w4, w6, w7),
					new ConjunctiveEffect(w1, w3, w5, w8),
					new ConjunctiveEffect(w1, w2, w3, w5, w8),
					new ConjunctiveEffect(w1, w3, w4, w5, w8),
					new ConjunctiveEffect(w1, w2, w3, w4, w5, w8),
					new ConjunctiveEffect(w1, w3, w6, w8),
					new ConjunctiveEffect(w1, w2, w3, w6, w8),
					new ConjunctiveEffect(w1, w3, w4, w6, w8),
					new ConjunctiveEffect(w1, w2, w3, w4, w6, w8)
			)
	);
	public static Action right = new Action(
			"right", 
			new PositiveLiteral(vl), 
			new NegativeLiteralEffect(vl)
	);
	public static Action left = new Action(
			"left", 
			new NegativeLiteral(vl), 
			new PositiveLiteralEffect(vl)
	);
	
	private static List<Action> actions = Arrays.asList(suck, right, left);
	
	public static void single() throws EmptyBeliefStateException, InconsistencyException {
		Init init = new OneOfInit(
				new ConjunctiveInit(
						new PositiveLiteralInit(vl), 
						new PositiveLiteralInit(dl), 
						new PositiveLiteralInit(dr)
				), 
				new ConjunctiveInit(
						new PositiveLiteralInit(dl), 
						new PositiveLiteralInit(dr)
				)
		);
		Formula goal = new ConjunctiveFormula(new NegativeLiteral(dl), new NegativeLiteral(dr));
		Problem problem = new Problem(domain, actions, init, goal);
		
		AndOrSearch aosearch = new AndOrSearch();
		Plan plan = aosearch.search(problem);
		
		System.out.println("Intial belief state: " + problem.getInitialBeliefState());
		System.out.println("               Goal: " + problem.getGoal());
		System.out.println("               Plan: " + plan);
	}
	
//	public static void all() throws EmptyBeliefStateException, InconsistencyException {
//		AdvancedSet<AdvancedSet<State>> subsets = states.powerset();
//		subsets.remove(new AdvancedSet<State>());
//		for(AdvancedSet<State> subset : subsets) {
//			BeliefState initialBeliefState = new BeliefState();
//			initialBeliefState.addAll(subset);
//			Formula goal = new ConjunctiveFormula(new NegativeLiteral(dl), new NegativeLiteral(dr));
//			Problem problem = new Problem(initialBeliefState, actions, goal);
//			
//			AndOrSearch aosearch = new AndOrSearch();
//			Plan plan = aosearch.search(problem);
//			
//			if(plan == null) {
//				System.exit(1);
//			}
//			
//			System.out.println(initialBeliefState + " -> " + plan);
//		}
//	}
	
	public static void formulas() throws ParseException, EmptyBeliefStateException {
		Parser parser = new Parser();
		List<String> tests = new ArrayList<>();
		tests.add("true");
		tests.add(" true");
		tests.add("true ");
		tests.add(" true ");
		
		tests.add("false");
		
		tests.add("atom");
		tests.add(" atom");
		tests.add("atom ");
		tests.add(" atom ");
		
		tests.add("(not atom)");
		tests.add("(not atom )");
		tests.add("( not atom )");
		tests.add(" (not atom)");
		tests.add("(not atom) ");
		tests.add(" (not atom) ");
		
		tests.add("(and a1 a2 a3)");
		tests.add("(and (not a1) a2 a3)");
		tests.add("(and a1 (not a2) a3)");
		tests.add("(and a1 a2 (not a3))");
		
		int indent = maxLength(tests);
		for(String test : tests) {
			System.out.println(Test.pad("'" + test + "'", indent + 2) + " = " + parser.parse(test));
		}
	}
	
	public static void effects() throws ParseException, EmptyBeliefStateException {
		Parser parser = new Parser();
		List<String> tests = new ArrayList<>();
		tests.add("null");
		tests.add(" null");
		tests.add("null ");
		tests.add(" null ");
		
		tests.add("atom");
		tests.add(" atom");
		tests.add("atom ");
		tests.add(" atom ");
		
		tests.add("(not atom)");
		tests.add("(not atom )");
		tests.add("( not atom )");
		tests.add(" (not atom)");
		tests.add("(not atom) ");
		tests.add(" (not atom) ");
		
		tests.add("(when a1 a2)");
		tests.add("(when (not a1) a2)");
		tests.add("(when a1 (not a2))");
		tests.add("(when (not a1) (not a2))");
		tests.add("(when (and a1 a2) a3)");
		
		tests.add("(and a1 a2)");
		tests.add("(and a1 (not a2))");
		tests.add("(and a1 (when a2 a3))");
		
		int indent = maxLength(tests);
		for(String test : tests) {
			System.out.println(Test.pad("'" + test + "'", indent + 2) + " = " + parser.parse(test));
		}
	}
	
	public static void actions() throws ParseException, EmptyBeliefStateException {
		Parser parser = new Parser();
		List<String> tests = new ArrayList<>();
		tests.add("(:action a1 :precondition true :effect null)");
		tests.add("(:action a1 :precondition false :effect null)");
		tests.add("(:action a1 :precondition p1 :effect null)");
		tests.add("(:action a1 :precondition (not p1) :effect null)");
		tests.add("(:action a1 :precondition (and p1 p2) :effect null)");
		tests.add("(:action a1 :precondition (and p1 (not p2)) :effect null)");
		tests.add("(:action a1 :precondition (and (not p1) p2) :effect null)");
		tests.add("(:action a1 :precondition (and (not p1) (not p2)) :effect null)");
		
		tests.add("(:action a1 :precondition true :effect q1)");
		tests.add("(:action a1 :precondition false :effect q1)");
		tests.add("(:action a1 :precondition p1 :effect q1)");
		tests.add("(:action a1 :precondition (not p1) :effect q1)");
		tests.add("(:action a1 :precondition (and p1 p2) :effect q1)");
		tests.add("(:action a1 :precondition (and p1 (not p2)) :effect q1)");
		tests.add("(:action a1 :precondition (and (not p1) p2) :effect q1)");
		tests.add("(:action a1 :precondition (and (not p1) (not p2)) :effect q1)");
		
		tests.add("(:action a1 :precondition true :effect (not q1))");
		tests.add("(:action a1 :precondition false :effect (not q1))");
		tests.add("(:action a1 :precondition p1 :effect (not q1))");
		tests.add("(:action a1 :precondition (not p1) :effect (not q1))");
		tests.add("(:action a1 :precondition (and p1 p2) :effect (not q1))");
		tests.add("(:action a1 :precondition (and p1 (not p2)) :effect (not q1))");
		tests.add("(:action a1 :precondition (and (not p1) p2) :effect (not q1))");
		tests.add("(:action a1 :precondition (and (not p1) (not p2)) :effect (not q1))");
		
		tests.add("(:action a1 :precondition true :effect (when p3 q1))");
		tests.add("(:action a1 :precondition false :effect (when p3 q1))");
		tests.add("(:action a1 :precondition p1 :effect (when p3 q1))");
		tests.add("(:action a1 :precondition (not p1) :effect (when p3 q1))");
		tests.add("(:action a1 :precondition (and p1 p2) :effect (when p3 q1))");
		tests.add("(:action a1 :precondition (and p1 (not p2)) :effect (when p3 q1))");
		tests.add("(:action a1 :precondition (and (not p1) p2) :effect (when p3 q1))");
		tests.add("(:action a1 :precondition (and (not p1) (not p2)) :effect (when p3 q1))");
		
		tests.add("(:action a1 :precondition true :effect (and q1 q2))");
		tests.add("(:action a1 :precondition false :effect (and q1 q2))");
		tests.add("(:action a1 :precondition p1 :effect (and q1 q2))");
		tests.add("(:action a1 :precondition (not p1) :effect (and q1 q2))");
		tests.add("(:action a1 :precondition (and p1 p2) :effect (and q1 q2))");
		tests.add("(:action a1 :precondition (and p1 (not p2)) :effect (and q1 q2))");
		tests.add("(:action a1 :precondition (and (not p1) p2) :effect (and q1 q2))");
		tests.add("(:action a1 :precondition (and (not p1) (not p2)) :effect (and q1 q2))");
		
		int indent = maxLength(tests);
		for(String test : tests) {
			System.out.println(Test.pad("'" + test + "'", indent + 2) + " = " + parser.parse(test));
		}
	}
	
	public static void problem() throws IOException, ParseException, EmptyBeliefStateException, InconsistencyException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/kevin/Downloads/vacuum-world.nd-pddl"));
		String input = "";
		for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
			input += line;
		}
		bufferedReader.close();
		
		Parser parser = new Parser();
		Problem problem = parser.parse(input);
		
		AndOrSearch aosearch = new AndOrSearch();
		Plan plan = aosearch.search(problem);
		
		System.out.println("Predicates: " + problem.getPredicates());
		System.out.println("   Actions: " + problem.getActions());
		System.out.println("      Init: " + problem.getInit());
		System.out.println("      Goal: " + problem.getGoal());
		System.out.println("      Plan: " + plan);
	}
	
	public static int maxLength(List<String> tests) {
		int indent = 0;
		for(String test : tests) {
			int length = test.length();
			if(length > indent) {
				indent = length;
			}
		}
		return indent;
	}
	
	public static String pad(String input, int indent) {
		int padding = indent - input.length();
		if(padding > 0) {
			return new String(new char[padding]).replace("\0", " ") + input;
		} else {
			return input;
		}
	}
	
	public static void main(String[] args) {
		try {
//			single();
//			all();
//			formulas();
//			effects();
//			actions();
			problem();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
