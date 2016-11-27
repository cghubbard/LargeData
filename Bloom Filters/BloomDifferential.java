package coms535.pa1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BloomDifferential {
	
	private BloomFilterDet bloomFilter;
	private File differentialFile;
	private int differentialFileSize;
	private int bitsPerElement;
    private File databaseFile;

	public BloomDifferential(int bitsPerElement,String differentialFileName, String databaseFile) {
		this.bitsPerElement = bitsPerElement;
		this.differentialFile = new File(differentialFileName);
		this.databaseFile = new File(databaseFile);
		try {
			setFileSize(this.differentialFile);
			createFilter();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void setFileSize(File file) throws FileNotFoundException
	{
		int count = 0;
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine())
		{
			count++;
			scanner.nextLine();
		}
		scanner.close();
		differentialFileSize = count;
		
		
	}

	
	private void createFilter() throws FileNotFoundException
	{
		bloomFilter = new BloomFilterDet(differentialFileSize,bitsPerElement);
		
		Scanner scanner = new Scanner(differentialFile); 
		while(scanner.hasNextLine())
		{
		    String line = scanner.nextLine();
		    Scanner scanline = new Scanner(line);
		    String key = "";
		    int count = 0;
		    while(scanline.hasNext())
		    {
		    	
		    	if(count == 3)
		    	{
		    		if(!key.isEmpty())
		    		{
		    			key+= scanline.next();
		    			scanline.close();
		    			bloomFilter.add(key.trim());
		    		}
		    		break;
		    	}
		    	count++;
		    	key+= scanline.next()+" ";
		    	
		    }
		}
		scanner.close();
	}
	
	public String retrieveRecord(String key) throws FileNotFoundException
	{
		if(key == null)
		{
		  return null;
		}
		if(bloomFilter.appears(key))
		{
			String result = get(key,differentialFile);
			if(result != null)
			{
				return result;
			}
			else
			{
				return get(key, databaseFile);
			}
		}
		else
		{
			return get(key, databaseFile);              
		}
		
	}
	
	private String get(String key, File file) throws FileNotFoundException
	{
		if(key == null)
		{
			return null;
		}
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine())
		{
		    String line = scanner.nextLine();
		    Scanner scanline = new Scanner(line);
		    String name = "";
		    int count = 0;
		    
		    while(scanline.hasNext())
		    {
		    	if (count == 3)
		    	{
		    		name+= scanline.next();
		    		scanline.close();
		    		if(name.trim().equals(key))
		    		{
		    			return line;
		    		}
		    		  break;
		    	}
		    	count++;
		    	name+= scanline.next()+" ";
		    	
		    }  
		    
		}
		scanner.close();
		return null;
	}

}
