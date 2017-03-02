package optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

// Example input parameters: -k 10 -l 10 -t 5

public class Main {
	
	public static void main(String[] args) {
		 
		 /*** Read the input parameters and generate random rates of events per type ***/
		 int k = 0;
		 int l = 0;
		 int t = 0;	  
		 int algo = 0;
		 
		 for (int i=0; i<args.length; i++) {
			if (args[i].equals("-k"))		k = Integer.parseInt(args[++i]);
			if (args[i].equals("-l"))		l = Integer.parseInt(args[++i]);
			if (args[i].equals("-t")) 		t = Integer.parseInt(args[++i]);
			if (args[i].equals("-algo")) 	algo = Integer.parseInt(args[++i]);
		 }	
		 
		 HashMap<String,Integer> rates = new HashMap<String,Integer>();
		 Random random = new Random();
		 for (int i=1; i<=t; i++) {
			 int rate = random.nextInt(t) + 1; 
			 rates.put(i+"", rate);
		 }		 
		 		 
		 /*** Create k random patterns of length l using t types ***/
		 System.out.println("\n*** " + k + " random patterns of length " + l + " composed of " + t + " event types: ***");
		 ArrayList<Pattern> randomPatterns = PatternGenerator.getRandomPatterns(k,l,t);
		 
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
