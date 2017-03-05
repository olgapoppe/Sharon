package executor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import event.*;

public class Spass {
	
	public static void execute (ArrayList<String> queries, ArrayList<Event> events) {
		
		long start = System.currentTimeMillis();
		int memory = 0;
		int pointers = 0;		
		
		// For each query, iterate over its types, create a stack for each type, store events in it, construct the sequences and count them
		String query = queries.get(0);	
			
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
							
				HashMap<Integer,Stack<Event>> new_length_to_stack = new HashMap<Integer,Stack<Event>>();
				ArrayList<Integer> lengths = (type_to_lengths.containsKey(event.type)) ? type_to_lengths.get(event.type) : new ArrayList<Integer>();
				for (Integer l : lengths) {
					
					if (l>0) {
						Stack<Event> prev_stack = length_to_stack.get(l-1);
						if (!prev_stack.isEmpty()) {
							Stack<Event> new_stack = new Stack<Event>();
							new_stack.addAll(length_to_stack.get(l));
							Event cloned_event = new Event(event.type);
							cloned_event.pointers.addAll(prev_stack);
							pointers += prev_stack.size();
							new_stack.add(cloned_event);
							new_length_to_stack.put(l,new_stack);
						}
					} else {
						Stack<Event> stack = length_to_stack.get(l);
						stack.add(event);
					}
				}
				for (Integer l : lengths) {
					if (new_length_to_stack.containsKey(l)) {
						Stack<Event> result = new_length_to_stack.get(l);
						length_to_stack.put(l,result);
						//System.out.println("Length " + l + " count " + result.size());
					}
				}
			}	
			// Traverse the pointers
			Stack<Event> last_stack = (length_to_stack.containsKey(query.length()-1)) ? length_to_stack.get(query.length()-1) : new Stack<Event>();
			BigInteger count = BigInteger.ZERO;
			for (Event last_event : last_stack) count = count.add(traversePointers(last_event, new Stack<Event>()));
			//System.out.println("Query " + query + " has result " + count.toString());	
		
		memory = events.size()+pointers;	
			
		long end = System.currentTimeMillis();
		System.out.println("\nLatency: " + (end - start) + "\nMemory: " + memory + "\n");		
	}
	
	// DFS in the stack
	public static BigInteger traversePointers (Event event, Stack<Event> current_sequence) {
		
		BigInteger count = BigInteger.ZERO;
				
		current_sequence.push(event);
		//System.out.println("pushed " + event);
		
		 if (event.pointers.isEmpty()) {   
	        	 	
	       	Iterator<Event> iter = current_sequence.iterator();
	       	EventSequence result = new EventSequence(new ArrayList<Event>());
	       	while(iter.hasNext()) {
	       		Event e = iter.next();
	       		result.events.add(e);
	       	}
	       	count = BigInteger.ONE;
	       	//results.add(result);	        	
			//System.out.println("result " + result);
				
	       } else {
	        /*** Recursive case: Traverse the following nodes. ***/        	
	        	for(Event prev_event : event.pointers) {        		
	        		//System.out.println("following of " + node.event.id + " is " + following.event.id);
	        		count = count.add(traversePointers(prev_event,current_sequence));        		
	        	}        	
	       }
	     Event top = current_sequence.pop();
	     //System.out.println("popped " + top.event.id);
	     
	     return count;
	}		
}