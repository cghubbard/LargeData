import java.util.ArrayList;

/**
 * 
 * @author charlesglenn
 *
 */
public class NearDuplicates {

	public static void main(String[] args) {
		
		String[] dupes = nearDuplicateDetector("/Users/charlesglenn/Desktop/535pa2/F16PA2/",400,0.9,"space-0.txt",16);
		for (String dupe : dupes) { System.out.println(dupe); }

		
	}
	
	public static String[] nearDuplicateDetector(String folder, int numPermutations, double s, String docName,int bands) {
		
		MinHash mh = new MinHash(folder,numPermutations);
		
		LSH lsh = new LSH(mh.minHashMatrix(),mh.allDocs(),bands);
		
		ArrayList<String> dupes = lsh.nearDublicatesOf(docName);
		
		ArrayList<String> simDocs = new ArrayList<String>();
		for(String doc : dupes) {
			
			double similarity = mh.approximateJaccard(doc, docName);
			if (similarity>=s) {simDocs.add(doc);}
			
		}
		
		String[] simArray = new String[simDocs.size()];
		simArray = simDocs.toArray(simArray);
		return simArray;
	}

	
}
