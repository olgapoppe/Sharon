package event;

public class Taxi extends Event {
	
	/*String vendor_id; //CMT
	String pickup_datetime; //2014-10-01,08:55:07
	String dropoff_datetime; //2014-10-01,09:11:03
	int passenger_count; //1
	String trip_distance; //2.2000000000000002
	String pickup_longitude; //-74.005866999999995
	String pickup_latitude; //40.737569999999998
	int rate_code; //1
	String store_and_fwd_flag; //Y
	String dropoff_longitude; //-74.015534000000002
	String dropoff_latitude; //40.708277000000002
	String payment_type;
	int fare_amount; //12
	int surcharge; //0
	String mta_tax; //0.5
	int tip_amount; //1
	String tolls_amount; //0
	String total_amount; //13.5*/
	
	Taxi (String type) {
		super(type);
	}
	
	public static Taxi parse (String line) {
		
		String[] trip = line.split(",");		
		String type = "";
		if (trip.length>=5) { 
			type=trip[4].substring(0,1);
			//System.out.println(type);
		}		
		return new Taxi(type);
	}
}
