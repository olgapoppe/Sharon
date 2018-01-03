package optimizer2;

import java.util.Random;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RegGraph {

	public static void main(String[] args) {
		/*** Read the input parameters ***/
		 
		 int n = 0; // number of vertices
		 int k = 0; // even degree
		 String file_of_graph = "";
		 
		 for (int i=0; i<args.length; i++) {
			 
			if (args[i].equals("-n"))		n = Integer.parseInt(args[++i]);
			if (args[i].equals("-k"))		k = Integer.parseInt(args[++i]);
			if (args[i].equals("-graph")) file_of_graph = args[++i];
		 }
		 
		 int m = k/2;
		 Random random = new Random();
		 
		 Graph G = new Graph();
		 for(int i=0; i<n; i++) {
			 int weight = random.nextInt(20) + 100;
			 G.addVertex(i+"", weight);
		 }
		 for(int i=0; i<n-m; i++) {
			 for(int j=0; j<m; j++) {
				 G.addEdge(i+"", i+1+j+"");
			 }
		 }
		 for(int i=0; i<m; i++) {
			 for(int j=0; j<m-i; j++) {
				 G.addEdge(i+"", n-1-j+"");
			 }
		 }
		 for(int i=n-1; i>n-m;i--) {
			 for(int j=0; j<m; j++) {
				 G.addEdge(i+"", i-j-1+"");
			 }
		 }
		 
		 try 
		 {
			 File output_file = new File(file_of_graph);
			 BufferedWriter output = new BufferedWriter(new FileWriter(output_file));
			 output.append(G.toString());
			 output.close();
		 } catch (IOException e) { e.printStackTrace(); }
		 
		 //System.out.println(G);
		 System.out.println("Degrees Check");
		 for(String v : G.getVnames()) {
			 System.out.println(v + " has degree " + G.getVertex(v).getDegree() + " and weight " + G.getVertex(v).getBValue());
		 }
		 
		 double perc = 2.0 * G.numEdges() / (G.numVertices() * (G.numVertices() - 1.0));
		 System.out.println("\nPercentage of Edges " + perc);
	}
}
