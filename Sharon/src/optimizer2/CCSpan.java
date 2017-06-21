package optimizer2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class CCSpan {
	
	 public static HashMap<String,Pattern> getFrequentPatterns (ArrayList<Pattern> randomPatterns, HashMap<String,Integer> rates) {
		 
		 HashMap<String,Pattern> results = new HashMap<String,Pattern>();		 
	     for (Pattern p : randomPatterns) {
	    	 results = getSubpatterns(p.pattern,results);
	     }	
	     
	     // Exclude those patterns that appear in only one pattern and compute BValue of frequent patterns
	     Set<String> keys = results.keySet();
	     ArrayList<Pattern> removed = new ArrayList<Pattern>();
	     //int number_of_patterns = 0;
	     //int number_of_beneficial_patterns = 0;
	     for (String key : keys) {
	    	 Pattern p = results.get(key);
	    	 if (!p.isFrequent() || p.pattern.length()<4) {
	    		removed.add(p);
	    	 	//System.out.println("Infrequent pattern " + p.toString());
	    	 } else {
	    		int bvalue =  p.getBValue(rates);
	    		if (bvalue > 0) {
	    			//System.out.println("Frequent beneficial pattern " + p.toString() + " with BValue " + bvalue + " appears in " + p.patterns.size() + " patterns");
	    			//number_of_beneficial_patterns++;
	    		}
	    		//else { System.out.println("Frequent non-beneficial pattern " + p.toString() + " with BValue " + bvalue); }
	    		//number_of_patterns++;
	    	 }
	     }
	     //System.out.println(number_of_beneficial_patterns + " of " + number_of_patterns + " are beneficial.");
	     for (Pattern p : removed)
	    	 results.remove(p);
	     
		 return results;
	 }
	 
	 public static HashMap<String,Pattern> getSubpatterns (String pattern, HashMap<String,Pattern> results) {
		 
		 String[] parts = pattern.split(",");
		 
		for (int end=0; end <= parts.length; end++) {
			 for (int start=0; start<=end; start++) {
				 String[] subparts = Arrays.copyOfRange(parts, start, end);
				 String subpattern = Arrays.toString(subparts).replace("[", "").replace("]", "").replaceAll(" ", "") + ",";
				 Pattern p = (results.containsKey(subpattern)) ? results.get(subpattern) : new Pattern(subpattern);
				 p.add2Patterns(pattern);
				 if (subpattern.split(",").length > 1) { results.put(subpattern, p);	}
				 }
		}
		 return results;
	 }

}
