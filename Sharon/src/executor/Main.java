package executor;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	    String queries = "./queries.txt";
	    String stream = "./stream.txt";
		
		String algorithm = "nonshared";
		int events_per_window = 100;
						
		// Read input parameters
	    for (int i=0; i<args.length; i++){
	    	if (args[i].equals("-type")) 		type = args[++i];
			if (args[i].equals("-stream")) 		stream = args[++i];
			if (args[i].equals("-queries")) 	queries = args[++i];
			if (args[i].equals("-algo")) 		algorithm = args[++i];
			if (args[i].equals("-epw")) 		events_per_window = Integer.parseInt(args[++i]);
		}	    	    
	        
	    // Print input parameters
	    System.out.println(	"Event type: " + type +
	    					//"\nInput file: " + input +
	    					"\nAlgorithm: " + algorithm +
	    					"\nEvents per window: " + events_per_window +
							"\n----------------------------------");	    

		/*** ALGORITHMS ***/
		if (algorithm.equals("spass")) {
			// Spass
		} else {
		if (algorithm.equals("nonshared")) {
			Aseq.nonshared(queries,stream,events_per_window);
		} else {
			// Aseq-s
		}}	
	}
}
