package main;

import java.util.HashMap;
import java.util.Map;

public class Plan {
	
	private Action action;
	private Map<Percept, Plan> subplans;
	
	public Plan() {
		this.setAction(Action.NOOP);
		this.setSubplans(null);
	}
	
	public Action getAction() {
		return this.action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Map<Percept, Plan> getSubplans() {
		return this.subplans;
	}

	public void setSubplans(Map<Percept, Plan> subplans) {
		this.subplans = subplans;
	}
	
	public void addSubplan(Percept percept, Plan plan) {
		if(this.getSubplans() == null) {
			this.setSubplans(new HashMap<Percept, Plan>());
		}
		this.getSubplans().put(percept, plan);
	}
	
	public int depth() {
		int maxDepth = 0;
		if(this.getSubplans() != null) {
			for(Map.Entry<Percept, Plan> entry : this.getSubplans().entrySet()) {
				Plan plan = entry.getValue();
				int childDepth = plan.depth();
				if(childDepth > maxDepth) {
					maxDepth = childDepth;
				}
			}
		}
		return maxDepth + 1;
	}
	
	public int nodes() {
		int sum = 0;
		if(this.getSubplans() != null) {
			for(Map.Entry<Percept, Plan> entry : this.getSubplans().entrySet()) {
				Plan plan = entry.getValue();
				sum += plan.nodes();
			}
		}
		return sum + 1;
	}
	
	public int leafs() {
		if(this.getSubplans() == null || this.getSubplans().isEmpty()) {
			return 1;
		} else {
			int sum = 0;
			if(this.getSubplans() != null) {
				for(Map.Entry<Percept, Plan> entry : this.getSubplans().entrySet()) {
					Plan plan = entry.getValue();
					sum += plan.leafs();
				}
			}
			return sum;
		}
	}

	@Override
	public String toString() {
		String output = "[" + action.getName();
		if(subplans != null) {
			output += ", ";
			String delim = "";
			for(Map.Entry<Percept, Plan> entry : this.getSubplans().entrySet()) {
				Percept percept = entry.getKey();
				Plan plan = entry.getValue();
				if(this.getSubplans().size() == 1) {
					output += delim + plan.toString().substring(1, plan.toString().length() - 1);
				} else {
					output += delim + "if " + percept + " then " + plan;
				}
				delim = ", ";
			}
		}
		output += "]";
		return output;
	}

}
