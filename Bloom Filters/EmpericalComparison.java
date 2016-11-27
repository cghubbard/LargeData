package coms535.pa1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class EmpericalComparison {

	public static void main(String[] args) {
		
		BloomDifferential bloomDiff = new BloomDifferential(4,"DiffFile.txt","database.txt");
		NaiveDifferential naiveDiff = new NaiveDifferential();
		

		long[] resultNaive = {0,0};
		long[] resultBloom = {0,0};
		
		File[] recordsFiles = {new File("indiff.txt"), new File("notindiff.txt")};
		String phrase = null;
		String record = null;
        Long start;
		Long stop;
		Long naiveSearchTime = (long) 0;
		Long bloomSearchTime = (long) 0;
		
		int k = 0;
		for (File file : recordsFiles) {
			
			try( Scanner scanner = new Scanner(file)) { 
			
			int i=0;
			
			//For 100 phrases in indiff.txt and notindiff.txt record time it takes NaiveDiff and BloomDiff to find record
			while (scanner.hasNextLine()) {
				
				phrase = scanner.nextLine();
				
				start = System.currentTimeMillis();
				record = naiveDiff.retrieveRecord(phrase);
				stop = System.currentTimeMillis();
					
				naiveSearchTime += stop-start;
					
					
				start = System.currentTimeMillis();
				record = bloomDiff.retrieveRecord(phrase);
				stop = System.currentTimeMillis();
							
				bloomSearchTime += stop-start;
				i++;
				
				System.out.println(i);
				
			}
			
			resultNaive[k] = naiveSearchTime/i;  
			resultBloom[k] = bloomSearchTime/i;
			
			} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			
			k++;
		
		}
		
		System.out.println("Files in differential: ");
		System.out.println("Naive average time: "+ resultNaive[0]);
		System.out.println("Bloom average time: "+ resultBloom[0]);
		System.out.println();
		System.out.println("Files in database: ");
		System.out.println("Naive average time: "+ resultNaive[1]);
		System.out.println("Bloom average time: "+ resultBloom[1]);	
		
	}
}


