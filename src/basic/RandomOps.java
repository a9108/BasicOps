package basic;

import java.util.ArrayList;
import java.util.Random;

import basic.format.Pair;

public class RandomOps {
	private static Random random = new Random();

	public static void setSeed(int s) {
		random = new Random(s);
	}

	public static double genNormal(double mean, double var) {
		return random.nextGaussian() * Math.sqrt(var) + mean;
	}

	public static ArrayList<Double> genNormalVector(int len, double mean,
			double var) {
		ArrayList<Double> res = new ArrayList<Double>();
		for (int i = 0; i < len; i++)
			res.add(genNormal(mean, var));
		return res;
	}

	public static double[] genNormal(int len, double mean, double var) {
		double[] res = new double[len];
		for (int i = 0; i < len; i++)
			res[i] = genNormal(mean, var);
		return res;
	}

	public static int weightedSelection(ArrayList<Pair<Integer, Double>> data,
			Random random,double sum) {
		int l = 0, r = data.size();
		double pos = random.nextDouble()*sum;
		for (; l < r - 1;) {
			int m = (l + r) / 2;
			if (data.get(m).getSecond() < pos)
				l = m;
			else
				r = m;
		}
		return l;
	}
}

