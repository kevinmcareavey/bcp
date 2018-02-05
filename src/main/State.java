package main;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class State extends AdvancedSet<Atom> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7580079134151466752L;
	
	private static final Map<State, String> STATES;
    static {
        Atom vl = new Atom("vacuum-left");
		Atom dl = new Atom("dirt-left");
		Atom dr = new Atom("dirt-right");
		
		State s1 = new State(vl, dl, dr);
		State s2 = new State(dl, dr);
		State s3 = new State(vl, dl);
		State s4 = new State(dl);
		State s5 = new State(vl, dr);
		State s6 = new State(dr);
		State s7 = new State(vl);
		State s8 = new State();
		
		Map<State, String> states = new HashMap<>();
		states.put(s1, "s1");
		states.put(s2, "s2");
		states.put(s3, "s3");
		states.put(s4, "s4");
		states.put(s5, "s5");
		states.put(s6, "s6");
		states.put(s7, "s7");
		states.put(s8, "s8");
        STATES = Collections.unmodifiableMap(states);
    }
	
	public State(Atom... atoms) {
		super(atoms);
	}

	@Override
	public String toString() {
		return STATES.get(this);
	}

}
