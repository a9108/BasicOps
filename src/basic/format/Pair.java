package basic.format;

import java.util.Comparator;

public class Pair<A extends Comparable<A>, B extends Comparable<B>> implements Comparable<Pair<A, B>>{
	private A first;
	private B second;

	public Pair() {
	}

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		return first;
	}

	public void setFirst(A first) {
		this.first = first;
	}

	public B getSecond() {
		return second;
	}

	public void setSecond(B second) {
		this.second = second;
	}

	@Override
	public String toString() {
		return first.toString() + "\t" + second.toString();
	}

	@Override
	public int compareTo(Pair<A, B> o) {
		if (first.equals(o.getFirst()))
			return second.compareTo(o.getSecond());
		else return first.compareTo(o.getFirst());
	}
}
