package event;

public abstract class Event {
	
	public String type; 
	
	public Event (String t) {
		type = t;
	}

	public static Event parse (String line, String type) {
		Event event;
		if (type.equals("commerse")) { 
			event = Commerse.parse(line); 
		} else {
		if (type.equals("traffic")) { 
			event = Traffic.parse(line); 
		} else {
			event = Taxi.parse(line);
		}}
		return event;
	}
	
	public String toString() {
		return type;
	}
}
