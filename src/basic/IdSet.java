package basic;

import java.util.HashMap;

public class IdSet<T> {
	private HashMap<T,Integer> id=new HashMap<T,Integer>();
	
	public int getId(T cur,boolean insert){
		if (!id.containsKey(cur)){
			if (!insert) return -1;
			id.put(cur, id.size());
		}
		return id.get(cur);
	}
	
	public int size(){
		return id.size();
	}
}
