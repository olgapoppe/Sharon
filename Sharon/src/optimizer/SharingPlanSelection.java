package optimizer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class SharingPlanSelection {
	// gwmin destroys the graph, so if you want to save the graph, make a copy of it.
	public static Set<String> gwmin(Graph G) {
		Set<String> I = new HashSet<String>();
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
				max_temp = (double) v_temp.getBValue() / (v_temp.getDegree() + 1);
				if (max_temp > max) {
					max = max_temp;
					v_i = v_id;
				}
			}
			//System.err.println(v_i + " BValue " + max);
			I.add(v_i);
			N = G.getNbrs(v_i);
			for (String nbr_id : N) {
				G.removeVertex(nbr_id);
			}
			G.removeVertex(v_i);
		}
		
		int M = 0;
		for (String s : I) { M += s.length()*2; }
		System.out.println("Size: " + M);
		
		return I;
	}
	
	public static int score(Graph G, LinkedList<String> plan) {
		int s = 0;
		for (String vname : plan) {
			s += G.getVertex(vname).getBValue();
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
		int p = G.getVertex(v).getBValue();
		for (String w : G.getVnames()) {
			if (!G.hasEdge(v, w)) {
				p += G.getVertex(w).getBValue();
			}
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
	
	// sharon reduces the graph, so if you want to save the graph, make a copy of it.
	public static Set<String> sharon(Graph G) {
		Set<String> opt = new HashSet<String>();
		Set<String> S = new HashSet<String>();
		Set<String> R, T;
		int max = 0;
		double min;
		boolean reduce;
		Pattern v_temp;
		LinkedList<LinkedList<String>> Level = new LinkedList<LinkedList<String>>();
		
		// Graph reduction
		reduce = true;
		
		while (reduce) {
			R = new HashSet<String>();
			T = new HashSet<String>();
			min = 0;
			for (String v_id : G.getVnames()) {
				v_temp = G.getVertex(v_id);
				min += (double) v_temp.getBValue() / (v_temp.getDegree() + 1);
			}
			
			//System.err.println(min);
			
			for (String vname : G.getVnames()) {
				if (G.getVertex(vname).getDegree()==0) {
					R.add(vname);
				} else if (pscore(G,vname) < min) {
					T.add(vname);
				}
			}
			
			for (String vname : R) {
				G.removeVertex(vname);
				//System.err.println("reduction removed and saved " + vname);
			}
			
			for (String vname : T) {
				G.removeVertex(vname);
				//System.err.println("reduction removed " + vname);
			}
			
			if (R.size()==0 && T.size()==0) {
				reduce = false;
			} else {
				S.addAll(R);
			}
		}
		
		// Start BnB
		int M = 0;
		int tempM = 0;
		
		for (String vname : G.getVnames()) {
			Level.add(new LinkedList<String>(Arrays.asList(vname)));
		}
		
		while (Level.size() > 0) {
			tempM = 0;
			for (LinkedList<String> P : Level) {
				if (score(G, P) > max) {
					opt.clear();
					opt.addAll(P);
					max = score(G, P);
				}
				
				for (String s : P) { tempM += s.length()*2; }
				if (tempM > M) { M = tempM; }
				
			}
			Level = getNextLevel(G, Level, true);
		}
		opt.addAll(S);
		System.out.println("Size: " + M);
		return opt;
	}
	
	public static Set<String> exhaustive(Graph G) {
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
		System.out.println("Size: " + M);
		return opt;
	}
}
