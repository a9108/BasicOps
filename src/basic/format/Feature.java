package basic.format;

public class Feature {
	private double[] value;
	private double result;

	public void setSize(int n) {
		value = new double[n];
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
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (double v : value)
			sb.append(v + "\t");
		return sb.toString();
	}
}
