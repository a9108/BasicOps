package basic.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import basic.FileOps;
import basic.Functions;
import basic.Vector;
import basic.format.Pair;

public class NonNegativeMatrixFactorization {
	private int NThread=1;
	private int K;
	private int NF;
	private int N, M;
	private double lambda, lambdaF, rate;
	private boolean globalBias = true, userBias = true, itemBias = true;

	public void setNThread(int nThread) {
		NThread = nThread;
	}
	public void setGlobalBias(boolean globalBias) {
		this.globalBias = globalBias;
	}

	public void setUserBias(boolean userBias) {
		this.userBias = userBias;
	}

	public void setItemBias(boolean itemBias) {
		this.itemBias = itemBias;
	}

	private LinkedList<Pair<Pair<Integer, Integer>, Double>> table;
	private double[][] A, B, F;
	private double[] biasA, biasB, biasF;
	private double bias;
	private ArrayList<LinkedList<Pair<Integer, Double>>> userF, itemF;
	private Random random;
	private int[] cntU, cntI;

	private int type;

	public static int BASIC = 1, SIGMOID = 2;

	public NonNegativeMatrixFactorization(int N, int M, int K, int NF, double lambda,
			double lambdaF, double rate, int type) {
		this.N = N;
		this.M = M;
		this.K = K;
		this.NF = NF;
		this.lambda = lambda;
		this.lambdaF = lambdaF;
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
				A[i][k] = (random.nextDouble()) / Math.sqrt(K);
		for (int i = 0; i < M; i++)
			for (int k = 0; k < K; k++)
				B[i][k] = (random.nextDouble()) / Math.sqrt(K);
		for (int i = 0; i < NF; i++)
			for (int k = 0; k < K; k++)
				F[i][k] = (random.nextDouble()) / Math.sqrt(K);
		
		table = new LinkedList<Pair<Pair<Integer, Integer>, Double>>();
		cntU = new int[N];
		cntI = new int[M];
	}

	public void addUserFeature(int uid, int fid, double value) {
		userF.get(uid).add(new Pair<Integer, Double>(fid, value));
	}

	public void addItemFeature(int iid, int fid, double value) {
		itemF.get(iid).add(new Pair<Integer, Double>(fid, value));
	}

	public void addTrain(int i, int j, double v) {
		cntU[i]++;
		cntI[j]++;
		table.add(new Pair<Pair<Integer, Integer>, Double>(
				new Pair<Integer, Integer>(i, j), v));
	}

	public double predict(int i, int j) {
		double bias = 0;
		if (globalBias)
			bias += this.bias;
		if (userBias)
			bias += getUserBias(i);
		if (itemBias)
			bias+=getItemBias(j);
		double value = bias
				+ Vector.dot(getEmbedding_User(i), getEmbedding_Item(j));
		if (type == BASIC)
			return value;
		if (type == SIGMOID)
			return Functions.sigmoid(value);
		return 0;
	}

	public double getUserBias(int i) {
		double bias=biasA[i];
		for (Pair<Integer, Double> f : userF.get(i)) {
			int id = f.getFirst();
			double weight = f.getSecond();
			bias += weight * biasF[id];
		}
		return bias;
	}
	
	public double getItemBias(int j){
		double bias = biasB[j];
		for (Pair<Integer, Double> f : itemF.get(j)) {
			int id = f.getFirst();
			double weight = f.getSecond();
			bias += weight * biasF[id];
		}
		return bias;
	}

	public double getCost() {
		double c = 0;
		for (Pair<Pair<Integer, Integer>, Double> entry : table) {
			if (type == BASIC) {
				c += Math.pow(
						predict(entry.getFirst().getFirst(), entry.getFirst()
								.getSecond() == -1 ? random.nextInt(M) : entry
								.getFirst().getSecond())
								- entry.getSecond(), 2);
			} else if (type == SIGMOID) {
				if (entry.getSecond() == 1)
					c -= Math.log(predict(entry.getFirst().getFirst(), entry
							.getFirst().getSecond() == -1 ? random.nextInt(M)
							: entry.getFirst().getSecond()));
				else
					c -= Math
							.log(1 - predict(
									entry.getFirst().getFirst(),
									entry.getFirst().getSecond() == -1 ? random
											.nextInt(M) : entry.getFirst()
											.getSecond()));
			}
		}
		double norm = 0;
		for (int i = 0; i < N; i++)
			for (int k = 0; k < K; k++)
				norm += Math.pow(A[i][k], 2);
		for (int i = 0; i < M; i++)
			for (int k = 0; k < K; k++)
				norm += Math.pow(B[i][k], 2);
		double normF = 0;
		for (int i = 0; i < NF; i++)
			for (int k = 0; k < K; k++)
				normF += Math.pow(F[i][k], 2);
		return c + lambda * norm + lambdaF * normF;
	}

	private void train(int i, int j, double v) {
		if (j == -1) {
			j = random.nextInt(M);
		}
		double y = predict(i, j);
		double err = y - v;
		double g = 0;
		if (type == BASIC)
			g = 2 * err;
		if (type == SIGMOID)
			g = 2 * err;// * y * (1 - y);

		double[] user = getEmbedding_User(i), item = getEmbedding_Item(j);
		for (int k = 0; k < K; k++) {
			A[i][k] -= rate * (g * item[k] + lambda * A[i][k]);
			A[i][k]=Math.max(0, A[i][k]);
			B[j][k] -= rate * (g * user[k] + lambda * B[j][k]);
			B[j][k]=Math.max(0, B[j][k]);
		}
		for (Pair<Integer, Double> f : userF.get(i)) {
			int id = f.getFirst();
			double weight = f.getSecond();
			biasF[id] -= rate * g * weight;
			for (int k = 0; k < K; k++)
				F[id][k] -= rate * weight * (g * item[k] + lambdaF * F[id][k]);
		}
		biasA[i] -= rate * g;
		biasB[j] -= rate * g;
		bias -= rate * g;
	}

	private void train() {
		Collections.shuffle(table);
		final LinkedList<Pair<Pair<Integer, Integer>, Double>> Q = new LinkedList<Pair<Pair<Integer, Integer>, Double>>(
				table);

		Thread[] workers = new Thread[NThread];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new Thread() {
				@Override
				public void run() {
					for (;;) {
						Pair<Pair<Integer, Integer>, Double> entry = null;
						synchronized (Q) {
							if (Q.isEmpty())
								return;
							entry = Q.removeFirst();
						}
						train(entry.getFirst().getFirst(), entry.getFirst()
								.getSecond(), entry.getSecond());
					}
				}
			};
			workers[i].start();
		}

		for (Thread worker : workers)
			try {
				worker.join();
			} catch (Exception e) {
			}

		// for (Pair<Pair<Integer, Integer>, Double> entry : tabel)
		// train(entry.getFirst().getFirst(), entry.getFirst().getSecond(),
		// entry.getSecond());
	}

	public void train(int ROUND, boolean silent) {
		try {
			double lacost = getCost();
			if (!silent)
				System.out.println("Initial Cost = " + lacost);
			for (int i = 0; i < ROUND; i++) {
				train();
				double cur = getCost();
				if (cur < lacost)
					rate = Math.min(rate * 1.1, 1e-3);
				else
					rate /= 2;
				lacost = cur;
				if (!silent)
					System.out.println("Round " + i + "\tCost = " + lacost
							+ "\tRate = " + rate);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
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

	public double[] getEmbedding_Feature(int i) {
		return F[i];
	}

	public void save(String dir) {
		LinkedList<String> outdata = new LinkedList<String>();
		outdata.add("" + N);
		outdata.add("" + M);
		outdata.add("" + NF);
		outdata.add("" + K);
		outdata.add("" + globalBias);
		outdata.add("" + userBias);
		outdata.add("" + itemBias);
		outdata.add("" + bias);
		for (int i = 0; i < N; i++) {
			StringBuffer cur = new StringBuffer();
			cur.append(biasA[i]);
			for (int q = 0; q < K; q++)
				cur.append("\t" + A[i][q]);
			for (Pair<Integer, Double> curF : userF.get(i))
				cur.append("\t" + curF.getFirst() + "\t" + curF.getSecond());
			outdata.add(cur.toString());
		}
		for (int i = 0; i < M; i++) {
			StringBuffer cur = new StringBuffer();
			cur.append(biasB[i]);
			for (int q = 0; q < K; q++)
				cur.append("\t" + B[i][q]);
			for (Pair<Integer, Double> curF : itemF.get(i))
				cur.append("\t" + curF.getFirst() + "\t" + curF.getSecond());
			outdata.add(cur.toString());
		}
		for (int i = 0; i < NF; i++) {
			StringBuffer cur = new StringBuffer();
			cur.append(biasF[i]);
			for (int q = 0; q < K; q++)
				cur.append("\t" + F[i][q]);
			outdata.add(cur.toString());
		}
		FileOps.SaveFile(dir, outdata);
	}

	public void load(String dir) {
		LinkedList<String> data = FileOps.LoadFilebyLine(dir);
		Iterator<String> itr = data.iterator();
		N = Integer.valueOf(itr.next());
		M = Integer.valueOf(itr.next());
		NF = Integer.valueOf(itr.next());
		K = Integer.valueOf(itr.next());
		globalBias = Boolean.valueOf(itr.next());
		userBias = Boolean.valueOf(itr.next());
		itemBias = Boolean.valueOf(itr.next());
		bias = Double.valueOf(itr.next());
		A = new double[N][K];
		biasA = new double[N];
		userF = new ArrayList<LinkedList<Pair<Integer, Double>>>();
		for (int i = 0; i < N; i++) {
			String[] sep = itr.next().split("\t");
			biasA[i] = Double.valueOf(sep[0]);
			for (int k = 0; k < K; k++)
				A[i][k] = Double.valueOf(sep[1 + k]);
			LinkedList<Pair<Integer, Double>> curF = new LinkedList<Pair<Integer, Double>>();
			for (int k = K + 1; k < sep.length; k += 2)
				curF.add(new Pair<Integer, Double>(Integer.valueOf(sep[K]),
						Double.valueOf(sep[k + 1])));
			userF.add(curF);
		}
		B = new double[M][K];
		biasB = new double[M];
		itemF = new ArrayList<LinkedList<Pair<Integer, Double>>>();
		for (int i = 0; i < M; i++) {
			String[] sep = itr.next().split("\t");
			biasB[i] = Double.valueOf(sep[0]);
			for (int k = 0; k < K; k++)
				B[i][k] = Double.valueOf(sep[1 + k]);
			LinkedList<Pair<Integer, Double>> curF = new LinkedList<Pair<Integer, Double>>();
			for (int k = K + 1; k < sep.length; k += 2)
				curF.add(new Pair<Integer, Double>(Integer.valueOf(sep[K]),
						Double.valueOf(sep[k + 1])));
			itemF.add(curF);
		}
		F = new double[NF][K];
		biasF = new double[NF];
		for (int i = 0; i < NF; i++) {
			String[] sep = itr.next().split("\t");
			biasF[i] = Double.valueOf(sep[0]);
			for (int k = 0; k < K; k++)
				F[i][k] = Double.valueOf(sep[1 + k]);
		}
	}
}
