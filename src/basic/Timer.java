package basic;

import java.util.Date;

public class Timer {
	private long time;
	public Timer() {
		time=new Date().getTime();
	}
	public void tick(String name){
		long cur=new Date().getTime();
		System.err.println("Timer - "+name+" : "+(cur-time)/1000.+"s");
		time=cur;
	}
}
