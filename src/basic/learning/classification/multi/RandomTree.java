package basic.learning.classification.multi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

import basic.format.Feature;
import basic.format.Pair;

public class RandomTree extends MultiClassClassification {

	private ArrayList<TreeNode> tree;
	private LinkedList<Integer> validFeatures;

	Random random;

	private int MAX_LEVEL;
	private int MIN_DATACNT;
	private double FEATURE_RATIO;

	public RandomTree(int nClass, int nFeatures, int max_level,
			int min_datacnt, double ratio) {
		super(nClass, nFeatures);
		MAX_LEVEL = max_level;
		MIN_DATACNT = min_datacnt;
		FEATURE_RATIO = ratio;
		random = new Random();
	}

	private double entropy(int[] cnt) {
		double s = 0;
		for (int c : cnt)
			s += c;
		double e = 0;
		for (int c : cnt)
			if (c > 0) {
				double p = c / s;
				e -= p * Math.log(p);
			}
		return e;
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
			int[] nright = new int[nClass];
			for (Feature f : data)
				nright[(int) f.getResult()]++;

			int[] nleft = new int[nClass];
			for (int i = 0; i < data.size(); i++) {
				nleft[(int) data.get(i).getResult()]++;
				nright[(int) data.get(i).getResult()]--;
//				System.out.println(i + "\t" + data.get(i).getValue(0));
				if (i + 1 < MIN_DATACNT || data.size() - 1 - i < MIN_DATACNT)
					continue;
				if (i + 1 < data.size()
						&& data.get(i).getValue(id) == data.get(i + 1)
								.getValue(id))
					continue;
				double cur = entropy(nleft) * (i + 1) + entropy(nright)
						* (data.size() - i - 1);
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
		double[] s = new double[nClass];

		for (Feature item : data)
			s[(int) item.getResult()]++;
		boolean finish = false;
		for (int i = 0; i < nClass; i++) {
			if (s[i] > data.size() - 0.1)
				finish = true;
			s[i] /= data.size();
		}
		cur.result = s;

		Split cut = null;
		if (!finish && depth < MAX_LEVEL)
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
	public void destroy() {
	}

	@Override
	public void clear() {
	}

	@Override
	public double[] predictDetail(Feature data) {
		for (int pos = 0;;) {
			TreeNode cur = tree.get(pos);
			if (cur.isleaf)
				return cur.result;
			if (data.getValue(cur.split.getFirst()) < cur.split.getSecond())
				pos = cur.left;
			else
				pos = cur.right;
		}
	}
}

class TreeNode {
	public Split split;
	public int depth;
	public double[] result;
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
