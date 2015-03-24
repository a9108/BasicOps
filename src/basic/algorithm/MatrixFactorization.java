package basic.algorithm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import basic.Functions;
import basic.Vector;
import basic.format.Pair;

public class MatrixFactorization {
	private int K;
	private int N, M;
	private double lambda, rate;
	private LinkedList<Pair<Pair<Integer, Integer>, Double>> tabel;
	private double[][] A, B;
	private double[] biasA, biasB;
	private Random random;

	public MatrixFactorization(int N, int M, int K, double lambda, double rate) {
		this.N = N;
		this.M = M;
		this.K = K;
		this.lambda = lambda;
		this.rate = rate;
		random = new Random();
		A = new double[N][K];
		B = new double[M][K];
		biasA = new double[N];
		biasB = new double[M];
		for (int i = 0; i < N; i++)
			for (int k = 0; k < K; k++)
				A[i][k] = (random.nextDouble() * 2 - 1) / Math.sqrt(K);
		for (int i = 0; i < M; i++)
			for (int k = 0; k < K; k++)
				B[i][k] = (random.nextDouble() * 2 - 1) / Math.sqrt(K);
		tabel = new LinkedList<Pair<Pair<Integer, Integer>, Double>>();
	}

	public void addTrain(int i, int j, double v) {
		tabel.add(new Pair<Pair<Integer, Integer>, Double>(
				new Pair<Integer, Integer>(i, j), v));
	}

	public double predict(int i, int j) {
		return Functions.sigmoid(biasA[i] + biasB[j] + Vector.dot(A[i], B[j]));
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
		return c + lambda * norm;
	}

	private void train(int i, int j, double v) {
		double y = predict(i, j);
		double err = y - v;
		double g = err * 2 * y * (1 - y);
		for (int k = 0; k < K; k++) {
			double Aik = A[i][k];
			A[i][k] -= rate * (g * B[j][k] + lambda * A[i][k]);
			B[j][k] -= rate * (g * Aik + lambda * B[j][k]);
		}
		biasA[i] -= rate * g;
		biasB[j] -= rate * g;
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
	public double[] getEmbedding_User(int i){
		return A[i];
	}
	public double[] getEmbedding_Item(int j){
		return B[j];
	}
}
