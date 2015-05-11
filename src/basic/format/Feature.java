package basic.format;

import java.util.HashSet;
import java.util.Set;

public abstract class Feature {
	private double result;

	public abstract void setSize(int n);

	public abstract int size();

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
		for (Integer id : getIds())
			sb.append(id + ":" + getValue(id));
		return sb.toString();
	}

}
