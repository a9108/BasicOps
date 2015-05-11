package basic.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import basic.Config;
import basic.FileOps;
import basic.SystemOps;
import basic.format.Feature;

public class WinSVM extends Classification {
	private Random rand = new Random();

	final private String svmDir;
	private String param_train, param_test;
	private String svmID;

	public WinSVM(String svmDir, String param_train, String param_test) {
		this.svmDir = svmDir;
		this.param_train = param_train;
		this.param_test = param_test;
		svmID = "." + rand.nextInt(1000000);
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
		FileOps.SaveFile("svm.in" + svmID, outdata);
		SystemOps.syscall(svmDir + "\\svm-scale -l 0 -u 1 -s svm.scale" + svmID
				+ " svm.in" + svmID, "svm.sin" + svmID);
		SystemOps.syscall(svmDir + "\\svm-train " + param_train + " svm.sin"
				+ svmID + " svm.model" + svmID, false);
		// SystemOps.syscall(svmDir + "\\svm-predict svm.sin" + svmID
		// + " svm.model+" + svmID + " svm.out" + svmID, false);
	}

	@Override
	public double predict(Feature data) {
		return -1;
	}

	@Override
	public ArrayList<Double> predict(List<Feature> datas) {
		ArrayList<Double> tres = new ArrayList<Double>();
		LinkedList<String> outdata = new LinkedList<String>();
		for (Feature data : datas) {
			StringBuilder sb = new StringBuilder();
			sb.append(data.getResult());
			for (int i = 0; i < data.size(); i++)
				sb.append("\t" + (i + 1) + ":" + data.getValue(i));
			outdata.add(sb.toString());
		}
		LinkedList<String> res;
		// synchronized (svmDir) {
		String id = "." + rand.nextInt(1000000);
		FileOps.SaveFile("svm.test" + id, outdata);
		SystemOps.syscall(svmDir + "\\svm-scale -r svm.scale" + svmID
				+ " svm.test" + id, "svm.stest" + id);
		SystemOps.syscall(svmDir + "\\svm-predict " + param_test + " svm.stest"
				+ id + " svm.model" + svmID + " svm.out" + id, false);
		res = FileOps.LoadFilebyLine("svm.out" + id);
		// }

		// FileOps.remove("svm.out" + id);
		// FileOps.remove("svm.stest" + id);
		// FileOps.remove("svm.test" + id);
		Iterator<String> ires = res.iterator();
		ires.hasNext();
		int cid = 1;
		if (ires.next().endsWith(" 1"))
			cid = 2;
		System.out.println(cid);
		for (; ires.hasNext();) {
			try {
				double dres = Double.valueOf(ires.next().split(" ")[cid]);
				tres.add(dres);
			} catch (Exception ex) {
				tres.add(0.0);
				ex.printStackTrace();
			}
		}
		System.out.println(tres.size() + "\t" + datas.size());
		return tres;
	}

	@Override
	public void destroy() {
		FileOps.remove("svm.in." + svmID);
		FileOps.remove("svm.sin." + svmID);
		FileOps.remove("svm.scale." + svmID);
		FileOps.remove("svm.model." + svmID);
	}

}
