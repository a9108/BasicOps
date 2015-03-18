package basic;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;

public class FileOps {

	public static void remove(String dir){
		File f=new File(dir);
		f.delete();
	}
	public static boolean exist(String dir){
		File f=new File(dir);
		return f.exists();
	}
	public static String LoadFile(String dir,String encoding){
		LinkedList<String> res=LoadFilebyLine(dir,encoding);
		StringBuilder sb=new StringBuilder();
		for (String q:res)
			sb.append(q+"\n");
		return sb.toString();
	}
	public static String LoadFile(String dir){
		LinkedList<String> res=LoadFilebyLine(dir);
		StringBuilder sb=new StringBuilder();
		for (String q:res)
			sb.append(q+"\n");
		return sb.toString();
	}
	public static <T> void SaveList(String dir,LinkedList<T> data){
		LinkedList<String> outdata=new LinkedList<String>();
		for (T item:data)
			outdata.add(item.toString());
		SaveFile(dir, outdata);
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
		}
		return res;
	}
	public static LinkedList<String> LoadFilebyLine(String dir,String encoding){
		LinkedList<String> res=new LinkedList<String>();
		try{
			InputStreamReader read = new InputStreamReader(new FileInputStream(new File(dir)),encoding);   
            BufferedReader reader=new BufferedReader(read);   
            String line;   
            while ((line = reader.readLine()) != null) {  
                res.add(line);
            }     
            read.close();   
		}catch (Exception ex){
//			System.err.println("File Loading Error : "+dir+"\t"+encoding);
//			ex.printStackTrace();
		}
		return res;
	}
	public static HashMap<String, Integer> LoadDiction(String dir){
		HashMap<String, Integer> res=new HashMap<String, Integer>();
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
	public static HashMap<String, String> LoadDictionSS(String dir){
		HashMap<String, String> res=new HashMap<String, String>();
		try{
			List<String> lines=LoadFilebyLine(dir);
			for (String line:lines){
				String[] sep=line.split("\t");
				res.put(sep[0], sep[1]);
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
