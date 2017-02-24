package algorithm;

import java.util.*;

public class Pattern {
	private String label;
	private int BValue;
	private Set<String> queries;
	
	public Pattern(String lab, int b, Set<String> qs) {
		label = lab;
		BValue = b;
		queries = qs;
	}
	
	public boolean conflictsWith(Pattern p) {
		Set<String> intersection = new HashSet<String>(queries);
		intersection.retainAll(p.getQueries());
		if (intersection.size() == 0) {
			return false;
		}
		String p_lab = p.toString();
		for (int i=0; i<label.length(); i++) {
			for (int j=0; j<p_lab.length(); j++) {
				int k=0;
				while (i+k+1<=label.length() && j+k+1<=p_lab.length()) {
					//System.out.println("testing " + label.substring(i, i+k+1) + " and " + p_lab.substring(j, j+k+1));
					if (label.substring(i, i+k+1).equals(p_lab.substring(j, j+k+1))) {
						//System.out.println("matching " + label.substring(i, i+k+1));
						return true;
					}
					k++;
				}
			}
		}
		return false;
	}
	
	public int getBValue() {
		return BValue;
	}
	
	public Set<String> getQueries() {
		return queries;
	}
	
	public String toString() {
		return label;
	}
}
