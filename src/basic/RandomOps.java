package basic;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.Random;

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
}
