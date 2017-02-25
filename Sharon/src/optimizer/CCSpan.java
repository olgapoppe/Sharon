package optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CCSpan {
	
	 public static HashMap<String,Pattern> getFrequentPatterns (ArrayList<Pattern> randomPatterns) {
		 
		 HashMap<String,Pattern> results = new HashMap<String,Pattern>();		 
		 int patternID = 1;
	     for (Pattern p : randomPatterns) {
	    	 results = getSubpatterns(p.pattern,patternID,results);		    	 
	    	 patternID++;
	     }	
	     
	     // Exclude those patterns that appear in only one pattern
	     Set<String> keys = results.keySet();
	     ArrayList<Pattern> removed = new ArrayList<Pattern>();
	     for (String key : keys) {
	    	 Pattern p = results.get(key);
	    	 if (!p.isFrequent()) {
	    		removed.add(p);
	    	 	//System.out.println("Infrequent pattern " + p.toString());
	    	 } else {
	    		System.out.println("Frequent pattern " + p.toString() + " appears in " + p.patternsToString() + " patterns");
	    	 }
	     }
	     for (Pattern p : removed)
	    	 results.remove(p);
	     
		 return results;
	 }
	 
	 public static HashMap<String,Pattern> getSubpatterns (String pattern, int patternID, HashMap<String,Pattern> results) {
		 
		for (int end=0; end<=pattern.length(); end++) {
			 for (int start=0; start<=end; start++) {
				 String subpattern = pattern.substring(start,end);
				 if (subpattern.length() > 0) {
					 Pattern p = (results.containsKey(subpattern)) ? results.get(subpattern) : new Pattern(subpattern);
					 p.add2Patterns(patternID);
					 results.put(subpattern, p);					 
				 }				 
		 }}
		 return results;
	 }

}
