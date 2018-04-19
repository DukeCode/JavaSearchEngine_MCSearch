package init.tools;

public class IdScorePair implements Comparable<IdScorePair> {
	public int docid;
	public double score;
	public IdScorePair(int docid, double score) {
		this.docid = docid;
		this.score = score;
	}
	
	@Override
	public int compareTo(IdScorePair other) {
		if (this.score < other.score) {
			return -1;
		}
		if (this.score > other.score) {
			return 1;
		}
		return 0;
	}
}
