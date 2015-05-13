package feature;

import java.util.LinkedList;

import basic.Config;
import basic.format.Feature;
import basic.format.SparseFeature;

public class FeatureFactory<T> {
	private int NFeature;
	private LinkedList<FeatureGen<T>> features;

	public FeatureFactory() {
		String[] names = Config.getString("Features").split(";");
		String prefix = Config.getString("FeaturePackage");
		features = new LinkedList<FeatureGen<T>>();
		NFeature = 0;
		int id = 0;
		for (String name : names)
			if (name.length() > 0) {
				FeatureGen<T> fgen = null;
				try {
					fgen = (FeatureGen<T>) Class.forName(prefix + "." + name)
							.newInstance();
				} catch (Exception e) {
					System.err.println("Unknown Feature : " + name);
				}
				try {
					fgen.initialize();
					NFeature += fgen.size();
					features.add(fgen);
					if (fgen.getNames().length != fgen.size())
						System.err.println("Feature Size Mismatch (" + name
								+ ")");
					for (String s : fgen.getNames())
						System.out.println("Feature #" + (id++) + " : " + s);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
	}

	public Feature genFeature(T info) {
		Feature f = null;
		for (FeatureGen<T> fgen : features) {
			if (f == null)
				f = fgen.genFeature(info);
			else
				f.append(fgen.genFeature(info));
		}
		return f;
	}

	public int size() {
		return NFeature;
	}
}
