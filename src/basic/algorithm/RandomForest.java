package basic.algorithm;

import java.util.LinkedList;
import java.util.Random;

import basic.Config;
import basic.DataOps;
import basic.Vector;
import basic.format.Feature;

public class RandomForest extends Classification {

	private int MAX_LEVEL;
	private int MIN_DATACNT;
	private double FEATURE_RATIO;
	private double DATA_RATIO;
	private int NUM_TREES;

	LinkedList<RandomTree> trees;

	public RandomForest(int num_trees, int max_level, int min_datacnt,
			double feature_ratio, double data_ratio, int NFeatures) {
		NUM_TREES = num_trees;
		MAX_LEVEL = max_level;
		MIN_DATACNT = min_datacnt;
		FEATURE_RATIO = feature_ratio;
		DATA_RATIO = data_ratio;
		setNFeature(NFeatures);
	}

	@Override
	public void train() {
		trees = new LinkedList<RandomTree>();
		final Random random = new Random();
		final LinkedList<Integer> Q = new LinkedList<Integer>();
		for (int i = 0; i < NUM_TREES; i++)
			Q.add(i);
		Thread[] workers = new Thread[Math.max(1, Config.getInt("Threads"))];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new Thread() {
				@Override
				public void run() {
					for (;;) {
						synchronized (Q) {
							if (Q.isEmpty())
								return;
							Q.removeFirst();
						}
						RandomTree tree = new RandomTree(MAX_LEVEL,
								MIN_DATACNT, FEATURE_RATIO, NFeature);
						for (Feature f : train)
							if (random.nextDouble() < DATA_RATIO)
								tree.addTrain(f);
						tree.train();
						synchronized (trees) {
							trees.add(tree);
						}
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

	}

	@Override
	public void destroy() {
		for (RandomTree tree : trees)
			tree.destroy();
	}

	@Override
	public double predict(Feature data) {
		LinkedList<Double> res = new LinkedList<Double>();
		for (RandomTree tree : trees)
			res.add(tree.predict(data));
		return DataOps.average(res);
	}

}
