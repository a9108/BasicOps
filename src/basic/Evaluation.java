package basic;

import java.util.Collections;
import java.util.LinkedList;

import basic.format.Pair;

public class Evaluation {
	public static double calcAUC(LinkedList<Pair<Double, Boolean>> pred) {
		Collections.sort(pred);
		Collections.reverse(pred);
		Pair<Double, Double> la = new Pair<Double, Double>(0., 0.);
		double res = 0;
		int cnt = 0, hit = 0;
		int totP = 0;
		for (Pair<Double, Boolean> item : pred)
			if (item.getSecond())
				totP++;
		for (Pair<Double, Boolean> item : pred) {
			cnt++;
			if (item.getSecond())
				hit++;
			double tp = hit / (totP + 0.0);
			double fp = (cnt - hit) / (pred.size() - totP + 0.0);
			res += (la.getFirst() + tp) / 2 * (fp - la.getSecond());
			la = new Pair<Double, Double>(tp, fp);
		}
		return res;
	}
}
