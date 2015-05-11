package basic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Vector {
	public static double dot(double[] a, double[] b) {
		double res = 0;
		for (int i = 0; i < a.length; i++)
			res += a[i] * b[i];
		return res;
	}

	public static double dot(List<Double> a, List<Double> b) {
		double res = 0;
		for (int i = 0; i < a.size(); i++)
			res += a.get(i) * b.get(i);
		return res;
	}

	public static double dist(List<Double> a, List<Double> b) {
		double res = 0;
		for (int i = 0; i < a.size(); i++)
			res += Math.pow(a.get(i) - b.get(i), 2);
		return Math.sqrt(res);
	}

	public static double dist(double[] a, double[] b) {
		double res = 0;
		for (int i = 0; i < a.length; i++)
			res += Math.pow(a[i] - b[i], 2);
		return Math.sqrt(res);
	}

	public static double norm(double[] a) {
		double res = 0;
		for (int i = 0; i < a.length; i++)
			res += a[i] * a[i];
		return Math.sqrt(res);
	}

	public static double norm(List<Double> a) {
		double res = 0;
		for (int i = 0; i < a.size(); i++)
			res += a.get(i) * a.get(i);
		return Math.sqrt(res);
	}

	public static double CosineSimilarity(double[] a, double[] b) {
		return dot(a, b) / norm(a) / norm(b);
	}

	public static double CosineSimilarity(List<Double> a, List<Double> b) {
		return dot(a, b) / norm(a) / norm(b);
	}

	public static ArrayList<Double> load(String s) {
		String[] sep = s.split("\t");
		ArrayList<Double> res = new ArrayList<Double>();
		for (String ts : sep)
			try {
				res.add(Double.valueOf(ts));
			} catch (Exception e) {
			}
		return res;
	}
}
