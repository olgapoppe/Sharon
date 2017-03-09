package optimizer2;

/*
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;
*/
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Main {
	
	public static void main(String[] args) {
		 
		 /*** Read the input parameters and generate random rates of events per type ***/
		 int n = 0; // n is the number of long patterns
		 int m = 0; // m*l is the length of long patterns
		 int k = 0; // number of short patterns
		 int l = 0; // length of short patterns
		 int t = 0;	// event types
		 int algo = 0; // algorithm: 0 - Exhaustive search, 1 - GWMIN, 2 - B&B
		 String file_of_queries = "";
		 String file_of_rates = "";
		 
		 for (int i=0; i<args.length; i++) {
			if (args[i].equals("-n"))		n = Integer.parseInt(args[++i]);
			if (args[i].equals("-m"))		m = Integer.parseInt(args[++i]);
			if (args[i].equals("-k"))		k = Integer.parseInt(args[++i]);
			if (args[i].equals("-l"))		l = Integer.parseInt(args[++i]);
			if (args[i].equals("-t")) 		t = Integer.parseInt(args[++i]);
			if (args[i].equals("-algo")) 	algo = Integer.parseInt(args[++i]);
			if (args[i].equals("-queries")) file_of_queries = args[++i];
			if (args[i].equals("-rates")) 	file_of_rates = args[++i];
		 }	
		 
		 /*** Generate random rates for t event types ***/
		 HashMap<String,Integer> rates = new HashMap<String,Integer>();
		 /*
		 Random random = new Random();
		 System.out.println("\n*** Event rate per event type: ***");
		 
		 try {			 
				File output_file = new File(file_of_rates);
				BufferedWriter output = new BufferedWriter(new FileWriter(output_file));
		 
			 for (int i=0; i<t; i++) {
				 int rate = random.nextInt(t) + 1; 
				 rates.put(i+"", rate);
				 System.out.println("Event type " + i + " has rate " + rate);
				 
				 output.append(i + ":" + rate + "\n");
			 }
			 output.close();
		 } catch (IOException e) { e.printStackTrace(); }
		 */
		 
		 try {
		 String line;
		 BufferedReader reader = new BufferedReader(new FileReader(file_of_rates));
		 while ((line = reader.readLine()) != null) {
		        String[] parts = line.split(":", 2);
		        if (parts.length >= 2)
		        {
		            String key = parts[0];
		            Integer value = Integer.parseInt(parts[1]);
		            rates.put(key, value);
		        } else {
		            System.out.println("ignoring line: " + line);
		        }
		    }

		    reader.close();
		 } catch (IOException e) { e.printStackTrace(); }
		 		 
		 /*** Generate n patterns of length m*l using t event types ***/
		 System.out.println("\n*** " + n + " patterns of length " + (m*l) + " composed of " + t + " event types: ***");
		 //ArrayList<Pattern> randomPatterns = PatternGenerator.getPatterns(k,l,t,n,m,file_of_queries);
		 
		 
		 ArrayList<Pattern> randomPatterns = new ArrayList<Pattern>();
		 try {
			 String line;
			 BufferedReader reader = new BufferedReader(new FileReader(file_of_queries));
			 while ((line = reader.readLine()) != null) {
			        Pattern pattern = new Pattern(line);
			        randomPatterns.add(pattern);
			    }

			    reader.close();
			 } catch (IOException e) { e.printStackTrace(); }		
		 
		 
		 /*** Get frequent patterns from random patterns ***/
		 System.out.println("\n*** Frequent patterns: ***");
		 HashMap<String,Pattern> frequentPatterns = CCSpan.getFrequentPatterns(randomPatterns,rates);
		 
		 /*** Construct PBC Graph ***/
		 System.out.println("\n*** PBC Graph: ***");
		 Graph G = new Graph(frequentPatterns);
		 System.out.println(G);
		 System.out.println("Number of Vertices: " + G.numVertices());
		 System.out.println("Number of Edges: " + G.numEdges());
		 
		 /*** Get shared patterns from frequent patterns ***/
		
		 System.out.println("\n*** Shared patterns created by " + algo + " algorithm: ***");
		 Set<String> sharedPatterns = new HashSet<String>();
		 long start =  System.currentTimeMillis();
		 switch (algo) {
		 case 0: sharedPatterns = SharingPlanSelection.exhaustive(G);
		 break;
		 case 1: sharedPatterns = SharingPlanSelection.gwmin(G);
		 break;
		 case 2: sharedPatterns = SharingPlanSelection.sharon(G);
		 break;
		 }	
		 long end =  System.currentTimeMillis();
		 System.out.println("Duration: " + (end - start));
		 for (String s : sharedPatterns) {
				System.out.println(s);
			}
		  	 
	}
}
