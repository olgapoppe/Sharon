package optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Pattern {
	
	public String pattern;
	// A list of patterns this pattern appears within
	public ArrayList<String> patterns;
	public int BValue;
	private int degree;
	
	Pattern(String p) {
		pattern = p;
		patterns = new ArrayList<String>();
		BValue = 0;
		degree = 0;
	}
	
	public boolean isFrequent() {
		return patterns.size() > 1;
	}
	
	public void add2Patterns(String pattern) {
		if (!patterns.contains(pattern))
			patterns.add(pattern);
	}	
	
	public int getBValue(HashMap<String,Integer> rates) {
		
		int cost_pattern = 0;
		for (int i=0; i<pattern.length(); i++) { cost_pattern = rates.get(pattern.charAt(i)+""); }
		int ns_cost = 0;
		int s_cost = rates.get(pattern.charAt(0)+"") * cost_pattern;
	
		for (String super_pattern : patterns) {
			
			/*** Determine prefix and suffix ***/
			String prefix = "";
			String suffix = "";			
			for (int i=0; i<super_pattern.length(); i++) {
				if(super_pattern.regionMatches(i, pattern, 0, pattern.length())) {
					prefix = super_pattern.substring(0,i);
					suffix = super_pattern.substring(i+pattern.length());
					break;
			}}	
			//System.out.println(super_pattern + " : " + prefix + " " + pattern + " " + suffix);
			
			/*** Non-shared method ***/
			int cost_prefix = 0;
			int cost_suffix =0;
			for (int i=0; i<prefix.length(); i++) { cost_prefix = rates.get(prefix.charAt(i)+""); }
			for (int i=0; i<suffix.length(); i++) { cost_suffix = rates.get(suffix.charAt(i)+""); }		
			ns_cost += rates.get(super_pattern.charAt(0)+"") * (cost_prefix + cost_pattern + cost_suffix);	
			
			/*** Shared method ***/
			if (prefix.length()>0) s_cost += rates.get(prefix.charAt(0)+"") * cost_prefix;
			if (suffix.length()>0) s_cost += rates.get(suffix.charAt(0)+"") * cost_suffix;
			if (prefix.length()>0 && suffix.length()>0) s_cost += rates.get(prefix.charAt(0)+"") * rates.get(pattern.charAt(0)+"") * rates.get(suffix.charAt(0)+"");			
		}
		BValue = ns_cost - s_cost;				
		return BValue;
	}
	
	public int getBValue() {
		return BValue;
	}
	
	public void changeDegree(int num) {
		degree += num;
	}
	
	public int getDegree() {
		return degree;
	}
	
	public boolean conflictsWith(Pattern p) {
		Set<String> intersection = new HashSet<String>(patterns);
		intersection.retainAll(p.getQueries());
		if (intersection.size() == 0) {
			return false;
		}
		String p_lab = p.toString();
		for (int i=0; i<pattern.length(); i++) {
			for (int j=0; j<p_lab.length(); j++) {
				int k=0;
				while (i+k+1<=pattern.length() && j+k+1<=p_lab.length()) {
					if (pattern.substring(i, i+k+1).equals(p_lab.substring(j, j+k+1))) {
						return true;
					}
					k++;
				}
			}
		}
		return false;
	}
	
	public Set<String> getQueries() {
		return new HashSet<String>(patterns);
	}
	
	public String patternsToString() {
		String result = "";
		for (String i : patterns)
			result += i + ";";
		return result;
	}
	
	public String toString() {
		return pattern + "";
	}
}
