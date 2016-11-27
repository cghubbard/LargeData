import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
/**
 * 
 * @author charlesglenn
 *
 */
public class MinHash {
	
	
	private File folder;
	private String[] files;
	private int[][] minHashMatrix;
	private int numPermutations;
	private HashMap<String, Integer> termTable = new HashMap<String, Integer>(40000);
	private Integer[][] perms;
	private String[] termList;
	private HashMap<String,Integer> docIndex = new HashMap<String, Integer>();
	private int numTerms;

	
	MinHash (String folder,int numPermutations) {
		
		//Set folder and permutations
		this.folder = new File(folder);
		this.numPermutations = numPermutations;
		this.files = new String[this.folder.list().length];
		
		//Build term list
		for (String doc : this.folder.list()) {
			parseDoc(doc);
		}
		
		int numTerms = termTable.size();
		this.numTerms = numTerms;
		
		this.perms = new Integer[numTerms][numPermutations];
		
		ArrayList<Integer> rowPerm = new ArrayList<Integer>(numTerms);
		
		for (int i=0;i<numTerms;i++) {
			rowPerm.add(i);
		}
		
		this.perms = new Integer[numPermutations][];
		
		//Set up permutations
		for (int i=0;i<numPermutations;i++) {
			
			Collections.shuffle(rowPerm);
			this.perms[i] = new Integer[numTerms];
			this.perms[i] = rowPerm.toArray(this.perms[i]);
			
		}
		
		this.termList = termTable.keySet().toArray(new String[termTable.size()]);
		
		//Get min hash signature for documents
		int docNum = 0;
		int[] tempSig;
		this.minHashMatrix = new int[this.numPermutations][this.termList.length];
		
		for (String file : this.folder.list()) {
			
			tempSig = minHashSig(file);
			
			for (int i=0; i<tempSig.length;i++) {
				
				this.minHashMatrix[i][docNum] = tempSig[i];
				
			}
			
			this.docIndex.put(file, docNum);
			this.files[docNum] = file;
			docNum++;
			
		}
		
	}
	
	private void parseDoc(String doc) {
		
		try {
			
			FileReader document = new FileReader(this.folder+"/"+doc);
			BufferedReader reader = new BufferedReader(document);
			
			String line;
			String[] terms;
			while ((line = reader.readLine()) != null) {
				
				line = line.toLowerCase();
				line = line.replaceAll("[.,;:']", "");
				terms = line.split("\\s+");
				
				for (String term : terms) {
					
					if ((term.length() > 2) && (!term.equals("the")) && ((!this.termTable.containsKey(term)))) {
						
						this.termTable.put(term, termTable.size()+1);
						
					}
				}
				
			}
			
			reader.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public int[] minHashSig(String fileName) {
		
		try {
			
			//Build term vector
			FileReader document = new FileReader(this.folder+"/"+fileName);
			BufferedReader reader = new BufferedReader(document);
			
			int[] termVector = new int[termTable.size()];
			String line;
			String[] terms;
			while ((line = reader.readLine()) != null) {
				
				line = line.toLowerCase();
				line = line.replaceAll("[.,;:']", "");
				terms = line.split("\\s+");
				
				for (String term : terms) {
					
					if ((term.length() > 2) && (!term.equals("the"))) {
						
						int num = this.termTable.get(term);
						if (num != 0) {
							termVector[num-1] = 1;
						}
						
						
					}
				}
				
			}
			
			reader.close();
			
			//Get min from permutations
			int[] sig = new int[this.numPermutations];
			int temp = 0;
			
			for(int i=0;i<numPermutations;i++) {
				
				for (int j=0; j<termTable.size();j++) {
					
					temp = perms[i][j];
					
					if (termVector[temp] == 1) {
						
						sig[i] = temp;
						break;
					}
				}
			}
			
			
			return sig;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

	public String[] allDocs() { 
		return this.files; 
	}
	
	public int[][] minHashMatrix () {
		return this.minHashMatrix;
	}
	
	public int numTerms() {
		return this.numTerms;
	}
	
	public int numPermutations() {
		return this.numPermutations;
	}

	public double exactJaccard(String file1, String file2) {
		
		//Setup sets for Jaccard Sim computations
		HashSet<String> set1 = new HashSet<String>();
		HashSet<String> set2 = new HashSet<String>();
		HashSet<String> intersection;
		HashSet<String> union;
		String[] bothFiles = {file1, file2};
		
		try {
			
			//Parse documents
			for (int i=0; i<2; i++) {
				
				
				FileReader document = new FileReader(this.folder+"/"+bothFiles[i]);
				BufferedReader reader = new BufferedReader(document);
			
				String line;
				String[] terms;
			
			
			
				while ((line = reader.readLine()) != null) {
					
					line = line.toLowerCase();
					line = line.trim();
					line = line.replaceAll("[.,;:']", "");
					terms = line.split("\\s+");
					
					for (String term : terms) {
						
						if ((term.length() > 2) && (!term.equals("the"))) {
							
							if (i==0) { set1.add(term); }
							if (i==1) { set2.add(term); }
							
						}
					}
					
				}
				
				reader.close();
				
			}	
			
			//Get intersection and union of term sets
			intersection = new HashSet<String>(set1);
			intersection.retainAll(set2);
			
			union = new HashSet<String>(set1);
			union.addAll(set2);
			
			return ( ((double) intersection.size())/union.size());
			
	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (Double) null;
		
	}

	public double approximateJaccard(String file1, String file2) {
		
		//Get indicies of documents
		int file1_number = this.docIndex.get(file1);
		int file2_number = this.docIndex.get(file2);
		
		//Count same mins
		double same_min = 0;
		
		for (int k=0; k<this.numPermutations;k++) {
		
			if ( this.minHashMatrix[k][file1_number] == this.minHashMatrix[k][file2_number] ) { same_min++; }
			
		}
		
		//Return empirical probability of permutations
		//resulting in same min
		return (same_min/this.numPermutations);
	}


}
