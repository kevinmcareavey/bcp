package debug;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import exception.DomainMismatchException;
import exception.EmptyBeliefStateException;
import exception.InconsistencyException;
import exception.InvalidNumException;
import exception.UnrecognisedEffect;
import exception.UnrecognisedObservation;
import language.Action;
import language.Atom;
import language.DomainProblem;
import language.Formula;
import language.Init;
import language.Name;
import language.Percept;
import language.effect.deterministic.ConjunctiveEffect;
import language.effect.deterministic.term.ConditionalTermEffect;
import language.effect.deterministic.term.unconditional.constant.NullEffect;
import language.effect.deterministic.term.unconditional.literal.NegativeLiteralEffect;
import language.effect.deterministic.term.unconditional.literal.PositiveLiteralEffect;
import language.effect.nondeterministic.OneOfEffect;
import language.formula.ConjunctiveFormula;
import language.formula.term.literal.NegativeLiteral;
import language.formula.term.literal.PositiveLiteral;
import language.init.deterministic.ConjunctiveInit;
import language.init.deterministic.PositiveLiteralInit;
import language.init.nondeterministic.OneOfInit;
import language.observation.deterministic.ConjunctiveObservation;
import language.observation.deterministic.term.ConditionalTermObservation;
import language.observation.deterministic.term.unconditional.literal.NegativeLiteralObservation;
import language.observation.deterministic.term.unconditional.literal.PositiveLiteralObservation;
import parser.Parser;
import planner.Plan;
import planner.num.FiniteNum;
import planner.num.InfiniteNum;
import planner.search.BAOStar;
import structure.AdvancedSet;

public class Test {
	
	public static Atom vl = new Atom("vacuum-left");
	public static Atom dl = new Atom("dirt-left");
	public static Atom dr = new Atom("dirt-right");
	
	private static AdvancedSet<Atom> predicates = new AdvancedSet<>(vl, dl, dr);
	
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
			new Name("suck"), 
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
			new Name("right"), 
			new NegativeLiteralEffect(vl)
	);
	public static Action left = new Action(
			new Name("left"), 
			new PositiveLiteralEffect(vl)
	);
	
	public static Percept sd = new Percept("sense-dirt");
	
	private static ConjunctiveFormula f5 = new ConjunctiveFormula(new PositiveLiteral(vl), new PositiveLiteral(dl));
	private static ConjunctiveFormula f6 = new ConjunctiveFormula(new PositiveLiteral(vl), new NegativeLiteral(dl));
	private static ConjunctiveFormula f7 = new ConjunctiveFormula(new NegativeLiteral(vl), new PositiveLiteral(dr));
	private static ConjunctiveFormula f8 = new ConjunctiveFormula(new NegativeLiteral(vl), new NegativeLiteral(dr));
	
	private static ConditionalTermObservation w9 = new ConditionalTermObservation(f5, new PositiveLiteralObservation(sd)); 
	private static ConditionalTermObservation w10 = new ConditionalTermObservation(f6, new NegativeLiteralObservation(sd)); 
	private static ConditionalTermObservation w11 = new ConditionalTermObservation(f7, new PositiveLiteralObservation(sd)); 
	private static ConditionalTermObservation w12 = new ConditionalTermObservation(f8, new NegativeLiteralObservation(sd));
	
	public static Action sense = new Action(
			new Name("sense"), 
			new ConjunctiveObservation(w9, w10, w11, w12)
	);
	
	private static List<Action> actions = Arrays.asList(suck, right, left, sense);
	
	public static void hardCoded() throws InconsistencyException, EmptyBeliefStateException, UnrecognisedEffect, UnrecognisedObservation, IOException, InterruptedException, DomainMismatchException, InvalidNumException {
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
		DomainProblem domainProblem = new DomainProblem(new Name(""), predicates, actions, new Name(""), init, goal);
		
		BAOStar search = new BAOStar(domainProblem, new FiniteNum(1));
		Plan plan = search.search();
		System.out.println(plan);
		if(plan != null) {
			plan.getDotGraph().writeToPdf(System.getProperty("user.home") + "/plan.pdf");
		}
	}
	
	public static void file() {
	try {
		Parser parser = new Parser();
		
//		Domain domain = parser.readOperator(System.getProperty("user.home") + "/Downloads/operator.cpddl");
//		Problem problem = parser.readFact(System.getProperty("user.home") + "/Downloads/fact.cpddl");
//		DomainProblem domainProblem = new DomainProblem(domain, problem);
//		System.out.println(domainProblem);
		
		DomainProblem domainProblem = parser.read(System.getProperty("user.home") + "/Downloads/combined.cpddl");
		System.out.println(domainProblem);
		
		BAOStar aosearch = new BAOStar(domainProblem, new InfiniteNum());
		Plan plan = aosearch.search();
		System.out.println(plan);
		if(plan != null) {
			plan.getDotGraph().writeToPdf(System.getProperty("user.home") + "/plan.pdf");
		}
	} catch (ParseException | IOException | DomainMismatchException | UnrecognisedEffect | InconsistencyException | UnrecognisedObservation | InterruptedException | InvalidNumException e) {
		e.printStackTrace();
	}
}
	
	public static void main(String[] args) {
		try {
			hardCoded();
//			file();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
