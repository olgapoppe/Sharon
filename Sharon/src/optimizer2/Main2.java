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
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Main2 {
	
	public static void main(String[] args) {
		 
		 /*** Read the input parameters ***/
		 
		 int algo = 0; // algorithm: 0 - Exhaustive search, 1 - GWMIN, 2 - B&B
		 String file_of_graph = "";
		 
		 for (int i=0; i<args.length; i++) {			
			if (args[i].equals("-algo")) 	algo = Integer.parseInt(args[++i]);
			if (args[i].equals("-graph")) file_of_graph = args[++i];
		 }	
		 
		 /*** Construct PQBC Graph ***/
		 //System.out.println("\n*** PQBC Graph: ***");
		 Graph G = new Graph();
		 
		 try {
		 String line;
		 BufferedReader reader = new BufferedReader(new FileReader(file_of_graph));
		 HashMap<String, String> neighbors = new HashMap<String, String>();
		 while ((line = reader.readLine()) != null) {
		        String[] parts = line.split(":");
		        if (parts.length >= 2)
		        {
		            String key = parts[0];
		            Integer value = Integer.parseInt(parts[2]);
		            G.addVertex(key, value);
		            neighbors.put(key, parts[1]);
		        } else {
		            System.out.println("ignoring line: " + line);
		        }
		    }
		 reader.close();
		 
		 for(String pat : neighbors.keySet()) {
			 String[] nbrs = neighbors.get(pat).split(" ");
			 for(String nbr : nbrs) {
				 G.addEdge(pat, nbr);
			 }
		 }
		 
		 } catch (IOException e) { e.printStackTrace(); }
		 
		 
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
