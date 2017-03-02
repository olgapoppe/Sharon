package optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import iogenerator.*;

// -n 10 -m 5 -k 3 -l 5 -t 5 -stream ../../../commerce/stream.txt -queries ../../../commerce/queries.txt
//-n 10 -m 5 -k 3 -l 5 -t 4 -queries ../../../commerce/queries.txt

public class Main {
	
	public static void main(String[] args) {
		 
		 /*** Read the input parameters and generate random rates of events per type ***/
		 int n = 0; // n is the number of long patterns
		 int m = 0; // m*l is the length of long patterns
		 int k = 0; // number of short patterns
		 int l = 0; // length of short patterns
		 int t = 0;	 // event types 
		 int algo = 0; // algorithm: 0 - Exhaustive search, 1 - GWMIN, 2 - B&B
		 String file_of_queries = "";
		 String file_of_stream = "";
		 
		 for (int i=0; i<args.length; i++) {
			if (args[i].equals("-n"))		n = Integer.parseInt(args[++i]);
			if (args[i].equals("-m"))		m = Integer.parseInt(args[++i]);
			if (args[i].equals("-k"))		k = Integer.parseInt(args[++i]);
			if (args[i].equals("-l"))		l = Integer.parseInt(args[++i]);
			if (args[i].equals("-t")) 		t = Integer.parseInt(args[++i]);
			if (args[i].equals("-algo")) 	algo = Integer.parseInt(args[++i]);
			if (args[i].equals("-stream")) 	file_of_stream = args[++i];
			if (args[i].equals("-queries")) file_of_queries = args[++i];
		 }	
		 
		 /*** Generate random rates for t event types ***/
		 HashMap<String,Integer> rates = new HashMap<String,Integer>();
		 Random random = new Random();
		 System.out.println("\n*** Event rate per event type: ***");
		 for (int i=0; i<t; i++) {
			 int rate = random.nextInt(t) + 1; 
			 rates.put(i+"", rate);
			 System.out.println("Event type " + i + " has rate " + rate);
		 }	
		 
		 /*** Generate input event stream for given rates per event type ***/
		 if (file_of_stream.length()>0) StreamGenerator.getStream(rates,file_of_stream);		 
		 		 
		 /*** Generate n patterns of length m*l using t event types ***/
		 System.out.println("\n*** " + n + " patterns of length " + (m*l) + " composed of " + t + " event types: ***");
		 ArrayList<Pattern> randomPatterns = PatternGenerator.getPatterns(k,l,t,n,m,file_of_queries);
		 
		 /*** Get frequent patterns from random patterns ***/
		 System.out.println("\n*** Frequent patterns: ***");
		 HashMap<String,Pattern> frequentPatterns = CCSpan.getFrequentPatterns(randomPatterns,rates);
		 
		 /*** Get shared patterns from frequent patterns ***/
		 System.out.println("\n*** Shared patterns created by " + algo + " algorithm: ***");
		 ArrayList<Pattern> sharedPatterns = new ArrayList<Pattern>();
		 long start =  System.currentTimeMillis();
		 /*switch (algo) {
		 case 0: sharedPatterns = ExhautiveSearch(frequentPatterns);
		 case 1: sharedPatterns = GWMIN(frequentPatterns);
		 case 2: sharedPatterns = BnB(frequentPatterns);
		 }*/	
		 long end =  System.currentTimeMillis();
		 System.out.println("Duration: " + (end - start));
	}
}
