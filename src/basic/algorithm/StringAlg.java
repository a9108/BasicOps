package basic.algorithm;

public class StringAlg {
	public static int EditDistance(String a,String b){
		int[][] t=new int[a.length()+1][b.length()+1];
		for (int i=0;i<=a.length();i++) t[i][0]=i;
		for (int j=0;j<=b.length();j++) t[0][j]=j;
		for (int i=1;i<=a.length();i++)
			for (int j=1;j<=b.length();j++)
				t[i][j]=Math.min(Math.min(t[i-1][j], t[i][j-1])+1,
						t[i-1][j-1]+(a.charAt(i-1)==b.charAt(j-1)?0:1));
		return t[a.length()][b.length()];
	}
}
