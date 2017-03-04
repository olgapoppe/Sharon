package event;

import java.util.ArrayList;

public class Event {
	
	public String type; 
	public ArrayList<Event> pointers;
	
	public Event (String t) {
		type = t;
		pointers = new ArrayList<Event>();
	}

	public static Event parse (String line, String type) {
		Event event;
		if (type.equals("commerce")) { 
			event = Commerce.parse(line); 
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
