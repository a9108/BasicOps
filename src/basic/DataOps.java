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
		T s = null;
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
			HashMap<A, B> data) {
		LinkedList<Pair<A, B>> list = dict2list(data);
		Collections.sort(list, new Comparator<Pair<A, B>>() {
			@Override
			public int compare(Pair<A, B> o1, Pair<A, B> o2) {
				return -o1.getSecond().compareTo(o2.getSecond());
			}
		});
		return list;
	}

	public static <A extends Comparable<A>, B extends Comparable<B>> LinkedList<Pair<A, B>> selectTopN(
			HashMap<A, B> data, int N) {
		LinkedList<Pair<A, B>> list = dict2list_sorted(data);
		LinkedList<Pair<A, B>> res = new LinkedList<Pair<A, B>>();
		Iterator<Pair<A, B>> itr = list.iterator();
		for (int i = 0; i < N && itr.hasNext(); i++)
			res.add(itr.next());
		return res;
	}

	public static <A extends Comparable<A>, B extends Comparable<B>> LinkedList<A> selectTopNItem(
			HashMap<A, B> data, int N) {
		LinkedList<Pair<A, B>> list = dict2list_sorted(data);
		LinkedList<A> res = new LinkedList<A>();
		Iterator<Pair<A, B>> itr = list.iterator();
		for (int i = 0; i < N && itr.hasNext(); i++)
			res.add(itr.next().getFirst());
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

	public static HashMap<Integer, Double> selectTopN_Map(
			HashMap<Integer, Double> values, int N) {
		HashMap<Integer, Double> res = new HashMap<Integer, Double>();
		for (Pair<Integer, Double> item : selectTopN(values, N))
			res.put(item.getFirst(), item.getSecond());
		return res;
	}

	public static double variance(List<Double> res) {
		double avg = average(res);
		double s = 0;
		for (double t : res)
			s += (t - avg) * (t - avg);
		return Math.sqrt(s / (res.size() - 1));
	}
}
