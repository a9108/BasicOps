package basic.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import basic.Config;
import basic.DataOps;
import basic.Functions;
import basic.RandomOps;
import basic.format.Feature;
import basic.format.Pair;

public class RankingLR extends Classification {

	private double var;
	private double[] w, ma;
	private double rate;
	private int TrainNum;
	private int SampleSize = 1;

	private Random random = new Random();

	public RankingLR(int nFeature, int TrainNum, double rate, double var) {
		setNFeature(nFeature);
		this.var = var;
		this.TrainNum = TrainNum;
		this.rate = rate;
	}

	private void normalize() {
		ma = new double[NFeature];
		for (int i = 0; i < NFeature; i++) {
			LinkedList<Double> cur = new LinkedList<Double>();
			for (Feature f : train)
				if (f.getIds().contains(i))
					cur.add(f.getValue(i));
			if (cur.size() == 0)
				ma[i] = 1;
			else {
				Collections.sort(cur);
				ma[i] = cur.get(cur.size() - 1 - cur.size() * 1 / 1000);
			}
			// System.out.println(cur.size() + "\t" + ma[i]);
			ma[i] = Math.max(ma[i], 1e-1);
			// if (cur.size() < 10000)
			// ma[i] = 1e100;
			// ma[i] = 0.1;
		}
		// for (Feature f : train)
		// for (int id : f.getIds())
		// ma[id] = Math.max(ma[id], Math.abs(f.getValue(id)));
		for (Feature f : train) {
			for (int id : f.getIds())
				f.setValue(id, f.getValue(id) / ma[id]);
		}
	}

	ArrayList<Feature> pos, neg;

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
		int[] cnt = new int[NFeature];
		for (Feature f : train)
			for (int q : f.getIds())
				cnt[q]++;
		
		int NUMNEG=Config.getInt("NegSampleSize");
		
		
		for (int r = 0; r < TrainNum && rate > 1e-8; r++) {
			ArrayList<Pair<Integer, Double>> negid=new ArrayList<Pair<Integer,Double>>();
			double sum=0;
			for (int i=0;i<neg.size();i++)
				negid.add(new Pair<Integer, Double>(i,sum+=predict(neg.get(i))));
	for (int z = 0; z < SampleSize; z++) {
				Collections.shuffle(pos);
//				for (int tn = 0; tn < NUMNEG; tn++) 
				for (Feature f : pos) {
						int nid=random.nextInt(neg.size());
						nid=RandomOps.weightedSelection(negid, random,sum);
						Feature nf = neg.get(nid);
						double p = predict(f, nf);
						double g = 1 - p;

						HashSet<Integer> ids = new HashSet<Integer>(f.getIds());
						ids.addAll(nf.getIds());
						for (int q : ids)
							w[q] += rate
									* ((f.getValue(q) - nf.getValue(q)) * g - w[q]
											* train.size()
											/ (pos.size() * cnt[q]) / var);
					}
			}

			double cost = getCost();

//			if (cost < lacost) {
//				rate *= 1.1;
//			} else {
//				rate /= 2;
//			}
//			lacost = cost;

			System.out.println("Linear Regression Cost = " + cost
					+ " , Rate = " + rate);
		}
		for (int i = 0; i < NFeature; i++)
			System.out.println(w[i]);
	}

	private double getCost() {
		double c = 0;
		for (Feature f : pos)
			for (int q = 0; q < SampleSize; q++) {
				Feature nf = neg.get(random.nextInt(neg.size()));
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
			res += w[i] * data.getValue(i) / ma[i];
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

}
