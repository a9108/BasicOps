package basic.format;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SparseFeature extends Feature{

	private int N;
	@Override
	public void setSize(int n) {
		this.N=n;
	}

	@Override
	public int size() {
		return N;
	}
	
	HashMap<Integer, Double> values;

	@Override
	public void setValue(int i, double v) {
		values.put(i, v);
	}

	@Override
	public double getValue(int i) {
		if (values.containsKey(i)) return values.get(i);
		return 0;
	}

	@Override
	public Set<Integer> getIds() {
		return values.keySet(); 
	}

}
