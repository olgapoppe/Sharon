package executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigInteger;
import event.*;

public class Aseq {
	
	public static void nonshared (ArrayList<String> queries, ArrayList<Event> events) {
		
		long start = System.currentTimeMillis();
		int memory = 0;
		
		// For each query, iterate over its types, map each type to lengths, map length to count
		for  (String query : queries) {	
			
			HashMap<String,ArrayList<Integer>> type_to_lengths = new HashMap<String,ArrayList<Integer>>(); 
			HashMap<Integer,BigInteger> length_to_count = new HashMap<Integer,BigInteger>();
				
			for (int i=0; i<query.length(); i++) {
					
				String type = query.charAt(i) + "";
				ArrayList<Integer> lengths = (type_to_lengths.containsKey(type)) ? type_to_lengths.get(type) : new ArrayList<Integer>();
				lengths.add(i);
				type_to_lengths.put(type,lengths);
				
				length_to_count.put(i,BigInteger.ZERO);					
			}
				
			// For each event of type E, update the counters for lengths E maps to
			for (Event event : events) {
					
				HashMap<Integer,BigInteger> new_length_to_count = new HashMap<Integer,BigInteger>();
				ArrayList<Integer> lengths = (type_to_lengths.containsKey(event.type)) ? type_to_lengths.get(event.type) : new ArrayList<Integer>();
				for (Integer l : lengths) {
						
					BigInteger count = BigInteger.ZERO;
					// This event is a start event
					if (l-1<0) count = length_to_count.get(l).add(BigInteger.ONE);
					// This event can be appended to existing sequences
					if (l-1>=0 && length_to_count.get(l-1).compareTo(BigInteger.ZERO)==1) count = length_to_count.get(l-1).add(length_to_count.get(l));
					// If the count is set, update the table
					if (count.compareTo(BigInteger.ZERO)==1) {new_length_to_count.put(l,count);
						//System.out.println(event + " updates the count for length " + l + " to " + count);
					}
				}					
				for (Integer l : lengths) {
					if (new_length_to_count.containsKey(l)) length_to_count.put(l,new_length_to_count.get(l));						
				}					
			}		
					
			// Print the count per query
			BigInteger result = length_to_count.get(query.length()-1);
			System.out.println("Query " + query + " has result " + result.toString());	
			memory += query.length();
		}
			
		long end = System.currentTimeMillis();
		System.out.println("\nLatency: " + (end - start) + "\nMemory: " + memory + "\n");
	}
}
