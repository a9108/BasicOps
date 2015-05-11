package basic.algorithm;

import java.util.Collections;
import java.util.Random;

import javafx.scene.shape.TriangleMesh;
import edu.stanford.nlp.ling.CoreAnnotations.NormalizedNamedEntityTagAnnotation;
import basic.DataOps;
import basic.Functions;
import basic.RandomOps;
import basic.format.Feature;

public class LinearRegression extends Classification {

	private double var;
	private double[] w, ma;
	private double bias = 0;
	private double rate;
	private int TrainNum;

	public LinearRegression(int nFeature, int TrainNum, double rate, double var) {
		setNFeature(nFeature);
		this.var = var;
		this.TrainNum = TrainNum;
		this.rate = rate;
	}

	private void normalize() {
		ma = new double[NFeature];
		for (int i = 0; i < NFeature; i++)
			ma[i] = 0.1;
		for (Feature f : train)
			for (int id : f.getIds())
				ma[id] = Math.max(ma[id], Math.abs(f.getValue(id)));
		for (Feature f : train)
			for (int id : f.getIds())
				f.setValue(id, f.getValue(id) / ma[id]);
	}

	@Override
	public void train() {
		normalize();

		w = new double[NFeature];
		for (int i = 0; i < NFeature; i++)
			w[i] = RandomOps.genNormal(0, 1. / NFeature);

		double lacost = getCost();
		int[] cnt = new int[NFeature];
		for (Feature f : train)
			for (int q : f.getIds())
				cnt[q]++;
		double[] law = new double[NFeature];
		for (int r = 0; r < TrainNum && rate > 1e-8; r++) {
			for (int q = 0; q < NFeature; q++)
				law[q] = w[q];
			Collections.shuffle(train);
			for (Feature f : train) {
				double p = predictLocal(f);
				// double err = f.getResult() - p;
				double g = f.getResult() - p;

				for (int q : f.getIds())
					w[q] += rate * (f.getValue(q) * g - w[q] / cnt[q] / var);
				bias += g * rate;
			}

			double cost = getCost();

			if (cost < lacost) {
				rate *= 1.1;
				lacost = cost;
			} else {
				for (int q = 0; q < NFeature; q++)
					w[q] = law[q];
				rate /= 2;
			}

			System.out.println("Linear Regression Cost = " + cost
					+ " , Rate = " + rate);
		}
		System.out.println(bias);
		for (int i = 0; i < NFeature; i++)
			System.out.println(w[i]);
	}

	private double getCost() {
		double c = 0;
		for (Feature f : train)
			if (f.getResult() == 1)
				c += Math.log(predictLocal(f));
			else
				c += Math.log(1 - predictLocal(f));
		double norm = 0;
		for (int i = 0; i < NFeature; i++)
			norm += w[i] * w[i];
		return -c + norm / var;
	}

	@Override
	public void destroy() {
	}

	@Override
	public double predict(Feature data) {
		double res = bias;
		for (int i : data.getIds())
			res += w[i] * data.getValue(i) / ma[i];
		return Functions.sigmoid(res);
	}

	private double predictLocal(Feature data) {
		double res = bias;
		for (int i : data.getIds())
			res += w[i] * data.getValue(i);
		return Functions.sigmoid(res);
	}

}
