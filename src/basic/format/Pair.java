package basic.format;

import java.util.HashSet;

public class Pair<A extends Comparable<A>, B extends Comparable<B>> implements
		Comparable<Pair<A, B>> {
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
		return first.toString() + "," + second.toString();
	}

	@Override
	public int compareTo(Pair<A, B> o) {
		if (first.equals(o.getFirst()))
			return second.compareTo(o.getSecond());
		else
			return first.compareTo(o.getFirst());
	}
	@Override
	public boolean equals(Object obj) {
		return first.equals(((Pair<A, B>)(obj)).first)&&second.equals(((Pair<A, B>)(obj)).second);
	}

	@Override
	public int hashCode() {
		return (first.hashCode() << 10) + second.hashCode();
	}

	public static void main(String[] args) {
		HashSet<Pair<Integer, Integer>> t = new HashSet<Pair<Integer, Integer>>();
		;
		t.add(new Pair<Integer, Integer>(0, 123));
		t.add(new Pair<Integer, Integer>(0, 123));
		System.out.println(t.size());
	}
}
