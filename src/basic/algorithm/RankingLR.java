package basic.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import basic.Config;
import basic.Functions;
import basic.RandomOps;
import basic.format.Feature;

public class RankingLR extends Classification {

	private double var;
	private double[] w, ma, mi;
	private double rate;
	private int TrainNum;

	private Random random = new Random();

	public RankingLR(int nFeature, int TrainNum, double rate, double var) {
		setNFeature(nFeature);
		this.var = var;
		this.TrainNum = TrainNum;
		this.rate = rate;
	}

	private void normalize() {
		ma = new double[NFeature];
		mi = new double[NFeature];
		for (int i = 0; i < NFeature; i++)
			mi[i] = 1e10;
		for (int i = 0; i < NFeature; i++) {
			LinkedList<Double> cur = new LinkedList<Double>();
			for (Feature f : train)
				if (f.getIds().contains(i))
					cur.add(f.getValue(i));
			if (cur.size() == 0) {
				ma[i] = 1;
				mi[i] = 0;
			} else {
				Collections.sort(cur);
				ma[i] = cur.get(cur.size() - 1 - cur.size() * 1 / 1000);
				// mi[i] = cur.get(cur.size() * 1 / 1000);
				// ma[i] = cur.get(cur.size()-1);
				// mi[i] = cur.get(0);
				mi[i] = 0;
			}
			ma[i] = Math.max(ma[i], mi[i] + 1e-1);
		}
		// for (Feature f : train)
		// for (int id : f.getIds())
		// ma[id] = Math.max(ma[id], Math.abs(f.getValue(id)));
		for (Feature f : train) {
			for (int id : f.getIds())
				f.setValue(id, (f.getValue(id) - mi[id]) / (ma[id] - mi[id]));
		}

		int cnt = 0;
		for (Feature f : train) {
			f.setSize(NFeature * Config.getInt("LRDegree"));
			HashSet<Integer> ids = new HashSet<Integer>(f.getIds());
			for (int i = 1; i < Config.getInt("LRDegree"); i++)
				for (int id : ids)
					f.setValue(id + i * NFeature,
							Math.pow(f.getValue(id), i + 1));
			if (cnt < 10) {
				System.out.println(f.toString());
				cnt++;
			}
		}
		NFeature *= Config.getInt("LRDegree");
	}

	ArrayList<Feature> pos, neg;
	int[] cntF;

	private void train(Feature f, Feature nf) {
		double p = predict(f, nf);
		double g = 1 - p;

		HashSet<Integer> ids = new HashSet<Integer>(f.getIds());
		ids.addAll(nf.getIds());
		for (int q : ids)
			w[q] += rate
					* ((f.getValue(q) - nf.getValue(q)) * g - w[q] / cntF[q]
							/ var);
	}

	@Override
	public void train() {
		normalize();

		pos = new ArrayList<Feature>();
		neg = new ArrayList<Feature>();
		for (Feature f : train)
			if (f.getResult() == 1)
				pos.add(f);
			else
				neg.add(f);

		w = new double[NFeature];
		for (int i = 0; i < NFeature; i++)
			w[i] = RandomOps.genNormal(0, 1. / NFeature);

		double lacost = getCost();
		cntF = new int[NFeature];
		for (Feature f : train)
			for (int q : f.getIds())
				cntF[q]++;

		for (int r = 0; r < TrainNum && rate > 1e-8; r++) {
			// ArrayList<Pair<Integer, Double>> negid = new
			// ArrayList<Pair<Integer, Double>>();
			// double sum = 0;
			// for (int i = 0; i < neg.size(); i++)
			// negid.add(new Pair<Integer, Double>(i, sum += predict(neg
			// .get(i))));
			// for (int tn = 0; tn < NUMNEG; tn++) {
			// Collections.shuffle(pos);
			//
			// for (Feature f : pos) {
			// int nid = random.nextInt(neg.size());
			// nid = RandomOps.weightedSelection(negid, random, sum);
			// Feature nf = neg.get(nid);
			// train(f, nf);
			// }
			Collections.shuffle(neg);
			for (Feature nf : neg)
				train(pos.get(random.nextInt(pos.size())), nf);
			for (Feature f : pos)
				train(f, neg.get(random.nextInt(neg.size())));

			// Collections.shuffle(pos);
			// for (Feature f:pos)
			// train(f,neg.get(random.nextInt(neg.size())));

			if (Config.getBoolean("AdaptiveRate")) {
				double cost = getCost();

				if (cost < lacost) {
					rate *= 1.1;
				} else {
					rate /= 2;
				}
				lacost = cost;
			}

			// System.out.println("Linear Regression Cost = " + cost
			// + " , Rate = " + rate);
		}
		for (int i = 0; i < NFeature; i++)
			System.out.println(w[i]);
	}

	private double getCost() {
		double c = 0;
		for (Feature f : pos) {
			Feature nf = neg.get(random.nextInt(neg.size()));
			double p = predict(f, nf);
			c += Math.log(p);
		}
		for (Feature nf : neg) {
			Feature f = pos.get(random.nextInt(pos.size()));
			double p = predict(f, nf);
			c += Math.log(p);
		}
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
		double res = 0;
		for (int i : data.getIds())
			res += w[i] * (data.getValue(i) - mi[i]) / (ma[i] - mi[i]);
		return Functions.sigmoid(res);
	}

	public double predict(Feature pos, Feature neg) {
		double res = 0;
		HashSet<Integer> ids = new HashSet<Integer>(pos.getIds());
		ids.addAll(neg.getIds());
		for (int i : ids)
			res += w[i] * (pos.getValue(i) - neg.getValue(i));
		return Functions.sigmoid(res);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
