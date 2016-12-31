import java.util.Comparator;

public class RelevanceScoreComparator implements Comparator<RelevanceScore> {

	@Override
	public int compare(RelevanceScore s1, RelevanceScore s2) {
		if (s1.getScore() < s2.getScore()) { return -1; }
		if (s1.getScore() > s2.getScore()) { return 1; }
		
		int stringComp = s1.getDocument().compareTo(s2.getDocument());

		if (stringComp > 0) { return 1; }
		if (stringComp < 0) { return -1;}
		return 0;
	}

}
