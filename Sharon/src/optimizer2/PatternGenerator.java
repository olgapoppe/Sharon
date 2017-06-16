package optimizer2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PatternGenerator {
	
	// Generates n long patterns of length m*l from k random short patterns of length l 
	public static ArrayList<Pattern> getPatterns(int k, int l, int t, int n, int m, String file_of_queries) {
		 
		 ArrayList<Pattern> long_patterns = new ArrayList<Pattern>();		 
		 ArrayList<Pattern> short_patterns = getRandomPatterns(k,l,t);		 
		 Random random = new Random();
		 
		 try {			 
			File output_file = new File(file_of_queries);
			BufferedWriter output = new BufferedWriter(new FileWriter(output_file));
			
			for (int i=0; i<n; i++) {
				String long_pattern = "";
				for (int j=0; j<m; j++) {
					int random_index = random.nextInt(k);
					Pattern short_pattern = short_patterns.get(random_index);
					long_pattern += short_pattern.pattern;
			 	}
			 	Pattern pattern = new Pattern(long_pattern);
			 	long_patterns.add(pattern);
			 	System.out.println(pattern.toString());
			 	
			 	output.append(long_pattern + "\n");
			}
			output.close();	
		} catch (IOException e) { e.printStackTrace(); }
		return long_patterns;
	 }
	
	// Randomly generates k short patterns of length l using t event types 
	public static ArrayList<Pattern> getRandomPatterns(int k, int l, int t) {
			 
		 ArrayList<Pattern> random_patterns = new ArrayList<Pattern>();
		 Random random = new Random();
		 for (int i=0; i<k; i++) {
			 String p = "";
			 for (int j=0; j<l; j++) {
				int event_type=-1;
				while (event_type<0 || p.contains(event_type + ",")) { event_type = random.nextInt(t); }
				p += event_type + ",";
			 }
			 Pattern pattern = new Pattern(p);
			 random_patterns.add(pattern);
			 //System.out.println(pattern.toString());
		}
		return random_patterns;
	}
}
