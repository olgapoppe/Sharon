package optimizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Graph {
	private LinkedHashSet<String> Vnames;
	HashMap<String, Pattern> V;
	private HashMap<Pattern, HashSet<Pattern>> E;
	private int nVerts;
	private int nEdges;
	
	public Graph() {
		Vnames = new LinkedHashSet<String>();
		V = new HashMap<String, Pattern>();
		E = new HashMap<Pattern, HashSet<Pattern>>();
		nVerts = 0;
		nEdges = 0;
	}
	
	public Graph(HashMap<String,Pattern> F) {
		Vnames = new LinkedHashSet<String>();
		V = new HashMap<String, Pattern>();
		E = new HashMap<Pattern, HashSet<Pattern>>();
		nVerts = 0;
		nEdges = 0;
		Set<Pattern> added = new HashSet<Pattern>();
		for (Pattern p : F.values()) {
			if (p.getBValue()>0) {
				this.addVertex(p);
				for (Pattern p_prime : added) {
					if(p.conflictsWith(p_prime)) {
						this.addEdge(p.toString(), p_prime.toString());
					}
				}
				added.add(p);
			}
		}
	}
	
	public void addVertex(Pattern p) {
		Pattern u = V.get(p.toString());
		if (u==null) {
			Vnames.add(p.toString());
			V.put(p.toString(), p);
			E.put(p, new HashSet<Pattern>());
			nVerts++;
		}
	}
	
	public void removeVertex(String lab) {
		Pattern v = V.get(lab);
		if (v==null) {
			System.err.println("Vertex removal failed");
			return;			// the vertex doesn't exist
		}
		removeVertex(v, lab);
	}
	
	private void removeVertex(Pattern v, String lab) {	
		for (Pattern u : E.get(v)) {
			E.get(u).remove(v);
			u.changeDegree(-1);
			nEdges--;
		}
		E.remove(v);
		Vnames.remove(lab);
		V.remove(lab);
		nVerts--;
	}
	
	public boolean hasVertex(String lab) {
		return V.containsKey(lab);
	}
	
	public boolean hasEdge(String fr, String to){
		return E.get(V.get(fr)).contains(V.get(to));
	}
	
	public void addEdge(String fr, String to) {
		Pattern u = V.get(fr);
		Pattern v = V.get(to);
		if (u==null || v==null) {
			System.err.println("Edge addition failed");
			return;			// the vertices don't exist
		}
		if (E.get(u).add(v)) {
			u.changeDegree(1);
			E.get(v).add(u);
			v.changeDegree(1);
			nEdges++;
		}
	}
	
	public void removeEdge(String fr, String to) {
		Pattern u = V.get(fr);
		Pattern v = V.get(to);
		if (u==null || v==null) {
			System.err.println("Edge removal failed");
			return;			// the vertices don't exist
		}
		removeEdge(u, v);
	}
	
	private void removeEdge(Pattern fr, Pattern to) {
		if (E.get(fr).remove(to)) {
			fr.changeDegree(-1);
			E.get(to).remove(fr);
			to.changeDegree(-1);
			nEdges--;
		}
	}
	
	public HashSet<String> getNbrs(String lab) {
		HashSet<String> nbr_ids = new HashSet<String>();
		HashSet<Pattern> nbrs = E.get(V.get(lab));
		for (Pattern u : nbrs) {
			nbr_ids.add(u.toString());
		}
		return nbr_ids;
	}
	
	public boolean isInd() {
		if (nEdges == 0) {
				return true;
		}
		return false;
	}
	
	public int numVertices() {
		return nVerts;
	}
	
	public int numEdges() {
		return nEdges;
	}
	
	public LinkedHashSet<String> getVnames() {
		// A linked hash set maintains insertion order
		return Vnames;
	}
	
	public Pattern getVertex(String lab) {
		return V.get(lab);
	}
	
	public String toString() {
		String s = "";
		for (Pattern u : V.values()) {
			s += u + ": ";
			for (Pattern v : E.get(u)) {
				s += v + " ";
			}
			s += "\n";
		}
		return s;
	}
}
//Source: https://www.cs.duke.edu/courses/cps100e/fall10/class/11_Bacon/code/Graph.html