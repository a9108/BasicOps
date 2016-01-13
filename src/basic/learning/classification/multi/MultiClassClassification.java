package basic.learning.classification.multi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import basic.format.Feature;

public abstract class MultiClassClassification {
	protected LinkedList<Feature> train = new LinkedList<Feature>();
	protected int NFeature;
	protected int nClass;

	public MultiClassClassification(int nClass, int nFeature) {
		this.nClass = nClass;
		setNFeature(nFeature);
	}

	public int getNFeature() {
		return NFeature;
	}

	public void setNFeature(int nFeature) {
		NFeature = nFeature;
	}

	public void addTrain(Feature feature) {
		train.add(feature);
	}

	public abstract void clear();

	public abstract void train();

	public abstract void destroy();

	public int predict(Feature data) {
		int id = 0;
		double[] pred = predictDetail(data);
		for (int t = 1; t < nClass; t++)
			if (pred[t] > pred[id])
				id = t;
		return id;
	}

	public abstract double[] predictDetail(Feature data);

	public ArrayList<Integer> predict(List<Feature> data) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (Feature f : data)
			res.add(predict(f));
		return res;
	}

	public ArrayList<double[]> predictDetail(List<Feature> data) {
		ArrayList<double[]> res = new ArrayList<double[]>();
		for (Feature f : data)
			res.add(predictDetail(f));
		return res;
	}
}
