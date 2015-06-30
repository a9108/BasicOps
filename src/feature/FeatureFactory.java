package feature;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.scene.control.Separator;

import com.sun.org.apache.xml.internal.security.Init;

import basic.Config;
import basic.format.Feature;
import basic.format.SparseFeature;

public class FeatureFactory<T> {
	private int NFeature;
	private ArrayList<FeatureGen<T>> features;
	private ArrayList<String[]> modifier;

	public FeatureFactory() {
		initialize(Config.getString("Features"),
				Config.getString("FeaturePackage"));
	}

	public FeatureFactory(String features, String prefix) {
		initialize(features, prefix);
	}

	private void initialize(String fnames, String prefix) {
		String[] names = fnames.split(";");
		features = new ArrayList<FeatureGen<T>>();
		modifier = new ArrayList<String[]>();
		NFeature = 0;
		int id = 0;
		for (String name : names)
			if (name.length() > 0) {
				FeatureGen<T> fgen = null;
				String[] sep = name.split(",");
				try {
					fgen = (FeatureGen<T>) Class.forName(prefix + "." + sep[0])
							.newInstance();
				} catch (Exception e) {
					System.err.println("Unknown Feature : " + name);
				}
				try {
					fgen.initialize();
					NFeature += fgen.size() * sep.length;
					features.add(fgen);
					modifier.add(sep);
					// if (fgen.getNames().length != fgen.size())
					// System.err.println("Feature Size Mismatch (" + name
					// + ")");
					// for (String s : fgen.getNames())
					// System.out.println("Feature #" + (id++) + " : " + s);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
	}

	public Feature genFeature(T info, boolean isTrain) {
		Feature f = null;
		for (int i = 0; i < features.size(); i++) {
			FeatureGen<T> fgen = features.get(i);
			Feature cur = fgen.genFeature(info, isTrain);
			if (f == null){
				f=new SparseFeature();
				f.setResult(cur.getResult());
				f.setSize(0);
				f.append(cur);
			}
			else
				f.append(cur);
			for (int j = 1; j < modifier.get(i).length; j++) {
				String mod = modifier.get(i)[j].toLowerCase();
				if (mod.equals("log"))
					f.append(FeatureModifier.LogModifier(cur));
				else if (mod.equals("sqrt"))
					f.append(FeatureModifier.SqrtModifier(cur));
				else if (mod.equals("hot"))
					f.append(FeatureModifier.HotModifier(cur));
				else if (mod.startsWith("pow"))
					f.append(FeatureModifier.PowModifier(cur,
							Double.valueOf(mod.split(":")[1])));
				else {
					System.err.println("Unknown Feature Modifier : " + mod);
					f.append(cur);
				}
			}

		}
		return f;
	}

	public int size() {
		return NFeature;
	}
}
