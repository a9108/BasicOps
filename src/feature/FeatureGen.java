package feature;

import java.util.LinkedList;

import basic.format.Feature;


public abstract class FeatureGen<T> {
	protected int nFeature;

	public int size() {
		return nFeature;
	}

	public abstract void initialize();

	public abstract Feature genFeature(T info);
	
	public abstract String[] getNames();
}
