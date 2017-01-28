package optimizer;

import java.util.Random;

public class PatternGenerator {
	
	 public static void main(String[] args) {
		 
		 /*** Read and print the input parameters ***/
		 int k = 0;
		 int l = 0;
		 int t = 0;	    
		 
		 for (int i=0; i<args.length; i++) {
			if (args[i].equals("-k"))	k = Integer.parseInt(args[++i]);
			if (args[i].equals("-l"))	l = Integer.parseInt(args[++i]);
			if (args[i].equals("-t")) 	t = Integer.parseInt(args[++i]);
		 }	
				
		 System.out.println(k + " random patterns of length " + l + " composed of " + t + " event types:");	    
	 	 	
	 	 /*** Create k random patterns of length l ***/
		 Random random = new Random();
		 for (int i=0; i<k; i++) {
			 String pattern = "";
			 for (int j=0; j<l; j++) {
				int event_type = random.nextInt(t) + 1;
				pattern += event_type + " ";
			 }
			 System.out.println(pattern);
		}			 
	 }
}
