package executor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import event.*;

public class Spass {
	
	public static void shared (ArrayList<String> queries, ArrayList<Event> events) {
		
		long start = System.currentTimeMillis();
		int memory = 0;
		int pointers = 0;
		
		// For each query, iterate over its types, create a stack for each type, store events in it, construct the sequences and count them
		for  (String query : queries) {	
			
			HashMap<String,ArrayList<Integer>> type_to_lengths = new HashMap<String,ArrayList<Integer>>(); 
			HashMap<Integer,Stack<Event>> length_to_stack = new HashMap<Integer,Stack<Event>>();
				
			for (int i=0; i<query.length(); i++) {
					
				String type = query.charAt(i) + "";
				ArrayList<Integer> lengths = (type_to_lengths.containsKey(type)) ? type_to_lengths.get(type) : new ArrayList<Integer>();
				lengths.add(i);
				type_to_lengths.put(type,lengths);
				
				length_to_stack.put(i,new Stack<Event>());					
			}
				
			// For each event of type E, store the event in the stack for type E, connect to all events in the previous stack
			for (Event event : events) {
							
				ArrayList<Integer> lengths = (type_to_lengths.containsKey(event.type)) ? type_to_lengths.get(event.type) : new ArrayList<Integer>();
				for (Integer l : lengths) {
					
					if (l>0) {
						Stack<Event> prev_stack = length_to_stack.get(l-1);
						if (!prev_stack.isEmpty()) {
							Stack<Event> stack = length_to_stack.get(l);
							Event cloned_event = new Event(event.type);
							stack.add(cloned_event);
							cloned_event.pointers.addAll(prev_stack);
							pointers += prev_stack.size();						
						}
					} else {
						Stack<Event> stack = length_to_stack.get(l);
						stack.add(event);
					}
				}					
			}	
			// Traverse the pointers
			Stack<Event> last_stack = length_to_stack.get(query.length()-1);
			ArrayList<EventSequence> results = new ArrayList<EventSequence>();
			for (Event last_event : last_stack)
				traversePointers(last_event, new Stack<Event>(), results);
			
			System.out.println("Query " + query + " has result " + results.size());	
		}
		memory = events.size()+pointers;	
			
		long end = System.currentTimeMillis();
		System.out.println("\nLatency: " + (end - start) + "\nMemory: " + memory + "\n");		
	}
	
	// DFS in the stack
	public static void traversePointers (Event event, Stack<Event> current_sequence, ArrayList<EventSequence> results) {       
				
		current_sequence.push(event);
		//System.out.println("pushed " + event);
		
		 if (event.pointers.isEmpty()) {   
	        	 	
	        	Iterator<Event> iter = current_sequence.iterator();
	        	EventSequence result = new EventSequence(new ArrayList<Event>());
	        	while(iter.hasNext()) {
	        		Event e = iter.next();
	        		result.events.add(e);
	        	}
	        	results.add(result);	        	
				//System.out.println("result " + result);
				
	        } else {
	        /*** Recursive case: Traverse the following nodes. ***/        	
	        	for(Event prev_event : event.pointers) {        		
	        		//System.out.println("following of " + node.event.id + " is " + following.event.id);
	        		traversePointers(prev_event,current_sequence,results);        		
	        	}        	
	        }
	     Event top = current_sequence.pop();
	     //System.out.println("popped " + top.event.id);
	}		
}