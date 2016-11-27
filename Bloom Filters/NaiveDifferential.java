package coms535.pa1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NaiveDifferential {

	private static String DiffFile = "DiffFile.txt";
	private static String Database = "database.txt";
	
	public NaiveDifferential() {
		
	}
	
	
	public static String retrieveRecord(String key) throws FileNotFoundException
	{

		String recordFromDiff = checkRecords(key, new File(DiffFile));
		if (!recordFromDiff.equals("")) return recordFromDiff;
		
		String recordFromDatabase = checkRecords(key, new File(Database));
		if (!recordFromDatabase.equals("")) return recordFromDatabase;
		
		return null;
	}

	/**
	 * @param key
	 * @return
	 * @throws FileNotFoundException
	 */
	private static String checkRecords(String key, File file) throws FileNotFoundException {
		
		if (key == null){
			return "";
		}
		
		int j = 0;
		String phrase;
		String record; 
		String currentWord;
		Scanner recordScanner = null;
		
		//Open Scanner for file
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine())
		{
			
			//Get full record
		    record = scanner.nextLine();
		    
		    
		    //Scan record for 4-word phrase
		    recordScanner = new Scanner(record);
		    phrase = "";
		    j = 0;
		    while(recordScanner.hasNext() || j==4)
		    {
		    	
		    	//Add word to phrase or check if phrase == key
		    	if(j>3)
		    	{
		    		if(phrase.equals(key))
		    		{
		    			scanner.close();
		    			recordScanner.close();
		    			return record;
		    		}
		    		break;
		    	}
		    	
		    	currentWord = recordScanner.next();
		    	phrase+= currentWord;
		    	if (j<3) phrase+=" ";
		    	j++;
		    }

		}
		
		//Phrase not found in file, return ""
		recordScanner.close();
		scanner.close();
		return "";
	}
}
