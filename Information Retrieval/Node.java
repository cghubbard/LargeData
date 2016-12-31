import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;

// Stores docucment frequency and postings list
public class Node {

		private int documentFreq;
		private Hashtable<String,Posting> postings;
		
		Node() {
			
			this.documentFreq = 0;
			this.postings = new Hashtable<String,Posting>();
			
		}
		
		public void addTermPosition (String document, int position) {
			
			if (!postings.containsKey(document)) {
				
				this.documentFreq++;
				ArrayList<Integer> positionStore = new ArrayList<Integer>();
				postings.put(document,new Posting(documentFreq,positionStore,document));
				
			} 
			
			postings.get(document).add(position);
			
		}
		
		public int getDocumentFreq() {
			return this.documentFreq;
		}
		
		public int getTermFrequency(String document) {
			
			if (postings.get(document) == null) {
				return 0;
			}
			return postings.get(document).size();
		}
		
		public String getPostingsList() {
			
			// get all docs with term
			Set<String> keys = this.postings.keySet();
			
			// sort based on when document was added
			Collection<Posting> collectionPostings = this.postings.values();
			TreeSet<Posting> postings = new TreeSet<Posting>(collectionPostings);
			
			// start string
			String postingsList = "[";
			
			// write positions from every doc
			//for ( String key : keys ) {
			while (!postings.isEmpty()) {
				
				Posting post = postings.pollFirst();
				String key = post.getDocName();
				
				String posting = "<";
				posting += key + ":";
				
				int numPositions = this.postings.get(key).size();
				ArrayList<Integer> positions = this.postings.get(key).getPositions();
				
				for (int i=0; i<numPositions; i++ ) {
					
					if (i == numPositions - 1) { posting += positions.get(i); }
					else { posting += positions.get(i) + ","; }
				}
				
				posting += ">,";
				postingsList += posting;
			}
			
			// remove last comma
			postingsList = postingsList.substring(0, postingsList.length()-1);
			
			// add final bracket
			postingsList += "]";
			return postingsList;
		}

		public ArrayList<Integer> getPositions(String document) {
			
			if (this.postings.get(document) == null) {
				return null;
			}
			
			return this.postings.get(document).getPositions();
			
		}
}
