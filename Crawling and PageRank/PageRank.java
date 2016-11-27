package pa3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

public class PageRank {
	
	private ArrayList<ArrayList<String>> edges;
	private Hashtable<String,Integer> vertexIndex;
	private double[] probVector;
	private int numVertices;
	private int numEdges;
	private int[] inDegree;
	private int iterations;
	

	PageRank(String fileName, double eta) {
		
		 getGraph(fileName);
		 computePageRank(eta);
		
	}

	private void computePageRank(double eta) {
		
		if (eta <= 0) { eta = 0.01; }
		double beta = 0.85;
		boolean converged = false;
		double[] probVectorNext = new double[this.numVertices];
		
		int linkIndex;
		int outDegree;

		int iterations = 0;
		while (!converged) {
			
			Arrays.fill(probVectorNext, (1-beta)/this.numVertices);
			
			for (int i=0; i<this.numVertices ; i++) {
			
				outDegree = this.edges.get(i).size();
				
				if ( outDegree == 0 ) {
					for (int j=0; j<this.numVertices; j++) {
						probVectorNext[j] += beta*this.probVector[i]/this.numVertices;
					}
				} else {
					for (String link : this.edges.get(i)) {
						linkIndex = vertexIndex.get(link);
						probVectorNext[linkIndex] += beta*this.probVector[i]/outDegree;
					}	
				}
			}
			
			
			double normDiff = differenceNorm(probVectorNext);
			this.probVector = new double[this.numVertices]; 
			System.arraycopy(probVectorNext, 0, this.probVector, 0, this.numVertices);
			iterations++;
			if ( normDiff <= eta ) { this.iterations = iterations; return; }
	
		}
		
		
	}
	private double differenceNorm(double[] vec) {
		
		double sum = 0;
		double temp;
		for (int i=0; i<vec.length; i++) { 
			
			temp = vec[i] - this.probVector[i];
			sum += temp*temp;
		}
		
		return Math.sqrt(sum);
		
	}
	private void getGraph(String fileName) {
		
		try {
			
			FileReader document = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(document);
			
			String line;
			String[] vertices;
			boolean firstLine = true;
			
			int outIndex;
			int inIndex;
			this.numEdges = 0;
			
			while ((line = reader.readLine()) != null) {
				
				if (firstLine) {
					
					int numVerts = Integer.parseInt(line);
					initFields(numVerts);
					
					firstLine = false;
				} else {
				
					vertices = line.split("\\s+");
					
					if (!this.vertexIndex.containsKey(vertices[0])) {
						
						outIndex = this.vertexIndex.size();
						this.vertexIndex.put(vertices[0], outIndex);
						
					} else { outIndex = vertexIndex.get(vertices[0]); }
					
					if (!this.vertexIndex.containsKey(vertices[1])) {
						
						inIndex = this.vertexIndex.size();
						this.vertexIndex.put(vertices[1], inIndex);
						
					} else { inIndex = vertexIndex.get(vertices[1]); }
					
					this.edges.get(outIndex).add(vertices[1]);
					this.inDegree[inIndex]++;
					this.numEdges++;
				}
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initFields(int numVerts) {
		
		this.numVertices = numVerts;
		this.vertexIndex = new Hashtable(numVerts);
		this.probVector = new double[numVerts];
		Arrays.fill(this.probVector, 1/(double) numVerts);
		
		this.edges = new ArrayList<ArrayList<String>>(numVerts);
		for (int i=0;i<numVerts;i++) { edges.add(new ArrayList<String>()); }
		this.inDegree = new int[numVerts];
	}
		
	public double pageRankOf(String vertex) {
		
		int index = this.vertexIndex.get(vertex);
		return this.probVector[index];
		
	}
	
	public int outDegreeOf(String vertex) {
		
		int index = this.vertexIndex.get(vertex);
		return this.edges.get(index).size();
	}
	
	public int inDegreeOf(String vertex) {
		
		int index = this.vertexIndex.get(vertex);
		return this.inDegree[index];
		
	}
	
	public int numEdges() {
		
		return this.numEdges;
		
	}
	
	
	public String[] topKInDegree(int k) {
		
		double[] topIns = new double[k];
		int[] indicies = new int[k];
		
		for (int i=0; i<k; i++) {
			topIns[i] = this.inDegree[i];
			indicies[i] = i;
		}
		int minIndex = getMinIndex(topIns);
		
		for (int i=k; i<this.numVertices; i++) {
			
			if (this.inDegree[i] > topIns[minIndex]) {
				
				topIns[minIndex] = this.inDegree[i];
				indicies[minIndex] = i;
				minIndex = getMinIndex(topIns);
			}
			
		}
		
		String[] verts = reverseLookup(indicies, k);
		
		return verts;
	}
	
	public String[] topKPageRank(int k) {
		
		double[] topRanks = new double[k];
		int[] indicies = new int[k];
		
		for (int i=0; i<k; i++) {
			topRanks[i] = this.probVector[i];
			indicies[i] = i;
		}
		int minIndex = getMinIndex(topRanks);
		
		for (int i=k; i<this.numVertices; i++) {
			
			if (this.probVector[i] > topRanks[minIndex]) {
				
				topRanks[minIndex] = this.probVector[i];
				indicies[minIndex] = i;
				minIndex = getMinIndex(topRanks);
			}
			
		}
		
		String[] verts = reverseLookup(indicies, k);
		
		return verts;
	}
	
	public String[] topKOutDegree(int k) {
		
		double[] topOuts = new double[k];
		int[] indicies = new int[k];
		
		for (int i=0; i<k; i++) {
			topOuts[i] = this.edges.get(i).size();
			indicies[i] = i;
		}
		int minIndex = getMinIndex(topOuts);
		
		for (int i=k; i<this.numVertices; i++) {
			
			if (this.edges.get(i).size() > topOuts[minIndex]) {
				topOuts[minIndex] = this.edges.get(i).size();
				indicies[minIndex] = i;
				minIndex = getMinIndex(topOuts);
			}
			
		}
		
		String[] verts = reverseLookup(indicies, k);

		return verts;
	}
	
	private String[] reverseLookup(int[] indicies,int k) {
		
		String[] verts = new String[k];
		for (Map.Entry<String,Integer> entry : this.vertexIndex.entrySet()) {
			for (int i=0; i<indicies.length; i++) {
				if (entry.getValue() == indicies[i]) { verts[i] = entry.getKey(); }
			}
		}
		
		for (int tst : indicies) {
			//System.out.println(tst);
		}
		return verts;
	}
	
	private int getMinIndex(double[] tops) {
		
		double min = 1000000000;
		int index = 0;
		for (int i=0; i<tops.length; i++) {
			
			if(tops[i] < min) {
				min = tops[i];
				index = i;
			}
			
		}
		
		return index;
	}

	public int getIterations() {
		return this.iterations;
	}
}
