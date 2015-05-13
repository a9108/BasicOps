package basic;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import basic.format.Pair;

public class DataOps {
	public static double sum(List<Double> data) {
		double s = 0;
		for (Double i : data)
			s += i;
		return s;
	}

	public static <T extends Comparable<T>> T max(List<T> data) {
		T s=null;
		for (T i : data)
			if (s == null || s.compareTo(i) < 0)
				s = i;
		return s;
	}

	public static double sum(double[] data) {
		double s = 0;
		for (Double i : data)
			s += i;
		return s;
	}

	public static double average(List<Double> data) {
		return sum(data) / data.size();
	}

	public static <A extends Comparable<A>, B extends Comparable<B>> LinkedList<Pair<A, B>> dict2list_sorted(
			HashMap<A, B> data, Comparator<Pair<A, B>> cmp) {
		LinkedList<Pair<A, B>> list = dict2list(data);
		Collections.sort(list, cmp);
		return list;
	}

	public static <A extends Comparable<A>, B extends Comparable<B>> LinkedList<Pair<A, B>> selectTopN(
			HashMap<A, B> data, Comparator<Pair<A, B>> cmp, int N) {
		LinkedList<Pair<A, B>> list = dict2list_sorted(data, cmp);
		LinkedList<Pair<A, B>> res = new LinkedList<Pair<A, B>>();
		Iterator<Pair<A, B>> itr = list.iterator();
		for (int i = 0; i < N && itr.hasNext(); i++)
			res.add(itr.next());
		return res;
	}

	public static <A extends Comparable<A>, B extends Comparable<B>> LinkedList<Pair<A, B>> dict2list(
			HashMap<A, B> data) {
		LinkedList<Pair<A, B>> res = new LinkedList<Pair<A, B>>();
		for (A key : data.keySet())
			res.add(new Pair<A, B>(key, data.get(key)));
		return res;
	}

	public static double calcRMSE(List<Double> err) {
		double serr = 0;
		for (Double v : err)
			serr += Math.pow(v, 2);
		return Math.sqrt(serr / err.size());
	}
}
