package pa3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class WikiCrawler {

	static final String BASE_URL = "https://en.wikipedia.org";
	private Weighted_Q vertQueue;
	private String startLocation;
	private int maxVertices;
	private HashSet<String> visited;
	private HashSet<String> allVertices;
	private String fileName;
	private String[] keywords;
	private boolean isWeighted;

	// Initialize the crawler class variables
	public WikiCrawler(String seed, String[] keywords, int max, String fileName, boolean isWeighted) {

		// Initialize class variables
		this.startLocation = seed;
		this.maxVertices = max;
		this.visited = new HashSet<String>(maxVertices);
		this.allVertices = new HashSet<String>(maxVertices);
		handleKeywords(keywords);
	
		//this.keywords = keywords;
		this.fileName = fileName;
		this.isWeighted = isWeighted;
		
		// Add seed to list of vertices
		this.allVertices.add(seed);
		
		//Initialize queue, add seed
		this.vertQueue = new Weighted_Q(isWeighted);
		this.vertQueue.add(new Tuple(this.startLocation,0));
		
	
	}
	
	// splits phrases into separate words, adds to this.keywords
	private void handleKeywords(String[] keys) {
		
		ArrayList<String> keyWord = new ArrayList<String>();
		for (String key : keys) {
			
			String[] splitKey = key.split("\\s+");
			
			for (String split : splitKey) {
				keyWord.add(split);
			}
			
		}
		
		this.keywords = new String[keyWord.size()];
		keyWord.toArray(this.keywords);
	}
	
	// Crawls web pages!
	public void crawl() throws IOException {

		// read robots.txt
		HashSet<String> disallowed = new HashSet<String>();
		getRobotRules(disallowed);
	
		// initialize output file
		File outputFile = new File(fileName);
		outputFile.createNewFile();
		FileWriter writer = new FileWriter(outputFile);
		writer.write(maxVertices + "\n");

		
		String page;
		String[] pageLinks = null;

		for (int i = 0; i < maxVertices; i++) {
			
			// is our queue empty?
			if (vertQueue.isEmpty()) {
				writer.flush();
				writer.close();
				
				return;
			}

			// extract until finding a link we haven't visited
			while (visited.contains(page = vertQueue.extract().getLink())) {

				if (vertQueue.isEmpty()) {
					writer.flush();
					writer.close();
					
					return;
				}
			}
			
			// check if seed is legal
			if (i==0) {
				boolean linkOk = true;
				for (String banned : disallowed) {
					
					if (0 == page.indexOf(banned)) { linkOk = false; }
					
				}
				
				if (!linkOk) {
					
					System.out.println("seed disallowed by robots.txt");
					writer.flush();
					writer.close();
					
					return;
				}
			}
			
			
			// visit page
			visited.add(page);
			pageLinks = extractLinks(getDoc(page),page,disallowed);

			// write edges to file
			for (String str : pageLinks) {

				if (str == null) { break; }
				writer.write(page + " " + str + "\n");
				
			}
			
			// pause for a second every 10 pages
			if (i % 10 == 0)
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}

		writer.flush();
		writer.close();

	}
	
	// Read robots.txt to determine which sites we are not
	// allowed to crawl.
	private void getRobotRules(HashSet<String> disallowed) {
		
		// get html
		URL url;
		try {
			url = new URL(BASE_URL +"/robots.txt");
		 
		BufferedReader URLin = new BufferedReader(new InputStreamReader(url.openStream()));
		String lineIn;
		
		boolean userAgent_star = false;
		while ((lineIn = URLin.readLine()) != null) {
		
			if (userAgent_star){
				
				String[] text = lineIn.split("\\s+");
				if(text[0].equals("Disallow:")) { disallowed.add(text[1]); }
				
		
			}
			else if ( lineIn.contains("User-agent: *") && !userAgent_star) {
				userAgent_star = true;
			}
			
		}
		
		
		}
		catch (IOException e) {
			System.out.println("couldn't load " +this.BASE_URL+ "/robots.txt");
		}
	}
	
	// Build HTML doc
	private static String[] getDoc(String page) {

		// get html
		URL url;
		ArrayList<String> validHTML = new ArrayList<String>();
		try {
			url = new URL(BASE_URL + page);
		
			BufferedReader URLin = new BufferedReader(new InputStreamReader(url.openStream()));
			String lineIn;
		
	
			// split on white spaces
				// store after tag
			boolean tagOccured = false;
			while ((lineIn = URLin.readLine()) != null) {
			
				if (tagOccured){
					
					for (String text : lineIn.split("\\s+")) { validHTML.add(text);} 
					
			
				}
				else if ( lineIn.contains("<p>") && !tagOccured) {
					
					int tagIndex = lineIn.indexOf("<p>");
					String[] postTag = lineIn.substring(tagIndex+3, lineIn.length()).split("\\s+");
					for (String text : postTag) { validHTML.add(text); }
					tagOccured = true;
				}
				
		}
		} catch (IOException  e) {
			System.out.println("couldn't load "+BASE_URL+page);
		}
		// return string array
		String[] toReturn = new String[validHTML.size()];
		validHTML.toArray(toReturn);
		return toReturn;
		
	}
	
	// Get only valid links from our HTML doc
	public String[] extractLinks(String[] doc, String page, HashSet<String> disallowed) {
		
		

		ArrayList<String> edges = new ArrayList<String>();
		
		String validLink;
		for (int i=0; i<doc.length;i++) {
			
			if (doc[i].contains("href=\"/wiki/") && !doc[i].contains(":") && !doc[i].contains("#")) {
						
						// trim link
						validLink = doc[i];
						validLink = validLink.substring(6, validLink.length()-1);
						
						// is link disallowed by robots.txt?
						boolean linkOk = true;
						for (String banned : disallowed) {
							
							if (0 == validLink.indexOf(banned)) { linkOk = false; }
							
						}
						
						// have we reached the vertex limit?
						// is this in our set of vertices
						boolean linkInGraph = false;
						if (this.allVertices.size()<this.maxVertices && !this.allVertices.contains(validLink) && linkOk) {
							this.allVertices.add(validLink);
							linkInGraph = true;
						}
						else if (this.allVertices.contains(validLink) && linkOk) {
							linkInGraph = true;
						}
						
						// can we crawl this link?  
						// have we already?
						float weight = 0;
						if (!visited.contains(validLink) && linkInGraph) {
						
							if (this.isWeighted) { weight = getWeight(doc, i); }
							else { weight = 0; }
					
							vertQueue.add(new Tuple(validLink,weight)); 
							
		
						}
						
						// have we recored this edge? is this a vertex in our graph?  is this a self loop?
						if (!edges.contains(validLink) && linkInGraph && !validLink.equals(page)) { edges.add(validLink); }
			
			}
	
		}
		

		String[] returnEdges = new String[edges.size()];
		edges.toArray(returnEdges);
		return returnEdges;
		
	}
	
	// compute the weight of the link at index
	private float getWeight(String[] doc, int index) {
		
		
		// check link text and anchor tag
		int anchorTagIndex = index;
		do { anchorTagIndex++; }
		while(!doc[anchorTagIndex].contains("</a>"));
		
		for(int i=index; i<=anchorTagIndex; i++) {
			
			for (String keyword : this.keywords) {
				if(doc[i].toLowerCase().contains(keyword.toLowerCase())) { return 1; }
			}
		}
			
		
		// check 20 before
		int distance = 21;
		int currentIndex = 0;
		
		checkbefore:
		for (int i=1;i<21;i++) {
			
			// skip "<a" tag;
			currentIndex = index-1-i;
			if (currentIndex < 0) { break; }
			for (String keyword : this.keywords) {
				
				if(doc[currentIndex].toLowerCase().contains(keyword.toLowerCase())) { 
					
					distance = i; 
					break checkbefore; 
					
				}
				
			}
		}
		
		int twentyBefore = distance;
		
		// check 20 after anchor tag	
		distance = 21;
		
		checkafter:
		for (int i=1;i<21;i++) {
		
			currentIndex = anchorTagIndex+i;
			if (currentIndex > (doc.length-1)) { break; }
			for (String keyword : this.keywords) {
				
				if(doc[currentIndex].toLowerCase().contains(keyword.toLowerCase())) { 
					
					distance = i; 
					break checkafter; 
					
				}
				
			}
		}
		
		int twentyAfter = distance;
		
		int minimumDistance = Math.min(twentyAfter, twentyBefore);
		
		if (minimumDistance > 20) { return 0; }
		else { return (1/((float)minimumDistance + 2)); }
		
	}
}

