package optimizer;

import java.util.ArrayList;
import java.util.Random;

public class PatternGenerator {
	
	 public static ArrayList<Pattern> getRandomPatterns(int k, int l, int t) {
		 
		 ArrayList<Pattern> results = new ArrayList<Pattern>();
		 Random random = new Random();
		 for (int i=0; i<k; i++) {
			 String p = "";
			 for (int j=0; j<l; j++) {
				int event_type = random.nextInt(t) + 1;
				p += event_type;
			 }
			 Pattern pattern = new Pattern(p);
			 results.add(pattern);
			 System.out.println(pattern.toString());
		}
		return results;
	 }
}
