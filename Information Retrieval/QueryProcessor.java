import java.util.ArrayList;

public class QueryProcessor {
	
	PositionalIndex index;
	public QueryProcessor(String path) {
		this.index = new PositionalIndex(path);
	}
	
	public ArrayList<String> topKDocs(String query,int k) {
		
		ArrayList<RelevanceScore> topK = index.topKRelevant(query, k);
		
		ArrayList<String> topDocs = new ArrayList<String>();
		for (int i=0;i<k;i++) {
			topDocs.add(topK.get(i).getDocument());
		}
		return topDocs;
	}
}
