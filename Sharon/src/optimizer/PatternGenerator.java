package optimizer;

import java.util.ArrayList;
import java.util.Random;

public class PatternGenerator {
	
	 public static ArrayList<String> getRandomPatterns(int k, int l, int t) {
		 
		 ArrayList<String> results = new ArrayList<String>();
		 Random random = new Random();
		 for (int i=0; i<k; i++) {
			 String pattern = "";
			 for (int j=0; j<l; j++) {
				int event_type = random.nextInt(t) + 1;
				pattern += event_type;
			 }
			 results.add(pattern);
			 System.out.println(pattern);
		}
		return results;
	 }
}
