package optimizer2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
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
			if (!G.hasEdge(v, w) && !v.equals(w)) {
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
			//System.err.println(v + " update maximal score " + NN);
			p += NN;
		}
		System.err.println(v + " final maximal score " + p);
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
	public static HashSet<Pattern> getClique(Graph G, String v, HashMap<String,Integer> rates) {
		HashSet<Pattern> C = new HashSet<Pattern>();
		Stack<Pattern> L_c = new Stack<Pattern>();
		Stack<Pattern> L_n = new Stack<Pattern>();
		Set<String> Q_c; // queries that cause a conflict
		HashSet<Set<String>> Qcombos = new HashSet<Set<String>>();
		
		L_c.push(G.getVertex(v));
		C.add(G.getVertex(v));
		
		while (!L_c.isEmpty()){
			Pattern vert = L_c.pop();
			
			for (String u : G.getNbrs(v)) {
				if (vert.conflictsWith(G.getVertex(u))) {
					Q_c = G.getVertex(v).getQueries();
					Q_c.retainAll(G.getVertex(u).getQueries());
					
					int sizeNbrComb = 0; // Only remove a number of queries such that the neighbor can remove the others
					while (G.getVertex(u).getQueries().size() - sizeNbrComb > 1 && sizeNbrComb < Q_c.size()) {
						
						Set<String> Q_prime = vert.getQueries();
						Q_prime.removeAll(Q_c);
						if (Q_prime.size()>1 && !Qcombos.contains(Q_prime)) {
							Qcombos.add(Q_prime);
							Pattern v_prime = new Pattern(v);
							for (String p : Q_prime) {
								v_prime.add2Patterns(p);
							}
							v_prime.getBValue(rates);
							if (v_prime.getBValue()>0) {
								L_n.push(v_prime);
								C.add(v_prime);
							}
						}
						
						sizeNbrComb++;
					}
				}
			}
			
			if (L_c.isEmpty()) {
				L_c = L_n;
				L_n = new Stack<Pattern>();
			}
		}
		
		return C;
	}
	
	// Sharing conflict resolution
	public static Graph expand(Graph G_B, HashMap<String,Integer> rates) {
		Graph G_E = new Graph();
		HashSet<Pattern> C;
		for (String v : G_B.getVnames()) {
			C = getClique(G_B, v, rates);
			Set<String> c_names = G_E.addClique(C);
			for (String v_prime : c_names) {
				for (String u : G_E.getVnames()) {
					if (G_E.getVertex(v_prime).conflictsWith(G_E.getVertex(u)) && !G_E.getVertex(u).toString().equals(v)) {
						//System.err.println("\nadded edge between " + v_prime + " and " + u);
						G_E.addEdge(v_prime, u);
					}
				}
			}
		}
		
		return G_E;
	}
	
	// sharon reduces the graph, so if you want to save the graph, make a copy of it.
	public static Map<String, String> sharon(Graph G, HashMap<String,Integer> rates) {
		Map<String, String> S_map = new HashMap<String, String>(); // to return
		Set<String> R, T, opt;

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
			R = new HashSet<String>();
			
			// Graph Expansion
			long startExpansion = System.currentTimeMillis();
			
			// Calculate min of basic graph before expansion begins
			min = 0;
			for (String v_id : comp.getVnames()) {
				v_temp = comp.getVertex(v_id);
				min += (double) v_temp.getBValue() / (v_temp.getDegree() + 1);
			}
			
			comp = expand(comp, rates);
			
			long endExpansion = System.currentTimeMillis();
			
			durExpansion += (endExpansion - startExpansion);
			
			long startReduction = System.currentTimeMillis();
			
			System.out.println("\nEXPANDED COMPONENT:\n" + comp);
			System.out.println("EXPANDED Vertices:");
			for (String vtestexpansion : comp.getVnames()) {
				System.out.println(vtestexpansion + " in patterns " + comp.getVertex(vtestexpansion).patternsToString());
			}
			System.out.println("\nEXPANDED Number of Vertices: " + comp.numVertices());
			System.out.println("EXPANDED Number of Edges: " + comp.numEdges());
			
			// Graph reduction
			reduce = true;
			
			while (reduce) {
				T = new HashSet<String>(); // non-beneficial
				//System.err.println("min is " + min);
				
				for (String vname : comp.getVnames()) {
					if (comp.getVertex(vname).getDegree()==0) {
						if (!R.contains(vname)) { R.add(vname); }
					} else if (pscore(comp,vname) < min) {
						T.add(vname);
					}
				}
				
				for (String vname : T) {
					comp.removeVertex(vname);
					System.err.println("reduction removed " + vname);
				}
				
				if (T.size()==0) {
					reduce = false;
				}
			}
			
			for (String vname : R) {
				S_map.put(vname, comp.getVertex(vname).patternsToString());
				comp.removeVertex(vname);
				//System.err.println("reduction removed and saved " + vname);
			}
			
			long endReduction = System.currentTimeMillis();
			durReduction += (endReduction - startReduction);
			
			// Start Sharing Plan Selection
			long startSharon = System.currentTimeMillis();
			opt = new HashSet<String>(); // used to store optimal sub-plans
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
			for (String optname : opt) {
				S_map.put(optname, comp.getVertex(optname).patternsToString());
			}
			long endSharon = System.currentTimeMillis();
			durSharon += endSharon - startSharon;
		}
		
		for (String s : S_map.keySet()) { 
			M += (s.length() + S_map.get(s).length())*2;
		}
		
		System.out.println("\nSize: " + M);
		System.out.println("\nDuration Expansion: " + durExpansion);
		System.out.println("\nDuration Reduction: " + durReduction);
		System.out.println("\nDuration Sharing Plan Selection: " + durSharon);
		
		return S_map;
	}
	
	/***Exhaustive***/
	public static Map<String, String> exhaustive(Graph G, HashMap<String,Integer> rates) {
		long startExpansion = System.currentTimeMillis();
		G = expand(G, rates);
		long endExpansion = System.currentTimeMillis();
		
		System.out.println("\nDuration Expansion: " + (endExpansion - startExpansion));
		
		System.out.println("\nEXPANDED GRAPH:\n" + G);
		System.out.println("EXPANDED Vertices:");
		for (String vtestexpansion : G.getVnames()) {
			System.out.println(vtestexpansion + " in patterns " + G.getVertex(vtestexpansion).patternsToString());
		}
		System.out.println("\nEXPANDED Number of Vertices: " + G.numVertices());
		System.out.println("EXPANDED Number of Edges: " + G.numEdges());
		
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
