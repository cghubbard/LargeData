package coms535.pa1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Scanner;

public class GetTestRecords {

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		
		BloomDifferential bloomDiff = new BloomDifferential(4,"DiffFile.txt","DiffFile.txt");
		int[] rands = new int[100];
		File allRecords = new File("grams.txt");
		String phrase = null;
		
		for(int i=0;i<100;i++) {
			
			rands[i] = (int)(Math.random()*1262148);
			
		}
		
		Arrays.sort(rands);
		
		String record;
		String[] phraseSplit;
		String shorten;
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream("indiff.txt"), "utf-8"))) {
			
			try( Scanner scanner = new Scanner(allRecords)) { 
				
				int i=0;
				int j=0;
				
				//For a random 1000 phrases in grams.txt record time it takes NaiveDiff to find record
				while (scanner.hasNextLine()) {
					
					
					phrase = scanner.nextLine();
					phraseSplit = phrase.split(" ");
					shorten = phraseSplit[0]+" "+phraseSplit[1]+" "+phraseSplit[2]+" "+phraseSplit[3];
					if (i == rands[j]) {
	
						
				
							writer.write(shorten+"\n");
							j++;
							System.out.println(j);
		
						
					}
					if (j>99) break;
					i++;
		
				}

				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
			
		
		
		

	}

}
