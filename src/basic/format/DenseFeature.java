package basic.format;

import java.util.HashSet;

public class DenseFeature extends Feature {
	private double[] value;
	private double result;
	private HashSet<Integer> ids;

	public void setSize(int n) {
		value = new double[n];
		ids = new HashSet<Integer>();
		for (int i = 0; i < n; i++)
			ids.add(i);
	}

	public int size() {
		return value.length;
	}

	public void setValue(int i, double v) {
		value[i] = v;
	}

	public double getValue(int i) {
		return value[i];
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	@Override
	public HashSet<Integer> getIds() {
		return ids;
	}
}
