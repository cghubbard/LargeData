package pa3;

import java.util.HashSet;

public class WikiTennisRanker {

	public static void main(String[] args) {
		
		double eta = 0.01;
		int k = 10;
		pageRankStats(eta, k);
		
		eta = 0.005;
		pageRankStats(eta, k);
		
		eta = 0.01;
		k = 100;
		jaccardSims(eta,k);
		
		eta = 0.005;
		jaccardSims(eta,k);
		
	}

	private static void pageRankStats(double eta, int k) {
		
		PageRank test = new PageRank("wikiTennis.txt",eta);
		String[] topRank = test.topKPageRank(k);
		String[] topIn = test.topKInDegree(k);
		String[] topOut = test.topKOutDegree(k);
		System.out.println("-----------------");
		System.out.println("eta: " + eta);
		System.out.println("-----------------");
		System.out.println("-----------------");
		System.out.println("top rank");
		System.out.println("-----------------");
		for (String t : topRank) {
			System.out.println(t + "\t" + test.pageRankOf(t));
		}
		System.out.println("-----------------");
		System.out.println("top in degree");
		System.out.println("-----------------");
		for (String t : topIn) {
			System.out.println(t + "\t" + test.inDegreeOf(t));
		}
		System.out.println("-----------------");
		System.out.println("top out degree");
		System.out.println("-----------------");
		for (String t : topOut) {
			System.out.println(t + "\t" + test.outDegreeOf(t));
		}
		System.out.println("-----------------");
		System.out.println("number of iterations");
		System.out.println("-----------------");
		System.out.println(test.getIterations());
		System.out.println("-----------------");
		System.out.println("-----------------");

	}

	
	private static void jaccardSims(double eta, int k) {
		
		PageRank test = new PageRank("wikiTennis.txt",eta);
		String[] topRank = test.topKPageRank(k);
		String[] topIn = test.topKInDegree(k);
		String[] topOut = test.topKOutDegree(k);
		
		double simAB = jacSim(topIn,topOut);
		double simBC = jacSim(topRank,topOut);
		double simAC = jacSim(topIn,topRank);
		
		System.out.println("-----------------");
		System.out.println("eta: "+eta);
		System.out.println("-----------------");
		System.out.println("-----------------");
		System.out.println("Jac Sim: A,B");
		System.out.println(simAB);
		System.out.println("-----------------");
		System.out.println("Jac Sim: B,C");
		System.out.println(simBC);
		System.out.println("-----------------");
		System.out.println("Jac Sim: A,C");
		System.out.println(simAC);
		System.out.println("-----------------");
		
		
	}

	private static double jacSim(String[] set1, String[] set2) {
		
		HashSet<String> setA = new HashSet<String>(set1.length);
		HashSet<String> setB = new HashSet<String>(set2.length);
		
		for (int i=0; i<set1.length; i++) {
			setA.add(set1[i]);
			setB.add(set2[i]);
		}
		
		HashSet<String> intersection = new HashSet<String>(setA);
		intersection.retainAll(setB);
		
		HashSet<String> union = new HashSet<String>(setA);
		union.addAll(setB);
		
		return ((double)intersection.size()/union.size());
	}
}
