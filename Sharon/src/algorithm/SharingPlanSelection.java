package algorithm;

import java.util.*;

public class SharingPlanSelection {

	public static void main(String[] args) {
		Pattern p1 = new Pattern("ABC", 7, new HashSet<String>(Arrays.asList("q1", "q3")));
		Pattern p2 = new Pattern("CDF", 1, new HashSet<String>(Arrays.asList("q1", "q2", "q5")));
		Pattern p3 = new Pattern("FA", 8, new HashSet<String>(Arrays.asList("q2", "q3", "q4", "q5", "q6")));
		Pattern p4 = new Pattern("FAM", 2, new HashSet<String>(Arrays.asList("q4", "q6")));
		Pattern p5 = new Pattern("DFA", 4, new HashSet<String>(Arrays.asList("q2", "q4", "q5")));
		Pattern p6 = new Pattern("XY", 5, new HashSet<String>(Arrays.asList("q1", "q7")));
		
		Pattern p7 = new Pattern("GH", 8, new HashSet<String>(Arrays.asList("q1", "q2", "q3", "q4")));
		Pattern p8 = new Pattern("GHI", 7, new HashSet<String>(Arrays.asList("q1", "q2")));
		Pattern p9 = new Pattern("JDG", 2, new HashSet<String>(Arrays.asList("q3", "q4")));
		Pattern p10 = new Pattern("JDGH", 4, new HashSet<String>(Arrays.asList("q3", "q4")));
		
		Pattern p11 = new Pattern("KP", 4, new HashSet<String>(Arrays.asList("q4", "q5", "q6", "q1")));
		Pattern p12 = new Pattern("KPO", 1, new HashSet<String>(Arrays.asList("q4", "q5")));
		Pattern p13 = new Pattern("LNC", 1, new HashSet<String>(Arrays.asList("q2", "q7")));
		Pattern p14 = new Pattern("GNKF", 1, new HashSet<String>(Arrays.asList("q6", "q2")));

		Graph G = construct(new HashSet<Pattern>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14)));
		
		/*
		System.out.println(G);
		
		G.removeVertex("CDE");
		System.out.println(G);
		for (String s : G.getVnames()) {
			System.out.println(s + " weight: " + G.getVertex(s).getWeight() + ", degree: " + G.getVertex(s).getDegree());
		}
		System.out.println(G.numVertices() + " vertices and " + G.numEdges() + " edges");
		*/
		
		// Exhaustive
		long startexh = System.currentTimeMillis();
		Set<String> exh_sol = exhaustive(G);
		long endexh = System.currentTimeMillis();
		
		// GWMIN
		long startgwmin = System.currentTimeMillis();		
		Set<String> gwmin_sol = gwmin(G);		
		long endgwmin = System.currentTimeMillis();
		
		// Print out results
		System.out.println("GWMIN:");
		for (String s : gwmin_sol) {
			System.out.println(s);
		}
		
		long durationgwmin = endgwmin - startgwmin;
		System.out.println("Time: " + durationgwmin + "\n");
		
		System.out.println("EXHAUSTIVE:");
		for (String s : exh_sol) {
			System.out.println(s);
		}
		
		long durationexh = endexh - startexh;
		System.out.println("Time: " + durationexh + "\n");
	}

	public static Graph construct(Set<Pattern> F) {
		Graph G = new Graph();
		Set<Pattern> added = new HashSet<Pattern>();
		for (Pattern p : F) {
			if (p.getBValue()>0) {
				G.addVertex(p.toString(), p.getBValue());
				//System.err.println(p);
				for (Pattern p_prime : added) {
					if(p.conflictsWith(p_prime)) {
						G.addEdge(p.toString(), p_prime.toString());
					}
				}
				added.add(p);
			}
		}
		
		return G;
	}
	
	public static Graph reduce(Graph G) {
		return G;
	}
	
	// gwmin destroys the graph, so if you want to save the graph, make a copy of it.
	public static Set<String> gwmin(Graph G) {
		Set<String> I = new HashSet<String>();
		int max;
		int max_temp;
		Vertex v_temp;
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
				max_temp = v_temp.getWeight() / (v_temp.getDegree() + 1);
				if (max_temp > max) {
					max = max_temp;
					v_i = v_id;
				}
			}
			I.add(v_i);
			N = G.getNbrs(v_i);
			for (String nbr_id : N) {
				G.removeVertex(nbr_id);
			}
			G.removeVertex(v_i);
		}
		return I;
	}
	
	public static int score(Graph G, LinkedList<String> plan) {
		int s = 0;
		for (String vname : plan) {
			s += G.getVertex(vname).getWeight();
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
	
	public static LinkedList<LinkedList<String>> getNextLevel(Graph G, LinkedList<LinkedList<String>> Parents, boolean exh) {
		LinkedList<LinkedList<String>> Children = new LinkedList<LinkedList<String>>();
		LinkedList<String> Pi, Pj, u;
		if (exh) {
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
		}
		return Children;
	}
	
	public static Set<String> BnB(Graph G) {
		Set<String> opt = new HashSet<String>();
		return opt;
	}
	
	public static Set<String> exhaustive(Graph G) {
		Set<String> opt = new HashSet<String>();
		int max = 0;
		LinkedList<LinkedList<String>> Level = new LinkedList<LinkedList<String>>();
		
		for (String vname : G.getVnames()) {
			Level.add(new LinkedList<String>(Arrays.asList(vname)));
			if (G.getVertex(vname).getWeight() > max) {
				opt.clear();
				opt.add(vname);
				max = G.getVertex(vname).getWeight();
			}
		}
		
		for (int i = 2; i <= G.numVertices(); i++) {
			Level = getNextLevel(G, Level, true);
			for (LinkedList<String> P : Level) {
				if (isValid(G, P) && score(G, P) > max) {
					opt.clear();
					opt.addAll(P);
					max = score(G, P);
				}
			}
		}
		return opt;
	}

}
