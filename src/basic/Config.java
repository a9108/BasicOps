package basic;

import java.util.*;

public class Config {
	public static Map<String, String> config=new HashMap<String,String>();
	public static void load(String dir){
		List<String> rows=FileOps.LoadFilebyLine(dir);
		for (String row:rows){
			String[] sep=row.split("\\s+");
			if (sep.length==2){
				config.put(sep[0], sep[1]);
			}
		}
	}
	public static String getValue(String s){
		if (config.containsKey(s))
			return config.get(s);
		return "";
	}
	public static void setValue(String name,String value){
		config.put(name, value);
	}
}