package basic;

import java.util.LinkedList;

public class Display {
	public static void showTop(String msg,LinkedList<String> data,int N){
		System.out.println(msg);
		for (String s:data){
			System.out.println(s);
			N--;
			if (N==0) break;
		}
	}
}
