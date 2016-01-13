package basic.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import basic.FileOps;
import basic.format.Feature;

public class xgBoost extends Classification {
	private Random rand = new Random();
	private String xgdir;

	public xgBoost(String xgdir) {
		this.xgdir = xgdir;
	}

	@Override
	public void train() {
		LinkedList<String> outdata = new LinkedList<String>();
		for (Feature data : train) {
			StringBuilder sb = new StringBuilder();
			sb.append(data.getResult());
			for (int i = 0; i < data.size(); i++)
				sb.append("\t" + (i + 1) + ":" + data.getValue(i));
			outdata.add(sb.toString());
		}
		FileOps.SaveFile(xgdir + "xgboost.train", outdata);
	}

	@Override
	public double predict(Feature data) {
		System.err.println("Single Predict is not Recommended for XGBOOST");
		LinkedList<Feature> datas = new LinkedList<Feature>();
		datas.add(data);
		return predict(datas).get(0);
	}

	@Override
	public ArrayList<Double> predict(List<Feature> datas) {
		ArrayList<Double> tres = new ArrayList<Double>();
		if (datas!=null){
		LinkedList<String> outdata = new LinkedList<String>();
		for (Feature data : datas) {
			StringBuilder sb = new StringBuilder();
			sb.append(data.getResult());
			for (int i = 0; i < data.size(); i++)
				sb.append("\t" + (i + 1) + ":" + data.getValue(i));
			outdata.add(sb.toString());
		}
		FileOps.SaveFile(xgdir + "xgboost.test", outdata);
		}
		return tres;
	}

	@Override
	public void destroy() {
		FileOps.remove(xgdir + "xgboost.test");
		FileOps.remove(xgdir + "xgboost.train");
		FileOps.remove(xgdir + "pred.txt");
	}

	@Override
	public void clear() {
	}
}
