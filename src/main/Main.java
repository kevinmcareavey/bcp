package main;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

import exception.DomainMismatchException;
import exception.InconsistencyException;
import exception.InvalidNumException;
import exception.UnrecognisedEffect;
import exception.UnrecognisedObservation;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import language.Domain;
import language.DomainProblem;
import language.Problem;
import language.Symbol;
import parser.Parser;
import planner.Num;
import planner.Plan;
import planner.num.FiniteNum;
import planner.num.InfiniteNum;
import planner.search.BAOStar;

public class Main {
	
	public static final String NAME = "bcp.jar";
	public static final String VERSION = "0.1.0";
	
	public static void main(String[] args) {
		OptionParser optionParser = new OptionParser();
		
		String[] pathOptions = {"p", "path"};
		optionParser.acceptsAll(Arrays.asList(pathOptions), "Path to combined operator and fact <FILE> in CPDDL").withRequiredArg().describedAs("FILE");
		
		String[] operatorOptions = {"o", "operator"};
		optionParser.acceptsAll(Arrays.asList(operatorOptions), "Path to operator (domain) <FILE> in CPDDL").requiredUnless("path").withRequiredArg().describedAs("FILE");
		
		String[] factOptions = {"f", "fact"};
		optionParser.acceptsAll(Arrays.asList(factOptions), "Path to fact (problem) <FILE> in CPDDL").requiredUnless("path").withRequiredArg().describedAs("FILE");
		
		String[] boundOptions = {"k", "bound"};
		optionParser.acceptsAll(Arrays.asList(boundOptions), "Value of k bound; <NUM> can be non-negative integer or 'infinity'").withRequiredArg().describedAs("NUM").defaultsTo("infinity");
		
		String[] helpOptions = {"h", "help"};
		optionParser.acceptsAll(Arrays.asList(helpOptions), "Display usage information and exit").forHelp();
		
		String[] versionOptions = {"v", "version"};
		optionParser.acceptsAll(Arrays.asList(versionOptions), "Display version information and exit").forHelp();
		
		OptionSet options = null;
		try {
			options = optionParser.parse(args);
		} catch(Exception e) {
			usage(optionParser);
			System.exit(1);
		}
		
		if(options.has("help")) {
			usage(optionParser);
			System.exit(0);
		}
		
		if(options.has("version")) {
			version();
			System.exit(0);
		}
		
		Num bound = new InfiniteNum();
		if(options.has("bound") && options.hasArgument("bound")) {
			String argument = (String) options.valueOf("bound");
			if(!argument.equals(Symbol.INFINITY)) {
				try {
					bound = new FiniteNum(Integer.valueOf((String) options.valueOf("bound")));
				} catch (InvalidNumException e) {
					usage(optionParser);
					System.exit(1);
				}
			}
		}
		
		Parser parser = new Parser();
		
		if(options.has("path") && options.hasArgument("path")) {
			if(options.has("operator") || options.has("fact")) {
				usage(optionParser);
				System.exit(1);
			}
			
			try {
				DomainProblem domainProblem = parser.read((String) options.valueOf("path"));
				run(domainProblem, bound);
			} catch (ParseException | IOException | DomainMismatchException | UnrecognisedEffect | InconsistencyException | UnrecognisedObservation | InterruptedException | InvalidNumException e) {
				e.printStackTrace();
			}
		}
		
		if(options.has("operator") && options.hasArgument("operator") && options.has("fact") && options.hasArgument("fact")) {
			if(options.has("path")) {
				usage(optionParser);
				System.exit(1);
			}
			
			try {
				Domain domain = parser.readOperator((String) options.valueOf("operator"));
				Problem problem = parser.readFact((String) options.valueOf("fact"));
				DomainProblem domainProblem = new DomainProblem(domain, problem);
				run(domainProblem, bound);
			} catch (ParseException | IOException | DomainMismatchException | UnrecognisedEffect | InconsistencyException | UnrecognisedObservation | InterruptedException | InvalidNumException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void run(DomainProblem domainProblem, Num bound) throws IOException, InterruptedException, UnrecognisedEffect, InconsistencyException, UnrecognisedObservation, InvalidNumException {
		BAOStar aosearch = new BAOStar(domainProblem, bound);
		Plan plan = aosearch.search();
		System.out.println(plan);
//		if(plan != null) {
//			plan.getDotGraph().writeToPdf(System.getProperty("user.home") + "/plan.pdf");
//		}
	}
	
	public static void usage(OptionParser optionParser) {
		System.out.println("Usage: java -jar " + NAME + " -o <FILE> -f <FILE> [-k <NUM>] | -p <FILE> [-k <NUM>]");
		System.out.println("Example: java -jar " + NAME + " -o domain.cpddl -f problem.cpddl -k 1");
		System.out.println();
		try {
			optionParser.printHelpOn(System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void version() {
		System.out.println(NAME + " " + VERSION);
	}

}
