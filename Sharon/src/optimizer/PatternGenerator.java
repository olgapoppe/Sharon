package optimizer;

import java.util.ArrayList;
import java.util.Random;

public class PatternGenerator {
	
	// Generates k short patterns of length l using t event types 
	// Generates n long pattern of length m*l from these short patterns 
	public static ArrayList<Pattern> getPatterns(int k, int l, int t, int n, int m) {
		 
		 ArrayList<Pattern> long_patterns = new ArrayList<Pattern>();		 
		 ArrayList<Pattern> short_patterns = RandomPatternGenerator.getRandomPatterns(k,l,t);
		 
		 Random random = new Random();
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
		}
		return long_patterns;
	 }

}
