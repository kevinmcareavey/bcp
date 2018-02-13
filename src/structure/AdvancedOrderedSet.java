package structure;

import java.util.Collection;
import java.util.LinkedHashSet;

public class AdvancedOrderedSet<T> extends LinkedHashSet<T> {
	
	private static final long serialVersionUID = -653747226129910057L;

	public AdvancedOrderedSet() {
		super();
	}
	
	@SafeVarargs
	public AdvancedOrderedSet(T... inputs) {
		super();
		for(int i = 0; i < inputs.length; i++) {
			this.add(inputs[i]);
		}
	}
	
	public AdvancedOrderedSet(Collection<T> c) {
		super(c);
	}
	
	public boolean subsetOf(AdvancedOrderedSet<T> other) {
		return other.containsAll(this);
	}
	
	public boolean supersetOf(AdvancedOrderedSet<T> other) {
		return this.containsAll(other);
	}
	
	public boolean intersects(AdvancedOrderedSet<T> other) {
		if(!this.isEmpty() && !other.isEmpty()) {
			for(T t : other) {
				if(this.contains(t)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public AdvancedOrderedSet<T> setminus(AdvancedOrderedSet<T> other) {
		AdvancedOrderedSet<T> result = new AdvancedOrderedSet<T>();
		for(T element : this) {
			if(!other.contains(element)) {
				result.add(element);
			}
		}
		return result;
	}
	
	public AdvancedOrderedSet<T> copy() {
		AdvancedOrderedSet<T> copy = new AdvancedOrderedSet<T>(this);
		return copy;
	}
	
	@Override
	public String toString() {
		return super.toString().replace("[", "{").replace("]", "}");
	}

}
