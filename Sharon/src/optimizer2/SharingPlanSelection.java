package optimizer2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;

public class SharingPlanSelection {
	// gwmin destroys the graph, so if you want to save the graph, make a copy of it.
	public static Map<String, String> gwmin(Graph G) {
		long startGWMIN = System.currentTimeMillis();
		Map<String, String> I = new HashMap<String, String>();
		double max;
		double max_temp;
		Pattern v_temp;
		String v_i;
		Graph G_i = G;
		Set<String> V;
		Set<String> N;
		while (G_i.numVertices() != 0) {
			max = 0;
			v_i = "";
			V = G_i.getVnames();
			for (String v_id : V) {
				v_temp = G_i.getVertex(v_id);
				max_temp = (double) v_temp.getBValue() / (v_temp.getDegree() + 1.0);
				if (max_temp > max) {
					max = max_temp;
					v_i = v_id;
				}
			}
			//System.err.println(v_i + " BValue " + max);
			I.put(v_i, G.getVertex(v_i).patternsToString());
			N = G.getNbrs(v_i);
			for (String nbr_id : N) {
				G.removeVertex(nbr_id);
			}
			G.removeVertex(v_i);
		}
		long endGWMIN = System.currentTimeMillis();
		
		int M = 0;
		for (String s : I.keySet()) { M += s.length()*2; M += I.get(s).length()*2; }
		System.out.println("\nSize: " + M);
		System.out.println("\nDuration Sharing Plan Selection: " + (endGWMIN - startGWMIN));
		
		return I;
	}
	
	public static int score(Graph Gr, LinkedList<String> plan) {
		int s = 0;
		for (String vname : plan) {
			s += Gr.getVertex(vname).getBValue();
		}
		return s;
	}
	
	public static boolean isValid(Graph G, LinkedList<String> plan) {
		for (int i=0; i<plan.size(); i++) {
			for (int j=i+1; j<plan.size(); j++) {
				if (G.hasEdge(plan.get(i), plan.get(j))) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static int pscore(Graph G, String v) {
		HashMap<String, Integer> non_nbrs = new HashMap<String, Integer>();
		int p = G.getVertex(v).getBValue();
		for (String w : G.getVnames()) {
			if (!G.hasEdge(v, w)) {
				if (non_nbrs.containsKey(G.getVertex(w).toString())) { 
					if (non_nbrs.get(G.getVertex(w).toString()) < G.getVertex(w).getBValue()) {
						non_nbrs.put(G.getVertex(w).toString(), G.getVertex(w).getBValue());
					}
				}
				else {
					non_nbrs.put(G.getVertex(w).toString(), G.getVertex(w).getBValue());
				}
			}
		}
		for (Integer NN : non_nbrs.values()) {
			p += NN;
		}
		return p;
	}
	
	// Because they are so similar, I combined exhaustive and Sharon algorithms together, but separating them might save a little time.
	public static LinkedList<LinkedList<String>> getNextLevel(Graph G, LinkedList<LinkedList<String>> Parents, boolean sharon) {
		LinkedList<LinkedList<String>> Children = new LinkedList<LinkedList<String>>();
		LinkedList<String> Pi, Pj, u;
		for (int i=0; i<Parents.size(); i++) {
			Pi = Parents.get(i);
			for (int j=i+1; j<Parents.size(); j++) {
				Pj = Parents.get(j);
				
				boolean generateChild = true;
				
				if (Pj.size()>1) {
					for (int k = 0; k<Pj.size()-1; k++) {
						if (Pi.get(k)!=Pj.get(k)) {
							generateChild = false;
							break;
						}
					}
				}
				
				if (generateChild && sharon && G.hasEdge(Pi.get(Pi.size()-1), Pj.get(Pj.size()-1))) {
					generateChild = false;
				}
				
				if (generateChild) {
					u = new LinkedList<String>();
					for (int n=0; n<Pi.size(); n++) {
						u.add(Pi.get(n));
					}
					u.add(Pj.get(Pj.size()-1));
					//System.err.println("added " + u);
					Children.add(u);
				}
			}
		}
		return Children;
	}
	
	// Clique generation
	public static HashSet<Pattern> getClique(Graph G, String v) {
		HashSet<Pattern> C = new HashSet<Pattern>();
		
		return C;
	}
	
	// Sharing conflict resolution
	public static Graph expand(Graph G_B) {
		Graph G_E = new Graph();
		HashSet<Pattern> C;
		for (String v : G_B.getVnames()) {
			C = getClique(G_B, v);
			Set<String> c_names = G_E.addClique(C);
			for (String v_prime : c_names) {
				for (String u : G_E.getVnames()) {
					if (G_E.getVertex(v_prime).conflictsWith(G_E.getVertex(u))) {
						G_E.addEdge(v_prime, u);
					}
				}
			}
		}
		
		return G_B;
	}
	
	// sharon reduces the graph, so if you want to save the graph, make a copy of it.
	public static Map<String, String> sharon(Graph G) {
		Set<String> opt = new HashSet<String>(); // used to store optimal sub-plans
		Set<String> S = new HashSet<String>(); // to return
		Set<String> R, T;
		int max;
		double min = 0;
		boolean reduce;
		Pattern v_temp;
		LinkedList<LinkedList<String>> Level = new LinkedList<LinkedList<String>>();
		int M = 0;
		
		ArrayList<Graph> CC = G.connectedComp();
		long durExpansion = 0;
		long durReduction = 0;
		long durSharon = 0;
		
		for (Graph comp : CC) {
			//System.err.println("Component number of vertices: " + comp.numVertices());
			//System.err.println(S);
			
			// Graph Expansion
			long startExpansion = System.currentTimeMillis();
			
			// Calculate min of basic graph before expansion begins
			for (String v_id : comp.getVnames()) {
				v_temp = comp.getVertex(v_id);
				min += (double) v_temp.getBValue() / (v_temp.getDegree() + 1);
			}
			
			comp = expand(comp);
			
			long endExpansion = System.currentTimeMillis();
			
			durExpansion += (endExpansion - startExpansion);
			
			long startReduction = System.currentTimeMillis();
			// Graph reduction
			reduce = true;
			
			while (reduce) {
				R = new HashSet<String>(); // non-conflict
				T = new HashSet<String>(); // non-beneficial
				//System.err.println(min);
				
				for (String vname : comp.getVnames()) {
					if (comp.getVertex(vname).getDegree()==0) {
						R.add(vname);
					} else if (pscore(comp,vname) < min) {
						T.add(vname);
					}
				}
				
				for (String vname : R) {
					comp.removeVertex(vname);
					//System.err.println("reduction removed and saved " + vname);
				}
				
				for (String vname : T) {
					comp.removeVertex(vname);
					//System.err.println("reduction removed " + vname);
				}
				
				if (R.size()==0 && T.size()==0) {
					reduce = false;
				} else {
					S.addAll(R);
				}
			}
			
			long endReduction = System.currentTimeMillis();
			durReduction += (endReduction - startReduction);
			
			// Start Sharing Plan Selection
			long startSharon = System.currentTimeMillis();
			max = 0;
			int tempM = 0;
			
			for (String vname : comp.getVnames()) {
				Level.add(new LinkedList<String>(Arrays.asList(vname)));
			}
			
			while (Level.size() > 0) {
				tempM = 0;
				for (LinkedList<String> P : Level) {
					if (score(comp, P) > max) {
						opt.clear();
						opt.addAll(P);
						max = score(comp, P);
					}
					
					for (String s : P) { tempM += s.length()*2; }
					if (tempM > M) { M = tempM; }
					
				}
				Level = getNextLevel(comp, Level, true);
			}
			S.addAll(opt);
			long endSharon = System.currentTimeMillis();
			durSharon += endSharon - startSharon;
		}
		
		Map<String, String> S_map = new HashMap<String, String>();
		
		for (String s : S) { 
			M += s.length()*2;
			S_map.put(s, G.getVertex(s).patternsToString());
		}
		
		System.out.println("\nSize: " + M);
		System.out.println("\nDuration Expansion: " + durExpansion);
		System.out.println("\nDuration Reduction: " + durReduction);
		System.out.println("\nDuration Sharing Plan Selection: " + durSharon);
		
		return S_map;
	}
	
	/***Exhaustive***/
	public static Map<String, String> exhaustive(Graph G) {
		long startExpansion = System.currentTimeMillis();
		G = expand(G);
		long endExpansion = System.currentTimeMillis();
		
		System.out.println("\nDuration Expansion: " + (endExpansion - startExpansion));
		
		long startExh = System.currentTimeMillis();
		Set<String> opt = new HashSet<String>();
		int max = 0;
		LinkedList<LinkedList<String>> Level = new LinkedList<LinkedList<String>>();
		
		int M = 0;
		int tempM = 0;
		
		for (String vname : G.getVnames()) {
			Level.add(new LinkedList<String>(Arrays.asList(vname)));
		}
		
		for (int i = 1; i <= G.numVertices(); i++) {
			tempM = 0;
			for (LinkedList<String> P : Level) {
				if (isValid(G, P) && score(G, P) > max) {
					opt.clear();
					opt.addAll(P);
					max = score(G, P);
				}
				
				for (String s : P) { tempM += s.length()*2; }
				if (tempM > M) { M = tempM; }
				
			}
			Level = getNextLevel(G, Level, false);
		}
		long endExh = System.currentTimeMillis();
		System.out.println("\nSize: " + M);
		System.out.println("\nDuration Sharing Plan Selection: " + (endExh - startExh));
		
		Map<String, String> opt_map = new HashMap<String, String>();
		for (String s : opt) {
			opt_map.put(s, G.getVertex(s).patternsToString());
		}
		return opt_map;
	}
}
