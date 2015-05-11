package basic;

import java.util.*;

public class Config {
	public static Map<String, String> config = new HashMap<String, String>();

	public static void load(String dir) {
		List<String> rows = FileOps.LoadFilebyLine(dir);
		for (String row : rows) {
			String[] sep = row.split("\\s+");
			if (sep.length == 2) {
				config.put(sep[0], sep[1]);
			}
		}
	}

	public static String getValue(String s) {
		if (config.containsKey(s))
			return config.get(s);
		return "";
	}
	
	public static String getString(String s) {
		if (config.containsKey(s))
			return config.get(s);
		return "";
	}

	public static void setValue(String name, String value) {
		config.put(name, value);
	}

	public static double getDouble(String name) {
		try {
			return Double.valueOf(config.get(name));
		} catch (Exception ex) {
		}
		return 0.;
	}

	public static int getInt(String name) {
		try {
			return Integer.valueOf(config.get(name));
		} catch (Exception ex) {
		}
		return 0;
	}
	
	public static boolean getBoolean(String name){
		return getString(name).toLowerCase().equals("true");
	}
}
