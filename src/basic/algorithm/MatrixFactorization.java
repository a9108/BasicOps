package basic.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import basic.Functions;
import basic.Vector;
import basic.format.Pair;

public class MatrixFactorization {
	private int K;
	private int NF;
	private int N, M;
	private double lambda, rate;
	private LinkedList<Pair<Pair<Integer, Integer>, Double>> tabel;
	private double[][] A, B, F;
	private double[] biasA, biasB, biasF;
	private double bias;
	private ArrayList<LinkedList<Pair<Integer, Double>>> userF, itemF;
	private Random random;

	private int type;

	public static int BASIC = 1, SIGMOID = 2;

	public MatrixFactorization(int N, int M, int K, int NF, double lambda,
			double rate, int type) {
		this.N = N;
		this.M = M;
		this.K = K;
		this.NF = NF;
		this.lambda = lambda;
		this.rate = rate;
		this.type = type;
		random = new Random();
		A = new double[N][K];
		B = new double[M][K];
		F = new double[NF][K];

		bias = 0;
		biasA = new double[N];
		biasB = new double[M];
		biasF = new double[NF];
		userF = new ArrayList<LinkedList<Pair<Integer, Double>>>();
		for (int i = 0; i < N; i++)
			userF.add(new LinkedList<Pair<Integer, Double>>());
		itemF = new ArrayList<LinkedList<Pair<Integer, Double>>>();
		for (int i = 0; i < M; i++)
			itemF.add(new LinkedList<Pair<Integer, Double>>());
		for (int i = 0; i < N; i++)
			for (int k = 0; k < K; k++)
				A[i][k] = (random.nextDouble() * 2 - 1) / Math.sqrt(K);
		for (int i = 0; i < M; i++)
			for (int k = 0; k < K; k++)
				B[i][k] = (random.nextDouble() * 2 - 1) / Math.sqrt(K);
		for (int i = 0; i < NF; i++)
			for (int k = 0; k < K; k++)
				F[i][k] = (random.nextDouble() * 2 - 1) / Math.sqrt(K);
		tabel = new LinkedList<Pair<Pair<Integer, Integer>, Double>>();
	}

	public void addUserFeature(int uid, int fid, double value) {
		userF.get(uid).add(new Pair<Integer, Double>(fid, value));
	}

	public void addItemFeature(int iid, int fid, double value) {
		itemF.get(iid).add(new Pair<Integer, Double>(fid, value));
	}

	public void addTrain(int i, int j, double v) {
		tabel.add(new Pair<Pair<Integer, Integer>, Double>(
				new Pair<Integer, Integer>(i, j), v));
	}

	public double predict(int i, int j) {
		double bias = this.bias + biasA[i] + biasB[j];
		for (Pair<Integer, Double> f : userF.get(i)) {
			int id = f.getFirst();
			double weight = f.getSecond();
			bias += weight * biasF[id];
		}
		for (Pair<Integer, Double> f : itemF.get(j)) {
			int id = f.getFirst();
			double weight = f.getSecond();
			bias += weight * biasF[id];
		}
		double value = bias
				+ Vector.dot(getEmbedding_User(i), getEmbedding_Item(j));
//		if (true) return bias;
		if (type == BASIC)
			return value;
		if (type == SIGMOID)
			return Functions.sigmoid(value);
		return 0;
	}

	public double getUserBias(int i) {
		return biasA[i];
	}

	public double getCost() {
		double c = 0;
		for (Pair<Pair<Integer, Integer>, Double> entry : tabel)
			c += Math.pow(
					predict(entry.getFirst().getFirst(), entry.getFirst()
							.getSecond())
							- entry.getSecond(), 2);
		double norm = 0;
		for (int i = 0; i < N; i++)
			for (int k = 0; k < K; k++)
				norm += Math.pow(A[i][k], 2);
		for (int i = 0; i < M; i++)
			for (int k = 0; k < K; k++)
				norm += Math.pow(B[i][k], 2);
		for (int i = 0; i < NF; i++)
			for (int k = 0; k < K; k++)
				norm += Math.pow(F[i][k], 2);
		return c + lambda * norm;
	}

	private void train(int i, int j, double v) {
		double y = predict(i, j);
		double err = y - v;
		double g = 0;
		if (type == BASIC)
			g = 2 * err;
		if (type == SIGMOID)
			g = 2 * err * y * (1 - y);
		
		double[] user = getEmbedding_User(i), item = getEmbedding_Item(j);
		for (int k = 0; k < K; k++) {
			A[i][k] -= rate * (g * item[k] + lambda * A[i][k]);
			B[j][k] -= rate * (g * user[k] + lambda * B[j][k]);
		}
		for (Pair<Integer, Double> f : userF.get(i)) {
			int id = f.getFirst();
			double weight = f.getSecond();
			biasF[id] -= rate * g * weight;
			for (int k = 0; k < K; k++)
				F[id][k] -= rate * weight * (g * item[k] + lambda * F[id][k]);
		}
		biasA[i] -= rate * g;
		biasB[j] -= rate * g;
		bias -= rate * g;
	}

	private void train() {
		Collections.shuffle(tabel);
		for (Pair<Pair<Integer, Integer>, Double> entry : tabel)
			train(entry.getFirst().getFirst(), entry.getFirst().getSecond(),
					entry.getSecond());
	}

	public void train(int ROUND, boolean silent) {
		double lacost = getCost();
		if (!silent)
			System.out.println("Initial Cost = " + lacost);
		for (int i = 0; i < ROUND; i++) {
			train();
			double cur = getCost();
			if (cur < lacost)
				rate *= 1.2;
			else
				rate /= 2;
			lacost = cur;
			if (!silent)
				System.out.println("Round " + i + "\tCost = " + lacost
						+ "\tRate = " + rate);
		}
	}

	public double[] getEmbedding_User(int i) {
		double[] embed = new double[K];
		for (int k = 0; k < K; k++)
			embed[k] = A[i][k];
		for (Pair<Integer, Double> f : userF.get(i)) {
			int id = f.getFirst();
			double weight = f.getSecond();
			for (int k = 0; k < K; k++)
				embed[k] += weight * F[id][k];
		}
		return embed;
	}

	public double[] getEmbedding_Item(int j) {
		double[] embed = new double[K];
		for (int k = 0; k < K; k++)
			embed[k] = B[j][k];
		for (Pair<Integer, Double> f : itemF.get(j)) {
			int id = f.getFirst();
			double weight = f.getSecond();
			for (int k = 0; k < K; k++)
				embed[k] += weight * F[id][k];
		}
		return embed;
	}
}
