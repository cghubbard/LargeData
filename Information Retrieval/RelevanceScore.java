
public class RelevanceScore {

	private String document;
	private double score;
	private double tpscore;
	private double vsscore;
	
	public RelevanceScore (String doc, double score) {
		
		setScore(score);
		setDocument(doc);
		
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getVsscore() {
		return vsscore;
	}
	public void setVsscore(double vsscore) {
		this.vsscore = vsscore;
	}
	public double getTpscore() {
		return tpscore;
	}
	public void setTpscore(double tpscore) {
		this.tpscore = tpscore;
	}
}
