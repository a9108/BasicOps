package basic;

public class Matrix {

	public static double[] matmult(double[][] A, double[] V) {
		double[] res = new double[A.length];
		for (int i = 0; i < A.length; i++)
			res[i] = Vector.dot(A[i], V);
		return res;
	}

	public static double[] matmult(double[] V, double[][] A) {
		double[][] ta = trans(A);
		double[] res = new double[ta.length];
		for (int i = 0; i < ta.length; i++)
			res[i] = Vector.dot(V, ta[i]);
		return res;
	}

	public static double[][] matmult(double[][] A, double[][] B) {
		double[][] res = new double[A.length][B[0].length];
		for (int i = 0; i < A.length; i++)
			res[i] = matmult(A[i], B);
		return res;
	}

	public static double[][] trans(double[][] A) {
		double[][] res = new double[A[0].length][A.length];
		for (int i = 0; i < A.length; i++)
			for (int j = 0; j < A[i].length; j++)
				res[j][i] = A[i][j];
		return res;
	}
}
