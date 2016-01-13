package basic.algorithm;

import java.util.Collections;

import basic.Functions;
import basic.RandomOps;
import basic.format.Feature;

public class LinearRegression extends Classification {

	private double var;
	private double[] w, ma, mi;
	private double bias = 0;
	private double rate, initrate;
	private int TrainNum;

	public LinearRegression(int nFeature, int TrainNum, double rate, double var) {
		setNFeature(nFeature);
		this.var = var;
		this.TrainNum = TrainNum;
		this.rate = rate;
		initrate = rate;
	}

	private void normalize() {
		ma = new double[NFeature];
		mi = new double[NFeature];
		boolean[] vi = new boolean[NFeature];
		for (int i = 0; i < NFeature; i++) {
			ma[i] = 0;
			mi[i] = 0;
			vi[i] = false;
		}
		for (Feature f : train)
			for (int id : f.getIds()) {
				if (!vi[id]) {
					vi[id] = true;
					ma[id] = mi[id] = f.getValue(id);
				}
				ma[id] = Math.max(ma[id], f.getValue(id));
				mi[id] = Math.min(mi[id], f.getValue(id));
			}
		for (int i = 0; i < NFeature; i++)
			ma[i] = Math.max(ma[i], mi[i] + 0.1);
		int cnt = 0;
		for (Feature f : train) {
			for (int id : f.getIds())
				f.setValue(id, (f.getValue(id) - mi[id]) / (ma[id] - mi[id]));
			if (cnt < 10) {
//				System.out.println(f.toString());
				cnt++;
			}
		}
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

//			System.out.println("Linear Regression Cost = " + cost
//					+ " , Rate = " + rate);
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
			res += w[i] * (data.getValue(i) - mi[i]) / (ma[i] - mi[i]);
		return Functions.sigmoid(res);
	}

	private double predictLocal(Feature data) {
		double res = bias;
		for (int i : data.getIds())
			res += w[i] * data.getValue(i);
		return Functions.sigmoid(res);
	}

	@Override
	public void clear() {
		train.clear();
		rate = initrate;
	}

}
