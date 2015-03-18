package basic.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import basic.Config;
import basic.FileOps;
import basic.SystemOps;
import basic.format.Feature;

public class WinSVM extends Classification{
	private Random rand=new Random();
	
	final private String svmDir;
	private String param_train,param_test;
	public WinSVM(String svmDir,String param_train,String param_test) {
		this.svmDir=svmDir;
		this.param_train=param_train;
		this.param_test=param_test;
	}
	@Override
	public void train() {
		LinkedList<String> outdata=new LinkedList<String>();
		for (Feature data:train){
			StringBuilder sb=new StringBuilder();
			sb.append(data.getResult());
			for (int i=0;i<data.size();i++)
				sb.append("\t"+(i+1)+":"+data.getValue(i));
			outdata.add(sb.toString());
		}
		FileOps.SaveFile("svm.in", outdata);
		SystemOps.syscall(svmDir+"\\svm-scale -l 0 -u 1 -s svm.scale svm.in", "svm.sin");
		SystemOps.syscall(svmDir+"\\svm-train "+param_train+" svm.sin svm.model", false);
		SystemOps.syscall(svmDir+"\\svm-predict svm.sin svm.model svm.out", false);
	}
	@Override
	public double predict(Feature data) {
		StringBuilder sb=new StringBuilder();
		sb.append(data.getResult());
		for (int i=0;i<data.size();i++)
			sb.append("\t"+(i+1)+":"+data.getValue(i));
		LinkedList<String> res;
		synchronized (svmDir) {
			FileOps.SaveFile("svm.test", sb.toString());
			SystemOps.syscall(svmDir+"\\svm-scale -r svm.scale svm.test", "svm.stest");
			SystemOps.syscall(svmDir+"\\svm-predict "+param_train+" svm.stest svm.model svm.out", true);
			res=FileOps.LoadFilebyLine("svm.out");
		}
		double dres=Double.valueOf(res.get(1).split(" ")[1]);
		return dres;
	}
	@Override
	public ArrayList<Double> predict(List<Feature> datas) {
		System.out.println("Batch Predict");
		ArrayList<Double> tres=new ArrayList<Double>();
		LinkedList<String> outdata=new LinkedList<String>();
		for (Feature data:datas){
			StringBuilder sb=new StringBuilder();
			sb.append(data.getResult());
			for (int i=0;i<data.size();i++)
				sb.append("\t"+(i+1)+":"+data.getValue(i));
			outdata.add(sb.toString());
		}
		LinkedList<String> res;
//		synchronized (svmDir) {
		String id="."+rand.nextInt(1000000);
			FileOps.SaveFile("svm.test"+id, outdata);
			SystemOps.syscall(svmDir+"\\svm-scale -r svm.scale svm.test"+id, "svm.stest"+id);
			SystemOps.syscall(svmDir+"\\svm-predict "+param_train+" svm.stest"+id+" svm.model svm.out"+id, true);
			res=FileOps.LoadFilebyLine("svm.out"+id);
//		}
			
		FileOps.remove("svm.out"+id);
		FileOps.remove("svm.stest"+id);
		FileOps.remove("svm.test"+id);
		for (int i=0;i<datas.size();i++){
			try{
			double dres=Double.valueOf(res.get(1+i).split(" ")[1]);
			tres.add(dres);
			}catch (Exception ex){
				tres.add(0.0);
				ex.printStackTrace();
			}
		}
		return tres;
	}

}
