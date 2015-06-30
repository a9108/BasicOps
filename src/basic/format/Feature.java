package basic.format;

import java.util.HashSet;
import java.util.Set;

public abstract class Feature {
	protected double result;
	protected int nFeature;

	public abstract void setSize(int n);

	public int size() {
		return nFeature;
	}

	public abstract void setValue(int i, double v);

	public abstract double getValue(int i);

	public double getResult() {
		return result;
	}

	public abstract Set<Integer> getIds();

	public void setResult(double result) {
		this.result = result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(result + "\t");
		for (Integer id : getIds())
			sb.append(id + ":" + getValue(id) + "\t");
		return sb.toString();
	}

	public abstract void append(Feature f);
}
