package coms535.pa1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class FalsePositives {

	static String[] dictionary;
	
	public static void main(String[] args) {
		double[][] detErrorRates = new double[100][3];
		double[][] ranErrorRates = new double[100][3];
		double[] errorRates = new double[2];
		int[] bits = {4,8,10};
		
		generateDict();
		
		for(int i=0; i<100; i++) {
			
			errorRates = testBlooms(4);
			detErrorRates[i][0] = errorRates[0];
			ranErrorRates[i][0] = errorRates[1];
			
			errorRates = testBlooms(8);
			detErrorRates[i][1] = errorRates[0];
			ranErrorRates[i][1] = errorRates[1];
			
			errorRates = testBlooms(10);
			detErrorRates[i][2] = errorRates[0];
			ranErrorRates[i][2] = errorRates[1];
			
		}
		
		double detSum;
		double ranSum;
		
		for(int i=0; i<3; i++){
			
			detSum = 0;
			ranSum = 0;
			
			for(int j=0; j<100; j++){
				
				 detSum += detErrorRates[j][i];
				 ranSum += ranErrorRates[j][i];
				
			}
			
			
			System.out.println("The "+bits[i]+"-bit determinist error rate is: "+ (double)(detSum/100));
			System.out.println("The "+bits[i]+"-bit random error rate is: "+ (double)(ranSum/100));
		}
		
	}

	private static double[] testBlooms(int n) {
		
		BloomFilterDet bloomDet = new BloomFilterDet(dictionary.length,n);
		BloomFilterRan bloomRan = new BloomFilterRan(dictionary.length,n);
		int bDetErrors = 0;
		int bRanErrors = 0;
		Random rand = new Random();
		
		int dictLength = 0;
		for(String word : dictionary)
		{

			bloomDet.add(word);
			bloomRan.add(word);
			dictLength += 1;
		}
		
		for(String word : dictionary)
		{
			word = word + Integer.toString(rand.nextInt(3000000));
			if (bloomDet.appears(word)) bDetErrors += 1;
			if (bloomRan.appears(word)) bRanErrors += 1;
		}
		
		
		double detErrorRate = (double) bDetErrors/dictLength;
		double ranErrorRate = (double) bRanErrors/dictLength;

		
		double[] errorRates = {detErrorRate,ranErrorRate};
		
		return errorRates;
	}

	private static void generateDict() {
		dictionary = new String[235886];
		
		int i = 0;
		try (Scanner fileScanner = new Scanner(new File("web2"))){
			while (fileScanner.hasNextLine() && i<235886) {
				dictionary[i] = fileScanner.nextLine();
				i++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
