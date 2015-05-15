package basic.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import basic.format.Feature;
import basic.format.Pair;

public class RandomTree extends Classification {

	private ArrayList<TreeNode> tree;
	private LinkedList<Integer> validFeatures;

	Random random;

	private int MAX_LEVEL;
	private int MIN_DATACNT;
	private double FEATURE_RATIO;

	public RandomTree(int max_level, int min_datacnt, double ratio,
			int nfeatures) {
		MAX_LEVEL = max_level;
		MIN_DATACNT = min_datacnt;
		FEATURE_RATIO = ratio;
		random = new Random();
		setNFeature(nfeatures);
	}

	private double mse(double s, double ss, int n) {
		return ss - s * s / n;
	}

	private Split findSplit(ArrayList<Feature> data) {
		Split res = null;
		double best = 1e100;
		for (final int id : validFeatures) {
			Collections.sort(data, new Comparator<Feature>() {
				@Override
				public int compare(Feature a, Feature b) {
					Double va = a.getValue(id);
					return va.compareTo(b.getValue(id));
				}
			});
			double TS = 0, TSS = 0;
			for (int i = 0; i < data.size(); i++) {
				TS += data.get(i).getResult();
				TSS += Math.pow(data.get(i).getResult(), 2);
			}

			double S = 0, SS = 0;
			for (int i = 0; i < data.size(); i++) {
				S += data.get(i).getResult();
				SS += Math.pow(data.get(i).getResult(), 2);
				if (i + 1 < MIN_DATACNT || data.size() - 1 - i < MIN_DATACNT)
					continue;
				if (i + 1 < data.size()
						&& data.get(i).getValue(id) == data.get(i + 1)
								.getValue(id))
					continue;
				double cur = mse(S, SS, i + 1)
						+ mse(TS - S, TSS - SS, data.size() - 1 - i);
				if (cur < best) {
					best = cur;
					res = new Split(id, (data.get(i).getValue(id) + data.get(
							i + 1).getValue(id)) / 2);
				}
			}
		}
		return res;
	}

	private int learn(ArrayList<Feature> data, int depth) {
		int id = tree.size();
		tree.add(new TreeNode());
		TreeNode cur = new TreeNode();
		cur.depth = depth;
		cur.cnt = data.size();
		cur.isleaf = true;
		cur.left = cur.right = -1;
		double s = 0;
		for (Feature item : data)
			s += item.getResult();
		s /= data.size();
		cur.result = s;
		// System.out.println(data.size()+"\t"+cur.result);

		Split cut = null;
		if (depth < MAX_LEVEL)
			cut = findSplit(data);
		if (cut != null) {
			ArrayList<Feature> dataleft = new ArrayList<Feature>();
			ArrayList<Feature> dataright = new ArrayList<Feature>();
			for (Feature item : data) {
				if (item.getValue(cut.getFirst()) < cut.getSecond())
					dataleft.add(item);
				else
					dataright.add(item);
			}
			cur.split = cut;
			// System.out.println(depth + "\t" + cur.result + "\t"
			// + dataleft.size() + "\t" + dataright.size() + "\t"
			// + cur.split);

			cur.left = learn(dataleft, depth + 1);
			cur.right = learn(dataright, depth + 1);
			cur.isleaf = false;
		}

		tree.set(id, cur);
		return id;
	}

	@Override
	public void train() {
		tree = new ArrayList<TreeNode>();
		validFeatures = new LinkedList<Integer>();
		for (int i = 0; i < NFeature; i++) {
			if (random.nextDouble() <= FEATURE_RATIO)
				validFeatures.add(i);
		}
		learn(new ArrayList<Feature>(train), 0);
	}

	@Override
	public double predict(Feature data) {
		for (int pos = 0;;) {
			TreeNode cur = tree.get(pos);
			// System.out.println(cur);
			if (cur.isleaf)
				return cur.result;
			if (data.getValue(cur.split.getFirst()) < cur.split.getSecond())
				pos = cur.left;
			else
				pos = cur.right;
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}

class TreeNode {
	public Split split;
	public int depth;
	public double result;
	public int cnt;
	public boolean isleaf;
	public int left, right;

	@Override
	public String toString() {
		return depth + "\t" + cnt + "\t" + result + "\t" + left + "\t" + right
				+ "\t" + split;
	}
}

class Split extends Pair<Integer, Double> {
	public Split(int id, double d) {
		super(id, d);
	}
}
