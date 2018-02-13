package language;

public class Symbol {
	
	public static final String LEFT_PARENTHESIS = "(";
	public static final String RIGHT_PARENTHESIS = ")";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String NULL = "null";
	public static final String NEGATION = "not";
	public static final String CONJUNCTION = "and";
	public static final String CONDITIONAL = "when";
	public static final String NONDETERMINISM = "oneof";
	public static final String NOOB = "noob";
	public static final String NOOP = "noop";
	
	public static final String DEFINE = "define";
	public static final String DOMAIN = "domain";
	public static final String PROBLEM = "problem";
	
	public static final String PREDICATES = ":predicates";
	public static final String ACTION = ":action";
	public static final String PRECONDITION = ":precondition";
	public static final String EFFECT = ":effect";
	public static final String OBSERVATION = ":observation";
	public static final String DOMAIN_REF = ":" + DOMAIN;
	public static final String INIT = ":init";
	public static final String GOAL = ":goal";
	
	public static boolean isValid(String label) {
		return label.equals(LEFT_PARENTHESIS) 
				|| label.equals(RIGHT_PARENTHESIS) 
				|| label.equals(Symbol.TRUE) 
				|| label.equals(Symbol.FALSE) 
				|| label.equals(Symbol.NULL) 
				|| label.equals(Symbol.NEGATION) 
				|| label.equals(Symbol.CONJUNCTION) 
				|| label.equals(Symbol.CONDITIONAL) 
				|| label.equals(Symbol.NONDETERMINISM) 
				|| label.equals(Symbol.NOOB) 
				|| label.equals(Symbol.NOOP);
	}
	
	public static boolean isValidConstant(String label) {
		return label.equals(Symbol.TRUE) || label.equals(Symbol.FALSE);
	}
	
	public static boolean isValidConstantEffect(String label) {
		return label.equals(Symbol.NULL);
	}
	
	public static boolean isValidConstantObservation(String label) {
		return label.equals(Symbol.NOOB);
	}

}
