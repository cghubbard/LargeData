package coms535.pa1;

import java.util.BitSet;
import java.util.Random;
import java.util.Arrays;

public class BloomFilterRan {
	
	private int k;
	private int[][] abHashStore;
	private int numBins;
	private BitSet bins;
	private int dataSize = 0;
	
	BloomFilterRan(int setSize, int bitsPerElement)
	{
		fillPrimes(setSize*bitsPerElement);
		numBins = largerPrime(setSize*bitsPerElement);
		bins = new BitSet(numBins);
		k = (int) Math.round(Math.log(2)*numBins/setSize);
		abHashStore = new int[k][2];
		setHashFunctions(k);
		
	}
	
	private void setHashFunctions(int k) 
	{
		
		int A;
		int B;
		Random rand = new Random();
		
		for(int i=0;i<k;i++)
		{
			A = rand.nextInt(numBins);
			B = rand.nextInt(numBins);
			abHashStore[i][0] = A;
			abHashStore[i][1] = B;
		}
		
		
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
		int A;
		int B;
		int func;
		
		A = abHashStore[functionNumber][0];
		B = abHashStore[functionNumber][1];
		func = A + B*s.hashCode();
		
		if (func<0) func = func*-1;
		return (func%numBins);
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
