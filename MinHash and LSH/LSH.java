
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author charlesglenn
 *
 */
public class LSH {

	
	private int prime;
	private int A;
	private int B;
	private int bands;
	private ArrayList<ArrayList<Set<String>>> lshTable;
	

	public LSH(int[][] minHashMatrix, String[] docNames, int bands) {
		
		//Get basic info from min hash matrix
		int numPerms = minHashMatrix.length; 
		int numDocs = docNames.length;
		this.bands = bands;
		
		//Set up hash tables for LSH
		this.lshTable = new ArrayList<ArrayList<Set<String>>>();
		
		//Set up hash function
		getHashFunctions(numDocs, bands);
		
		//Hash min hash signatures
		for (int i=0;i<numDocs;i++) {
			
			String doc = docNames[i];
			ArrayList<Integer> tuple = new ArrayList<Integer>();
			
			
			int count = 0;
			for (int j=0;j<numPerms;j++) {
				
				tuple.add(minHashMatrix[j][i]);
				
				//Always rounding numPerms/bands up will leave us with one smaller
				//tuple at the end of hasing.  If we round down we will run out of 
				//hash tables to place our tuples.
				if (tuple.size() == Math.ceil((double)numPerms/bands)) {
					
					hashTuple(tuple,doc, count);
					count++;
					tuple.clear();
					
				}
				//In the case of uneven tuple sizes.
				if ( j==(numPerms-1) && tuple.size() != 0) {
					
					hashTuple(tuple,doc,count);
					count++;
					tuple.clear();
					
				}
				
			}
		}
	}
	
	private void getHashFunctions(int numDocs, int bands) {
		
		this.prime = getPrime(numDocs);
			
		this.A= (int) Math.floor(Math.random() * prime);
		this.B= (int) Math.floor(Math.random() * prime);

		for (int i=0; i<this.prime; i++) {
			
			this.lshTable.add(new ArrayList<Set<String>>());
			

			for (int j=0; j<this.bands; j++) {
			
				this.lshTable.get(i).add(new HashSet<String>());
		
			}
		}
		
	}

	private void hashTuple(ArrayList<Integer> tuple,String doc, int table) {
		
		int hash = tuple.hashCode();
		hash = A* hash + B;
		hash = hash%this.prime;
		if ((hash) < 0) { hash = hash*-1; }
			
		this.lshTable.get(hash).get(table).add(doc);
		
	}

	ArrayList<String> nearDublicatesOf(String docName) {
		
		ArrayList<String> dupes = new ArrayList<String>();
		
		//Look through every bucket of every hash table
		for (int i=0; i<bands; i++) {
			for (int j=0; j<this.prime; j++) {
				
				//If a bucket contains docName
				if (this.lshTable.get(j).get(i).contains(docName)) {
					
					//Store all other docs not already accounted for
					Iterator<String> it = this.lshTable.get(j).get(i).iterator();
					
					while(it.hasNext()) {
						
						String temp = it.next();
						if (!temp.equals(docName) && !dupes.contains(temp)) { dupes.add(temp); }
		
					}
					
				}
			}
		}
		
		return dupes;
	}
	
	private int getPrime(int n)
	{
		//Find and return next largest prime
		boolean[] primes = new boolean[2*n];
		Arrays.fill(primes, true);
		primes[0] = false;
		primes[1] = false;
		for (int i=2; i<primes.length;i++)
		{
			if (primes[i])
			{
				for(int j=2; i*j<primes.length;j++)
				{
					primes[i*j] = false;
				}
			}
		}
		
		for (int i=n; i<2*n; i++) {
			
			if (primes[i]) { return i; }
		}
		
		return (Integer) null;
	}
}
