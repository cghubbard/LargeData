import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeSet;


public class PositionalIndex {
	
	private Hashtable<String,Node> dictionary;
	private int numDocs;
	private File directory;
	
	
	public PositionalIndex(String inputFolder) {
		
		File folder = new File(inputFolder);
		this.directory = folder;
		
		dictionary = new Hashtable<String,Node>();
		
		File[] files = folder.listFiles();
		this.numDocs = files.length;
		
		for (int i=0; i<files.length; i++) {
			
			if (files[i].isFile()) { 
				
				parseFile(files[i]);
			}
		}
		
		
	}

	private void parseFile(File file) {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			String line;
			String[] words;
			
			// keep track to position of words in document
			int position = 0;
			
			while ((line = reader.readLine()) != null ) {
				
				line = line.toLowerCase();
				line = line.replaceAll("[,\"?'{}:;()\\]\\[]","");
				words = line.split("\\s+");
				
				
				for (int i=0; i<words.length; i++) {
					
					// Check for periods in non-floating point numbers
					if ( words[i].contains(".") ) {
						
						try { 
							double check = Double.parseDouble(words[i]); 
							}
						catch (NumberFormatException e) {
							words[i] = words[i].replaceAll("\\.", "");
						}
						
					}
					
					// Is this word in the dictionary
					if ( !this.dictionary.containsKey(words[i]) ) {
						
						Node node = new Node();
						this.dictionary.put(words[i], node);
						
					}
					
					// Get node for word
					Node node = this.dictionary.get(words[i]);
					
					// add term to positional index for document
					node.addTermPosition(file.getName(), position);
					position++;
				}
				
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public int termFrequency(String term, String doc) {	
		
		return this.dictionary.get(term).getTermFrequency(doc);
	}

	public int docFrequency(String term) {
		
		return this.dictionary.get(term).getDocumentFreq();
	}

	public String postingsList(String term) {
		return this.dictionary.get(term).getPostingsList();
	}
	
	public double weight(String term, String doc) {
		
		int docFreq;
		
		// if this term does not appear in the dictionary return 0
		if (this.dictionary.get(term) == null) { return 0; }
		else { docFreq = this.dictionary.get(term).getDocumentFreq(); }
	
		double N = this.numDocs;
		return (log2(1+termFrequency(term,doc))*Math.log10(N/docFreq));
		
	}
	
	private double log2(int i) {
		
		if (i == 1) return 0;
		return Math.log(i)/Math.log(2);
	}

	public double TPScore(String query, String doc) {
		
		String[] terms = cleanQuery(query);
		return calcTPScore(terms,doc);
	}

	public double VSScore(String query, String doc) {
		
		String[] terms = cleanQuery(query);
		return calcVSScore(terms,doc);
		
	}
	
	private double calcVSScore(String[] terms, String doc) {
		
		int termFreq = 0;
		
		// if the query terms don't appear in doc the dot product 
		// will be zero, no need to construct document vectors
		for (String term : terms) {
			
			if ( !this.dictionary.containsKey(term) ) { termFreq += 0; }
			else { termFreq += this.dictionary.get(term).getTermFrequency(doc); }
		}
		
		if ( termFreq == 0 ) { return 0; }
		
		// Non-zero dot product create vectors
		Hashtable<String,Double> queryWeights = queryWeightVector(terms);
		Hashtable<String,Double> docWeights = docWeightVector(doc);
		
		
		double dotProd = dotProduct(docWeights,queryWeights);
		double normProd = normProduct(docWeights,queryWeights);
		if (normProd == 0) { return 0; }
		return dotProd/normProd;
	}


	private double normProduct(Hashtable<String, Double> docWeights, Hashtable<String, Double> queryWeights) {
		
		double docNorm = 0;
		double queryNorm = 0;
		
		// Sum of squared weights
		for (String key : queryWeights.keySet()) {
			queryNorm += queryWeights.get(key)*queryWeights.get(key);
		}
		for (String key : docWeights.keySet()) {
			docNorm += docWeights.get(key)*docWeights.get(key);
		}
		
		// Sqrt of squared weights for L2 norm
		return Math.sqrt(docNorm)*Math.sqrt(queryNorm);
	}


	private double dotProduct(Hashtable<String, Double> docWeights, Hashtable<String, Double> queryWeights) {
		
		double dotProd = 0;
		
		for (String key : queryWeights.keySet()) {
			
			if (docWeights.containsKey(key)) {
				dotProd += queryWeights.get(key)*docWeights.get(key);
			}
		}
		return dotProd;
	}


	private Hashtable<String, Double> queryWeightVector(String[] terms) {
		
		Hashtable<String,Double> queryWeights = new Hashtable<String,Double>();
		
		// check for query term appearing multiple times
		for (int i=0; i<terms.length; i++) {
			int freqInQuery = 1;
			
			for (int j=i+1; j<terms.length;j++) {
				if (terms[i].equals(terms[j])) { freqInQuery++; }
			}
			
			double weight;
			
			// if term does not appear in dictionary give it zero weight
			if (this.dictionary.get(terms[i]) == null) { weight = 0.0; }
			else { weight = log2(1+freqInQuery)*
					Math.log10(((double) this.numDocs)/this.dictionary.get(terms[i]).getDocumentFreq()); 
			}
			
			if (!queryWeights.containsKey(terms[i])) { queryWeights.put(terms[i], weight); }
		}
		
		return queryWeights;
	}


	private Hashtable<String, Double> docWeightVector(String doc) {
		
		
		File file = new File(this.directory,doc);
		Hashtable<String,Double> weights = new Hashtable<String,Double>();
		
		// parse document to create document term vector
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			String line;
			String[] words;
		
			while ((line = reader.readLine()) != null ) {
				
				line = line.toLowerCase();
				line = line.replaceAll("[,\"?'{}:;()\\]\\[]","");
				words = line.split("\\s+");
				
				
				for (int i=0; i<words.length; i++) {
					
					// Check for periods in non-floating point numbers
					if ( words[i].contains(".") ) {
						
						try { 
							double check = Double.parseDouble(words[i]); 
							}
						catch (NumberFormatException e) {
							words[i] = words[i].replaceAll("\\.", "");
						}
						
					}
					
					// Do we have a weight for this word?
					if (!weights.containsKey(words[i]))  {
						weights.put(words[i],weight(words[i],doc));
					}
					
				}
		
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return weights;
	}
	
	
	private double calcTPScore(String[] terms, String doc) {
		
		int l = terms.length;
		
		// definition of TPScore
		if (l == 1) { return 0; }
		
		int distance = 0;
		String term1,term2;
		
		for (int i=0; i<terms.length-1; i++) {
			
			term1 = terms[i];
			term2 = terms[i+1];
			
			ArrayList<Integer> position1, position2;
			
			// if a term is not in dictionary or not in doc
			// return 17
			if(!dictionary.containsKey(term1) || !dictionary.containsKey(term2)) {
				distance += 17;
			} 
			else if ( (position1 = dictionary.get(term1).getPositions(doc)) == null ||
					(position2 = dictionary.get(term2).getPositions(doc)) == null) {
				
				distance += 17;
			}
			else {
				
				distance += termDist(position1,position2);
			}
			
			
		}
		return ((double) l)/distance;
	}

	
	private int termDist(ArrayList<Integer> position1, ArrayList<Integer> position2) {
		
		int minDist = 17;
		int index2 = 0;
		int pos2 = position2.get(index2);
		
		for (int j=0; j<position1.size(); j++) {
			
			int pos1 = position1.get(j);
			
			while (pos1 > pos2) {
				
				index2++;
				if (index2 == position2.size()) { return minDist; }
				pos2 = position2.get(index2);
			}
			
			minDist = Math.min(minDist, pos2-pos1);
			
		}
		return minDist;
	}
	
	
	private String[] cleanQuery(String query) {
		
		// process query the same way we 
		// processed the document
		query = query.toLowerCase();
		query = query.replaceAll("[,\"?'{}:;()\\]\\[]","");
		String[] words = query.split("\\s+");
		
		
		for (int i=0; i<words.length; i++) {
			
			// Check for periods in non-floating point numbers
			if ( words[i].contains(".") ) {
				
				try { 
					double check = Double.parseDouble(words[i]); 
					}
				catch (NumberFormatException e) {
					words[i] = words[i].replaceAll("\\.", "");
				}
				
			}
		}
		
		return words;
	}

	
	public double Relevance(String query, String doc) {
		
		return 0.6*TPScore(query,doc) + 0.4*VSScore(query,doc);
		
	}

	
	public ArrayList<RelevanceScore> topKRelevant(String query, int k) {
		
		File files[] = this.directory.listFiles();
		
		// store scores in a tree set so they are
		// added in sorted order
		TreeSet<RelevanceScore> rScores = new TreeSet<RelevanceScore>(new RelevanceScoreComparator());
		
		for (int i=0; i<files.length; i++) {
			
			RelevanceScore score = new RelevanceScore(files[i].getName(),Relevance(query,files[i].getName()));

			// don't let tree grow larger than k
			if (i>k) { 
				double min = rScores.first().getScore(); 
				if ( min < score.getScore() ) { 			
					rScores.pollFirst();
					rScores.add(score); 		
				} 
			} else { rScores.add(score); }
			
		}
		
		ArrayList<RelevanceScore> topK = new ArrayList<RelevanceScore>();
		
		// poll largest score
		// get TPScore and VSScore (re-calculated because of restrictions on the method: Relevance)
		for (int j=0; j<k; j++) {
			
			RelevanceScore temp = rScores.pollLast();
		//	temp.setTpscore(TPScore(query,temp.getDocument()));
		//	temp.setVsscore(VSScore(query,temp.getDocument()));
			topK.add(temp);
			
		}
		
		return topK;
	}
	
}
