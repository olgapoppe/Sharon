package executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import event.*;

public class Main {
	
	public static void main (String[] args) { 
		
		/*** Print current time ***/
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
	    System.out.println("----------------------------------\nCurrent Date: " + ft.format(dNow));
	    
	    /*Path currentRelativePath = Paths.get("");
	    String s = currentRelativePath.toAbsolutePath().toString();
	    System.out.println("Current relative path is: " + s);*/
	    
	    /*** Input and output ***/
	    // Set default values
	    String type = "commerse";
	    String file_of_queries = "./queries.txt";
	    String file_of_stream = "./stream.txt";
		
		String algorithm = "nonshared";
		int events_per_window = Integer.MAX_VALUE;
						
		// Read input parameters
	    for (int i=0; i<args.length; i++){
	    	if (args[i].equals("-type")) 		type = args[++i];
			if (args[i].equals("-stream")) 		file_of_stream = args[++i];
			if (args[i].equals("-queries")) 	file_of_queries = args[++i];
			if (args[i].equals("-algo")) 		algorithm = args[++i];
			if (args[i].equals("-epw")) 		events_per_window = Integer.parseInt(args[++i]);
		}	    	    
	        
	    // Print input parameters
	    System.out.println(	"Event type: " + type +
	    					//"\nInput file: " + input +
	    					"\nAlgorithm: " + algorithm +
	    					"\nEvents per window: " + events_per_window +
							"\n----------------------------------");	
	    
	    /*** Parsing queries and event stream ***/
	    try {
	    	ArrayList<String> queries = new ArrayList<String>();
	    	ArrayList<Event> events = new ArrayList<Event>();
	    	
	    	Scanner query_scanner = new Scanner(new File(file_of_queries));
	    	while (query_scanner.hasNextLine()) {
				String query = query_scanner.nextLine();
	    		queries.add(query);	    		
	    	}
	    	query_scanner.close();
	    
	    	Scanner stream_scanner = new Scanner(new File(file_of_stream));
			String events_string = stream_scanner.nextLine();
			String[] event_strings = events_string.split(" ");
			int event_number = 0;
			for (String line : event_strings) {
				Event e = Event.parse(line,type);
				if (event_number < events_per_window) {
					events.add(e);
					event_number++;
				} else {
					break;
				}
			}
			stream_scanner.close();
		
			/*** ALGORITHMS ***/
			if (algorithm.equals("nonshared")) {
				Aseq.nonshared(queries,events);
			} else {
			if (algorithm.equals("shared")) {
				// Aseq.shared(queries,events);
			} else {
				// spass
			}}	
		
	    } catch (FileNotFoundException e) { e.printStackTrace(); }
	}
}
