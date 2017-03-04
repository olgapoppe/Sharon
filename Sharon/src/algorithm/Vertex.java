package algorithm;

public class Vertex {
	private String label;
	private int weight;
	private int degree;
	
	public Vertex(String lab, int w) {
		label = lab;
		weight = w;
		degree = 0;
	}
	
	public void changeDegree(int num) {
		degree += num;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getDegree() {
		return degree;
	}
	
	public String toString() {
		return label;
	}
}
