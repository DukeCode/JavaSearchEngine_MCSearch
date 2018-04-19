package init.tools;

public class DirichletSmooth {
	public static double calculateScore(int idf, long cf, double miu, int docLength, long cLength) {
		double result = (idf + miu * (cf / cLength)) / (docLength + miu);
		return result;
	}
}
