package event;

public class Commerce extends Event {
	
	Commerce (String type) {
		super(type);
	}
	
	public static Commerce parse (String line) {
		return new Commerce(line);
	}
}
