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
		
		try {			
			// Input file with queries
			Scanner scanner_queries = new Scanner(new File(queries));
			while (scanner_queries.hasNextLine()) {
				
				// Get query, iterate over its types, map each type to lengths, map length to count 
				String query = scanner_queries.nextLine();
				for (int i=0; i<query.length(); i++) {
					
					String type = query.charAt(i) + "";
					ArrayList<Integer> lengths = (type_to_lengths.containsKey(type)) ? type_to_lengths.get(type) : new ArrayList<Integer>();
					lengths.add(i);
					type_to_lengths.put(type,lengths);
					
					length_to_count.put(i+1,0);					
				}
				
				// For each event, update the respective prefix counters
				Scanner scanner_stream = new Scanner(new File(stream));
				String events_string = scanner_stream.nextLine();
				String[] events = events_string.split(" ");
				for (String event : events) {
					
					ArrayList<Integer> lengths = type_to_lengths.get(event);
					for (Integer l : lengths) {
						Integer count = length_to_count.get(l-1)+length_to_count.get(l);
						length_to_count.put(l,count);
					}					
				}		
				scanner_stream.close();
				
				// Print the count per query
				int result = length_to_count.get(query.length()+1);
				System.out.println("Query " + query + " has result " + result);				
			}
			scanner_queries.close();
		} catch (FileNotFoundException e) { e.printStackTrace(); }
				
		long end = System.currentTimeMillis();
		long duration = end - start;
		int memory = 0;		
		
		System.out.println("\nLatency: " + duration + "\nMemory: " + memory + "\n");
	}
}
