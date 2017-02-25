package optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CCSpan {
	
	 public static HashMap<String,ArrayList<Integer>> getFrequentPatterns (ArrayList<String> randomPatterns) {
		 
		 // A sub-pattern p is mapped to the list of patterns p appears within
		 HashMap<String,ArrayList<Integer>> results = new HashMap<String,ArrayList<Integer>>();		 
		 int patternID = 1;
	     for (String p : randomPatterns) {
	    	 results = getSubpatterns(p,patternID,results);		    	 
	    	 patternID++;
	     }	
	     
	     // Exclude those patterns that appear in only one pattern
	     Set<String> patterns = results.keySet();
	     ArrayList<String> removed = new ArrayList<String>();
	     for (String p : patterns) {
	    	 if (results.get(p).size() < 2) {
	    		removed.add(p);
	    	 	System.out.println("Infrequent pattern " + p);
	    	 } else {
	    		System.out.println("Frequent pattern " + p + " appears in " + results.get(p).size() + " patterns");
	    	 }
	     }
	     for (String p : removed)
	    	 results.remove(p);
	     
		 return results;
	 }
	 
	 public static HashMap<String,ArrayList<Integer>> getSubpatterns (String pattern, int patternID, HashMap<String,ArrayList<Integer>> results) {
		 
		 for (int end=0; end<pattern.length(); end++) {
			 for (int start=0; start<=end; start++) {
				 String subpattern = pattern.substring(start,end);
				 if (subpattern.length() > 0) {
					 if (results.containsKey(subpattern)) {
						 results.get(subpattern).add(patternID);					 
					 } else {
						 ArrayList<Integer> patternIDs = new ArrayList<Integer>();
						 patternIDs.add(patternID);
						 results.put(subpattern, patternIDs);
				 }}
				 //System.out.println(subpattern);
		 }}
		 return results;
	 }

}
