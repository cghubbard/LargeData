package pa3;



public class Tuple {
	
	private String link;
	private float weight;
	private int added;
	
	public Tuple (String link, float weight) {
		
		this.link = link;
		this.weight = weight;
		this.setAdded(0);
	}
	
	
	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getLink() {
		return link;
	}

	public void setLink (String link) {
		this.link = link;
	}


	public int getAdded() {
		return added;
	}


	public void setAdded(int added) {
		this.added = added;
	}
}


