package event;

public class Traffic extends Event {
	
	Traffic (String type) {
		super(type);
	}
	
	public static Traffic parse (String line) {
		
		String[] values = line.split(",");
		
		//int sec = Integer.parseInt(values[1]);
        //int vid = Integer.parseInt(values[2]);          	
    	//int spd = Integer.parseInt(values[3]);
    	//int xway = Integer.parseInt(values[4]);
    	String lane = values[5];
    	//int dir = Integer.parseInt(values[6]);
    	//int seg = Integer.parseInt(values[7]);
    	//int pos = Integer.parseInt(values[8]); 
		
		return new Traffic(lane);
	}
}