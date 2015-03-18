package basic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class SystemOps {
	public static void syscall(String cmd, boolean silent) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);
//			System.err.println("Executing "+cmd);
			if (!silent) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(p.getInputStream()));
				String line = "";
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			}
			p.waitFor();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void syscall(String cmd, String redirect) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);
//			System.err.println("Executing "+cmd);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = "";
			LinkedList<String> data = new LinkedList<String>();
			while ((line = reader.readLine()) != null)
				data.add(line);
			FileOps.SaveFile(redirect, data);
			p.waitFor();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
