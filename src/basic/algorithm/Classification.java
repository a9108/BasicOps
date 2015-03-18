package basic.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import basic.format.Feature;

public abstract class Classification {
	protected LinkedList<Feature> train=new LinkedList<Feature>();
	protected int NFeature;
	
	public int getNFeature() {
		return NFeature;
	}

	public void setNFeature(int nFeature) {
		NFeature = nFeature;
	}

	public void addTrain(Feature feature){
		train.add(feature);
	}
	
	public abstract void train();
	public abstract double predict(Feature data);
	public ArrayList<Double> predict(List<Feature> data){
		ArrayList<Double> res=new ArrayList<Double>();
		for (Feature f:data)
			res.add(predict(f));
		return res;
	}
	
}
