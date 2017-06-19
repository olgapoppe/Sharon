package optimizer2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

// -n 20 -m 3 -k 40 -l 2 -t 100  -queries ../../experiments/queriesTEST.txt -rates ../../experiments/ratesTEST.txt -graph ../../experiments/graphTEST.txt

public class Main {
	
	public static void main(String[] args) {
		 
		 /*** Read the input parameters and generate random rates of events per type ***/
		 
		 int n = 0; // n is the number of long patterns
		 int m = 0; // m*l is the length of long patterns
		 int k = 0; // number of short patterns
		 int l = 0; // length of short patterns
		 int t = 0;	// event types, must be greater than l
		 
		 String file_of_queries = "";
		 String file_of_rates = "";
		 String file_of_graph = "";
		 String file_of_PQ = "";
		 
		 for (int i=0; i<args.length; i++) {
			 
			if (args[i].equals("-n"))		n = Integer.parseInt(args[++i]);
			if (args[i].equals("-m"))		m = Integer.parseInt(args[++i]);
			if (args[i].equals("-k"))		k = Integer.parseInt(args[++i]);
			if (args[i].equals("-l"))		l = Integer.parseInt(args[++i]);
			if (args[i].equals("-t")) 		t = Integer.parseInt(args[++i]);
			
			if (args[i].equals("-queries")) file_of_queries = args[++i];
			if (args[i].equals("-rates")) 	file_of_rates = args[++i];
			if (args[i].equals("-graph")) file_of_graph = args[++i];
			if (args[i].equals("-PQ")) file_of_PQ = args[++i];
		 }	
		 
		 /*** Generate random rates for t event types ***/
		 HashMap<String,Integer> rates = new HashMap<String,Integer>();
		 
		 Random random = new Random();
		 //System.out.println("\n*** Event rate per event type: ***");
		 
		 try {			 
				File output_file = new File(file_of_rates);
				BufferedWriter output = new BufferedWriter(new FileWriter(output_file));
		 
			 for (int i=0; i<t; i++) {
				 int rate = random.nextInt(t) + 1; 
				 rates.put(i+"", rate);
				 //System.out.println("Event type " + i + " has rate " + rate);
				 
				 output.append(i + ":" + rate + "\n");
			 }
			 output.close();
		 } catch (IOException e) { e.printStackTrace(); }
	
		 ArrayList<Pattern> randomPatterns = PatternGenerator.getPatterns(k,l,t,n,m,file_of_queries);
		 
		 /*** Get frequent patterns from random patterns ***/
		 System.out.println("\n*** Frequent patterns: ***");
		 HashMap<String,Pattern> frequentPatterns = CCSpan.getFrequentPatterns(randomPatterns,rates);
		 
		 try {			 
				File output_file = new File(file_of_PQ);
				BufferedWriter output = new BufferedWriter(new FileWriter(output_file));
		 
			for (Pattern freqP : frequentPatterns.values()) {
				if (freqP.getBValue() > 0) {
					output.append(freqP.toString() + ":" + freqP.patternsToString() + "\n");
				}
			 }
			 output.close();
		 } catch (IOException e) { e.printStackTrace(); }
		 
		 /*** Construct PBC Graph ***/
		 //System.out.println("\n*** PBC Graph: ***");
		 long startConstruction = System.currentTimeMillis();
		 Graph G = new Graph(frequentPatterns);
		 long endConstruction = System.currentTimeMillis();
		 System.out.println("\nDuration Construction: " + (endConstruction - startConstruction));
		 //System.out.println("\n" + G);
		 System.out.println("\nNumber of Vertices: " + G.numVertices());
		 System.out.println("Number of Edges: " + G.numEdges());
		 
		 try 
		 {
			 File output_file = new File(file_of_graph);
			 BufferedWriter output = new BufferedWriter(new FileWriter(output_file));
			 output.append(G.toString());
			 output.close();
		 } catch (IOException e) { e.printStackTrace(); }
		  	 
	}
}
