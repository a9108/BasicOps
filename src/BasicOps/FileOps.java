package BasicOps;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;


public class FileOps {
	public static boolean exist(String dir){
		File f=new File(dir);
		return f.exists();
	}
	public static String LoadFile(String dir){
		LinkedList<String> res=LoadFilebyLine(dir);
		String s="";
		for (String q:res)
			s+=q+"\n";
		return s;
	}
	public static LinkedList<String> LoadFilebyLine(String dir){
		LinkedList<String> res=new LinkedList<String>();
		try{
			BufferedReader fin=new BufferedReader(new FileReader(dir));
			for (;;){
				String s=fin.readLine();
				if (s==null) break;
				res.add(s);
			}
			fin.close();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return res;
	}
	public static Map<String, Integer> LoadDiction(String dir){
		Map<String, Integer> res=new HashMap<String, Integer>();
		try{
			List<String> lines=LoadFilebyLine(dir);
			for (String line:lines){
				String[] sep=line.split("\t");
				res.put(sep[0], Integer.valueOf(sep[1]));
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return res;
	}
	public static void SaveFile(String dir,String content){
		try{
			BufferedWriter fout=new BufferedWriter(new FileWriter(dir));
			fout.write(content);
			fout.close();
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	public static void SaveFile(String dir,List<String> content){
		try{
			BufferedWriter fout=new BufferedWriter(new FileWriter(dir));
			for (String s:content)
				fout.write(s+"\n");
			fout.close();
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	public static void SaveFile(String dir,Map<String,Integer> dict){
		try{
			BufferedWriter fout=new BufferedWriter(new FileWriter(dir));
			for (String s:dict.keySet())
				fout.write(s+"\t"+dict.get(s)+"\n");
			fout.close();
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	public static void createDir(String dir){
		try{
			File cur=new File(dir);
			cur.mkdir();
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
