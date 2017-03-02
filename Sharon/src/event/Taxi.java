package event;

public class Taxi extends Event {
	
	Taxi (String type) {
		super(type);
	}
	
	public static Taxi parse (String line) {
		return new Taxi(line);
	}
}
