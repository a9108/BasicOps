package basic.format;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SparseFeature extends Feature {

	public SparseFeature() {
	}
	public SparseFeature(Feature curF) {
		setSize(curF.size());
		setResult(curF.getResult());
		for (int id : curF.getIds())
			setValue(id, curF.getValue(id));
	}

	@Override
	public void setSize(int n) {
		nFeature = n;
	}

	HashMap<Integer, Double> values = new HashMap<Integer, Double>();

	@Override
	public void setValue(int i, double v) {
		values.put(i, v);
	}

	@Override
	public double getValue(int i) {
		if (values.containsKey(i))
			return values.get(i);
		return 0;
	}

	@Override
	public Set<Integer> getIds() {
		return values.keySet();
	}

	@Override
	public void append(Feature f) {
		for (int id : f.getIds())
			setValue(id + size(), f.getValue(id));
		setSize(nFeature + f.size());
	}

}
