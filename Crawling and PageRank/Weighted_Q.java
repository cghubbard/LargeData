package pa3;

import java.util.PriorityQueue;

public class Weighted_Q {
	
	
	private PriorityQueue<Tuple> queue;
	private int added = 0;
	private boolean isWeighted;
	
	public Weighted_Q(boolean isWeighted) {
		
		TupleComparator tupleComp = new TupleComparator();
		this.queue = new PriorityQueue<Tuple>(100,tupleComp);
		this.isWeighted = isWeighted;
		
	}
	
	public void add(Tuple t) {
		
		t.setAdded(added);
		
		if(!this.isWeighted) { t.setWeight(0); }
		
		queue.add(t);
		added++;
	}
	
	public Tuple extract() {
		return queue.poll();
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
}
