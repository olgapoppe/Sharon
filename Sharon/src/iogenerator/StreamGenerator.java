package iogenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class StreamGenerator {
	
	// Generate input event stream for given rates per event type
	public static void getStream(HashMap<String,Integer> rates, String file_of_stream) {
		
		try {			 
			File output_file = new File(file_of_stream);
			BufferedWriter output = new BufferedWriter(new FileWriter(output_file));			
			
			// Generate a string
			String string = "";
			Set<String> types = rates.keySet();
			for (String type : types) {
				int rate = rates.get(type);
				for (int i=0; i<rate; i++) {
					string += type + "\n";
				}
			}
			//System.out.println("String " + string);	
			
			// Store the string to file
			for (int i=0; i<100; i++) {
				 output.append(string); 
			}			
			
			 output.close();
		} catch (IOException e) { e.printStackTrace(); }			
	 }
}
