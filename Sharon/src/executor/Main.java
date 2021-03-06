package executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import event.*;

// -type commerce -stream ../../../commerce/stream.txt -queries ../../../commerce/queries.txt -algo nonshared -epw 80
// -type traffic -stream ../../../LR/traffic.dat -queries ../../../LR/queries.txt -algo nonshared -epw 1000
// -type taxi -stream ../../../taxi/yellow_tripdata_2014-10.csv -queries ../../../taxi/queries.txt -algo nonshared -epw 10

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
	    String type = "commerce";
	    String file_of_queries = "./queries.txt";
	    String file_of_stream = "./stream.txt";
		
		String algorithm = "nonshared";
		int events_per_window = Integer.MAX_VALUE;
		int query_number = Integer.MAX_VALUE;
						
		// Read input parameters
	    for (int i=0; i<args.length; i++){
	    	if (args[i].equals("-type")) 		type = args[++i];
			if (args[i].equals("-stream")) 		file_of_stream = args[++i];
			if (args[i].equals("-queries")) 	file_of_queries = args[++i];
			if (args[i].equals("-algo")) 		algorithm = args[++i];
			if (args[i].equals("-epw")) 		events_per_window = Integer.parseInt(args[++i]);
			if (args[i].equals("-qn"))	 		query_number = Integer.parseInt(args[++i]);
		}	    	    
	        
	    // Print input parameters
	    System.out.println(	"Event type: " + type +
	    					//"\nInput file: " + input +
	    					"\nAlgorithm: " + algorithm +
	    					"\nEvents per window: " + events_per_window +
	    					"\nNumber of queries: " + query_number +
							"\n----------------------------------");	
	    
	    /*** Parsing queries and event stream ***/
	    try {
	    	ArrayList<String> queries = new ArrayList<String>();
	    	Scanner query_scanner = new Scanner(new File(file_of_queries));
	    	while (query_scanner.hasNextLine()) {
				String query = query_scanner.nextLine();
	    		queries.add(query);	    		
	    	}
	    	query_scanner.close();
	    
	    	ArrayList<Event> events = new ArrayList<Event>();
	    	Scanner stream_scanner = new Scanner(new File(file_of_stream));
	    	int event_number = 0;
	    	while (stream_scanner.hasNextLine() && event_number < events_per_window) {
	    		String event_string = stream_scanner.nextLine();		
	    		Event e = Event.parse(event_string,type);
				events.add(e);
				event_number++;			
			}
			stream_scanner.close();
		
			/*** ALGORITHMS ***/
			if (algorithm.equals("spass")) {
				Spass.execute(queries,events);
			} else {
			if (algorithm.equals("aseq")) {
				Aseq.execute(queries,events,false,query_number);
			} else {
				Aseq.execute(queries,events,true,query_number);
			}}	
		
	    } catch (FileNotFoundException e) { e.printStackTrace(); }
	}
}
