package coms535.pa1;

import java.util.Arrays;
import java.util.BitSet;

public class BloomFilterDet {
	
	private int k;
	private int numBins;
	private BitSet bins;
	private int dataSize = 0;
	private long FNV64PRIME = Long.parseUnsignedLong("1099511628211");
	private long FNV64INIT = Long.parseUnsignedLong("14695981039346656037");
	
	BloomFilterDet(int setSize, int bitsPerElement)
	{
		fillPrimes(setSize*bitsPerElement);
		numBins = largerPrime(setSize*bitsPerElement);
		bins = new BitSet(numBins);
		k = (int) Math.round(Math.log(2)*numBins/setSize);

	}
	

	public void add(String s)
	{
		dataSize +=1;
		s = s.toLowerCase();
		int hashResult;
		for(int i=0;i<k;i++)
		{
		
			hashResult = hashVal(s,i);
			
			bins.set(hashResult);
		}
	}
	
	public int filterSize()
	{
		return numBins;
	}
	
	public int dataSize()
	{
		return dataSize;
	}
	
	public int numHashes()
	{
		return k;
	}
	
	public boolean appears(String s)
	{
		s = s.toLowerCase();
		int hashResult;
		for(int i=0;i<k;i++)
		{
			hashResult = hashVal(s,i);
			if(!bins.get(hashResult)) return false;
		}
		return true;
	}
	
	private int hashVal(String s, int functionNumber)
	{
		long func = FNV64INIT;
		char[] charS = s.toCharArray();
		for (char c : charS)
		{
			func ^= c;
			func = (long) ((long) func*FNV64PRIME%Math.pow(2, 64));
					
		}
		int hashOne;
		int hashTwo;
		long temp;
		
		temp = (func>>32);
		hashOne = (int) temp;
		hashTwo = (int) func;
		
		int hashReturn = (hashOne + (functionNumber*hashTwo));
		if (hashReturn < 0) hashReturn = hashReturn*-1;
		
		return (hashReturn%numBins);
	}
	
	private int largerPrime(int n)
	{
		boolean[] primes = fillPrimes(n);
		
		for(int i=n+1; i<primes.length;i++)
		{
			if(primes[i]) return i;
		}
		
		return 0;
	}
	
	private boolean[] fillPrimes(int n)
	{
		boolean[] primes = new boolean[2*n];
		Arrays.fill(primes, true);
		primes[0] = false;
		primes[1] = false;
		for (int i=2; i<primes.length;i++)
		{
			if (primes[i])
			{
				for(int j=2; i*j<primes.length;j++)
				{
					primes[i*j] = false;
				}
			}
		}
		return primes;
	}
}

