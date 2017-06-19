package optimizer2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import java.util.ArrayList;

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
	
	public Graph(HashMap<String,Pattern> F, Graph G) {
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
					if(G.hasEdge(p.toString(), p_prime.toString())) {
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
	
	public void addVertex(String lab, int w) {
		Pattern u = V.get(lab);
		if (u==null) {
			Vnames.add(lab);
			u = new Pattern(lab, w);
			V.put(lab, u);
			E.put(u, new HashSet<Pattern>());
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
	
	// generates id names for each vertex in clique
	// draws an edge between each vertex in clique
	public Set<String> addClique(Set<Pattern> C) {
		Set<String> names = new HashSet<String>();
		int count = 1;
		for (Pattern p : C) {
			String id = p.toString() + " ID " + count;
			Pattern u = V.get(id);
			if (u==null) {
				Vnames.add(id);
				V.put(id, p);
				E.put(p, new HashSet<Pattern>());
				nVerts++;
				for (String name : names) {
					//System.err.println("added edge between " + id + " and " + name);
					this.addEdge(id, name);
				}
				names.add(id);
				count++;
			}
		}
		return names;
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
			System.err.println("Edge addition failed between " + fr + " and " + to);
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
	
	public ArrayList<Graph> connectedComp() {
		ArrayList<Graph> components = new ArrayList<Graph>();
		ArrayList<HashMap<String, Pattern>> compPatterns = new ArrayList<HashMap<String, Pattern>>();
		int numComponents = 0;
		HashMap<String, Integer> nodeCompIDs = new HashMap<String, Integer>();
		Set<String> keys = V.keySet();
		
		for (String k : keys) {
			ArrayList<Integer> graphNums = new ArrayList<Integer>();
			// Identify which subgraphs should be combined
			for (String oldK : nodeCompIDs.keySet()) {
				if (this.hasEdge(k, oldK)) {
					graphNums.add(nodeCompIDs.get(oldK));
				}
			}
			// Combine subgraphs
			if (graphNums.size() > 0) { 
				nodeCompIDs.put(k, graphNums.get(0));
			
				for (int i=1; i<graphNums.size(); i++) {
					for (String node : nodeCompIDs.keySet()) {
						if (nodeCompIDs.get(node) == graphNums.get(i)) { nodeCompIDs.put(node, graphNums.get(0)); }
					}
				}
			
			} else {
				nodeCompIDs.put(k, numComponents);
				numComponents++;
			}
			
			this.getVertex(k).setDegree(0);
		}
		
		// Create hashmaps of patterns
		for (int j=0; j<numComponents; j++) {
			HashMap<String, Pattern> H = new HashMap<String, Pattern>();
			for (String k : nodeCompIDs.keySet()) {
				if (nodeCompIDs.get(k)==j) {
					H.put(k, this.getVertex(k));
				}
			}
			compPatterns.add(H);
		}
		
		for (int i=0; i<compPatterns.size(); i++) {
			components.add(new Graph(compPatterns.get(i), this));
		}
		
		return components;
	}
	
	public String toString2() {
		HashMap<String, String> names4print = new HashMap<String, String>();
		int name = 0;
		for (String v : Vnames) {
			names4print.put(v, name+"");
			name++;
		}
		
		String s = "";
		for (Pattern u : V.values()) {
			s += names4print.get(u.toString()) + ":";
			for (Pattern v : E.get(u)) {
				s += names4print.get(v.toString()) + " ";
			}
			s += ":" + u.getBValue();
			s += "\n";
		}
		return s;
	}
	
	public String toString() {
		String s = "";
		for (Pattern u : V.values()) {
			s += u.toString() + ":";
			for (Pattern v : E.get(u)) {
				s += v.toString() + " ";
			}
			s += ":" + u.getBValue();
			s += "\n";
		}
		return s;
	}
}
//Source: https://www.cs.duke.edu/courses/cps100e/fall10/class/11_Bacon/code/Graph.html