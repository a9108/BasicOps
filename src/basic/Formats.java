package basic;

import java.util.ArrayList;

public class Formats {
	public static ArrayList<Double> doubleArrayLoader(String s,String split){
		ArrayList<Double> res=new ArrayList<Double>();
		for (String item:s.split(split))
			res.add(Double.valueOf(item));
		return res;
	}
}
