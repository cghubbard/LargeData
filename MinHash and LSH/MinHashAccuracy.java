/**
 * 
 * @author charlesglenn
 *
 */
public class MinHashAccuracy {

	public static void main(String[] args) {
		
		int perms[] = {400,600,800};
		double eta[] = {0.04,0.07,0.09};
		//double eta[] = {0.04};
		
		for (int i=0;i<perms.length;i++) {
			System.out.println("Number of permutations: " + perms[i]);
			for (int j=0;j<eta.length;j++) {
				
				System.out.println("	" + "Error threshold: "+ eta[j]);
				accuracy("space",perms[i],eta[j]);
				
				
			}
			System.out.println();
		}
	}
	
	public static void accuracy(String folder, int numPermutations, double eta) {
		
		
		MinHash mh = new MinHash(folder,numPermutations);
		
		String[] files = mh.allDocs();
		int count = 0;
		
		for (int i=0; i<files.length;i++) {
			for (int j=i+1; j<files.length;j++) {
				
				double exactJac = mh.exactJaccard(files[i], files[j]);
				double approxJac = mh.approximateJaccard(files[i], files[j]);
				
				if (Math.abs(exactJac-approxJac)>eta) { 
					count++; }
				
			}
		}
		
		System.out.println("		" + "Number of pairs with abs(difference) > " + eta + " = " + count);
		
	}

}
