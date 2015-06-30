package feature;

import basic.format.Feature;
import basic.format.SparseFeature;

public class FeatureModifier {
	public static Feature LogModifier(Feature f) {
		Feature nf = new SparseFeature();
		nf.setSize(f.size());
		for (int id : f.getIds())
			nf.setValue(id, Math.log(1 + f.getValue(id)));
		nf.setResult(f.getResult());
		return nf;
	}

	public static Feature HotModifier(Feature f) {
		Feature nf = new SparseFeature();
		nf.setSize(f.size());
		for (int id : f.getIds())
			nf.setValue(id, f.getValue(id) > 0 ? 1 : 0);
		nf.setResult(f.getResult());
		return nf;
	}

	public static Feature SqrtModifier(Feature f) {
		Feature nf = new SparseFeature();
		nf.setSize(f.size());
		for (int id : f.getIds())
			nf.setValue(id, Math.sqrt(Math.abs(f.getValue(id))));
		nf.setResult(f.getResult());
		return nf;
	}

	public static Feature PowModifier(Feature f, double d) {
		Feature nf = new SparseFeature();
		nf.setSize(f.size());
		for (int id : f.getIds())
			nf.setValue(id, Math.pow(f.getValue(id), d));
		nf.setResult(f.getResult());
		return nf;
	}
}
