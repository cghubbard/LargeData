/**
 * 
 * @author charlesglenn
 *
 */
public class MinHashTime {

	public static void main(String[] args) {
		timer("space", 600);

	}
	
	public static void timer(String folder, int numPermutations) {
		
		long start = System.currentTimeMillis();
		MinHash mh = new MinHash(folder,numPermutations);
		long end = System.currentTimeMillis();
		
		System.out.println("Time taken to construct MinHash matrix: " + (end-start) + " milliseconds.");
		
		String[] files = mh.allDocs();
		int count = 0;
		
		start = System.currentTimeMillis();
		for (int i=0; i<files.length;i++) {
			for (int j=i+1; j<files.length;j++) { double exactJac = mh.exactJaccard(files[i], files[j]); }
		}
		end = System.currentTimeMillis();
		
		System.out.println("Time taken to compute exact Jaccard similarities: " + (end-start)+ " milliseconds");
		
		start = System.currentTimeMillis();
		for (int i=0; i<files.length;i++) {
			for (int j=i+1; j<files.length;j++) { double approxJac = mh.approximateJaccard(files[i], files[j]); }
		}
		end = System.currentTimeMillis();
		
		System.out.println("Time taken to compute approximate Jaccard similarities: " + (end-start) + " milliseconds");

		
		
	}

}
