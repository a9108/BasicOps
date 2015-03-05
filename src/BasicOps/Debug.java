package BasicOps;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.naming.InitialContext;


public class Debug {
	public static BufferedWriter out;
	public static void write(String s){
		try{
			if (out!=null){
				out.write(s+"\n");
				out.flush();
			}
		}catch (Exception ex){}
	}
	public static void init(){
		try{
			out=new BufferedWriter(new FileWriter(new File("log")));
		}catch (Exception ex){
		}
	}
	public static void close(){
		try{
			out.close();
		}catch (Exception ex){}
	}
	
}
