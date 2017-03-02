package event;

public class Commerse extends Event {
	
	Commerse (String type) {
		super(type);
	}
	
	public static Commerse parse (String line) {
		return new Commerse(line);
	}
}
