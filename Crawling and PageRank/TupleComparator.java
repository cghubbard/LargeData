package pa3;

import java.util.Comparator;

public class TupleComparator implements Comparator<Tuple> {

	@Override
	public int compare(Tuple t1, Tuple t2) {
		
			float w1 = t1.getWeight();
			float w2 = t2.getWeight();
			
			float difference = w1-w2;
			
			if ( difference == 0 ) { return (t1.getAdded() - t2.getAdded()); }
			else if (difference > 0) { return (-1); };
			return (1);
	}
	
}
