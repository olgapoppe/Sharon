package executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Aseq {
	
	public static void nonshared (String queries, String stream, int events_per_window) {
		
		HashMap<String,ArrayList<Integer>> type_to_lengths = new HashMap<String,ArrayList<Integer>>(); 
		HashMap<Integer,Integer> length_to_count = new HashMap<Integer,Integer>();
		long start = System.currentTimeMillis();
		int memory = 0;
		
		try {			
			// Input file with queries
			Scanner scanner_queries = new Scanner(new File(queries));
			while (scanner_queries.hasNextLine()) {
				
				// Get query, iterate over its types, map each type to lengths, map length to count 
				String query = scanner_queries.nextLine();
				memory += query.length();
				
				for (int i=0; i<query.length(); i++) {
					
					String type = query.charAt(i) + "";
					ArrayList<Integer> lengths = (type_to_lengths.containsKey(type)) ? type_to_lengths.get(type) : new ArrayList<Integer>();
					lengths.add(i);
					type_to_lengths.put(type,lengths);
					
					length_to_count.put(i,0);					
				}
				
				// For each event of type E, update the counters for lengths E maps to
				Scanner scanner_stream = new Scanner(new File(stream));
				String events_string = scanner_stream.nextLine();
				String[] events = events_string.split(" ");
				for (String event : events) {
					
					HashMap<Integer,Integer> new_length_to_count = new HashMap<Integer,Integer>();
					ArrayList<Integer> lengths = (type_to_lengths.containsKey(event)) ? type_to_lengths.get(event) : new ArrayList<Integer>();
					for (Integer l : lengths) {
						
						int count = 0;
						// This event is a start event
						if (l-1<0) count = length_to_count.get(l)+1;
						// This event can be appended to existing sequences
						if (l-1>=0 && length_to_count.get(l-1)>0) count = length_to_count.get(l-1) + length_to_count.get(l);
						// If the count is set, update the table
						if (count>0) {
							new_length_to_count.put(l,count);
							//System.out.println(event + " updates the count for length " + l + " to " + count);
						}
					}					
					for (Integer l : lengths) {
						if (new_length_to_count.containsKey(l)) length_to_count.put(l,new_length_to_count.get(l));						
					}					
				}		
				scanner_stream.close();
				
				// Print the count per query
				int result = length_to_count.get(query.length()-1);
				System.out.println("Query " + query + " has result " + result);				
			}
			scanner_queries.close();
		} catch (FileNotFoundException e) { e.printStackTrace(); }
				
		long end = System.currentTimeMillis();
		long duration = end - start;
		
		System.out.println("\nLatency: " + duration + "\nMemory: " + memory + "\n");
	}
}
