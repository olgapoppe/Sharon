package optimizer;

public class PatternGenerator {
	
	 public static void main(String[] args) {
		 
		 /*** Read and print the input parameters ***/
		 int number_of_patterns = 0;
		 int min_length = 0;
		 int max_length = 0;
		 int number_of_event_types = 0;	    
		 
		 for (int i=0; i<args.length; i++) {
			if (args[i].equals("-number_of_patterns")) 		number_of_patterns = Integer.parseInt(args[++i]);
			if (args[i].equals("-min_length")) 				min_length = Integer.parseInt(args[++i]);
			if (args[i].equals("-max_length")) 				max_length = Integer.parseInt(args[++i]);
			if (args[i].equals("-number_of_event_types")) 	number_of_event_types = Integer.parseInt(args[++i]);
		 }	
				
		 System.out.println(	"Number of patterns: " + number_of_patterns +
			   "\nMin length: " + min_length +
			   "\nMax length: " + max_length +
			   "\nNumber of event types: " + number_of_event_types +
				"\n----------------------------------");	    
		}

}
