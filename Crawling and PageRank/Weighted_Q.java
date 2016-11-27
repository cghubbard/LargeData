package pa3;

import java.util.PriorityQueue;

public class Weighted_Q {

	public static void main(String[] args) {
		
		Tuple t1 = new Tuple("1",0);
		Tuple t2 = new Tuple("2",6);
		Tuple t3 = new Tuple("3",0);
		Weighted_Q q = new Weighted_Q(true);
		q.add(t1);
		q.add(t2);
		q.add(t3);
		System.out.println(q.extract().getLink());
		System.out.println(q.extract().getLink());
		Tuple t4 = new Tuple("4",15);
		q.add(t4);
		System.out.println(q.extract().getLink());
		
	}
	
	
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
