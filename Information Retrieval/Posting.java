import java.util.ArrayList;

// Stores a single posting in the postings list
public class Posting implements Comparable<Posting>  {
	
	private int docNum;
	private ArrayList<Integer> positions;
	private String docName;
	
	public Posting(int docNum, ArrayList<Integer> positions, String docName) {
		
		setDocName(docName);
		setDocNum(docNum);
		setPositions(positions);
		
	}

	public void add(int position) {
		positions.add(position);
	}
	
	public int size() {
		return positions.size();
	}
	
	public int getDocNum() {
		return docNum;
	}

	public void setDocNum(int docNum) {
		this.docNum = docNum;
	}

	public ArrayList<Integer> getPositions() {
		return positions;
	}

	public void setPositions(ArrayList<Integer> positions) {
		this.positions = positions;
	}

	@Override
	public int compareTo(Posting p) {	
		return ((Integer) this.docNum).compareTo((Integer) p.getDocNum());
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	
}
