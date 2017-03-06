// OLD - do not use

package algorithm;

import java.util.*; 

public class Graph {
	private LinkedHashSet<String> Vnames;
	HashMap<String, Vertex> V;
	private HashMap<Vertex, HashSet<Vertex>> E;
	private int nVerts;
	private int nEdges;
	
	public Graph() {
		Vnames = new LinkedHashSet<String>();
		V = new HashMap<String, Vertex>();
		E = new HashMap<Vertex, HashSet<Vertex>>();
		nVerts = 0;
		nEdges = 0;
	}
	
	public void addVertex(String lab, int w) {
		Vertex u = V.get(lab);
		if (u==null) {
			Vnames.add(lab);
			u = new Vertex(lab, w);
			V.put(lab, u);
			E.put(u, new HashSet<Vertex>());
			nVerts++;
		}
	}
	
	public void removeVertex(String lab) {
		Vertex v = V.get(lab);
		if (v==null) {
			System.err.println("Vertex removal failed");
			return;			// the vertex doesn't exist
		}
		removeVertex(v, lab);
	}
	
	private void removeVertex(Vertex v, String lab) {	
		for (Vertex u : E.get(v)) {
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
		Vertex u = V.get(fr);
		Vertex v = V.get(to);
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
		Vertex u = V.get(fr);
		Vertex v = V.get(to);
		if (u==null || v==null) {
			System.err.println("Edge removal failed");
			return;			// the vertices don't exist
		}
		removeEdge(u, v);
	}
	
	private void removeEdge(Vertex fr, Vertex to) {
		if (E.get(fr).remove(to)) {
			fr.changeDegree(-1);
			E.get(to).remove(fr);
			to.changeDegree(-1);
			nEdges--;
		}
	}
	
	public HashSet<String> getNbrs(String lab) {
		HashSet<String> nbr_ids = new HashSet<String>();
		HashSet<Vertex> nbrs = E.get(V.get(lab));
		for (Vertex u : nbrs) {
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
	
	public Vertex getVertex(String lab) {
		return V.get(lab);
	}
	
	public String toString() {
		String s = "";
		for (Vertex u : V.values()) {
			s += u + ": ";
			for (Vertex v : E.get(u)) {
				s += v + " ";
			}
			s += "\n";
		}
		return s;
	}
}
//Source: https://www.cs.duke.edu/courses/cps100e/fall10/class/11_Bacon/code/Graph.html