package optimizer;

import java.util.ArrayList;

public class Pattern {
	
	public String pattern;
	// A list of patterns this pattern appears within
	public ArrayList<Integer> patternIDs;
	public int BValue;
	
	Pattern(String p) {
		pattern = p;
		patternIDs = new ArrayList<Integer>();
		BValue = 0;
	}
	
	public boolean isFrequent() {
		return patternIDs.size() > 1;
	}
	
	public void add2Patterns(int patternID) {
		if (!patternIDs.contains(patternID))
			patternIDs.add(patternID);
	}	
	
	public String patternsToString() {
		String result = "";
		for (int i : patternIDs)
			result += i + ";";
		return result;
	}
	
	public String toString() {
		return pattern + "";
	}
}
