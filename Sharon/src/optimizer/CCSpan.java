package optimizer;

import java.util.ArrayList;
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
	     for (String key : keys) {
	    	 Pattern p = results.get(key);
	    	 if (!p.isFrequent()) {
	    		removed.add(p);
	    	 	//System.out.println("Infrequent pattern " + p.toString());
	    	 } else {
	    		System.out.println("Frequent pattern " + p.toString() + 
	    				" with BValue " + p.getBValue(rates) +
	    				" appears in " + p.patterns.size() + " patterns");
	    	 }
	     }
	     for (Pattern p : removed)
	    	 results.remove(p);
	     
		 return results;
	 }
	 
	 public static HashMap<String,Pattern> getSubpatterns (String pattern, HashMap<String,Pattern> results) {
		 
		for (int end=0; end<=pattern.length(); end++) {
			 for (int start=0; start<=end; start++) {
				 String subpattern = pattern.substring(start,end);
				 if (subpattern.length() > 0) {
					 Pattern p = (results.containsKey(subpattern)) ? results.get(subpattern) : new Pattern(subpattern);
					 p.add2Patterns(pattern);
					 results.put(subpattern, p);					 
				 }				 
		 }}
		 return results;
	 }

}
