package esercizio3;
import esercizio2.XMLable;
import esercizio2.XMLfield;

//class to try the deserialization (after the serialization)

@XMLable
public class Event {
	
	//fields
	
	@XMLfield(type = "String")
	private String name;
	
	@XMLfield(type = "String", name="city")
	private String cityName;
	
	
	private int staff;
	
	@XMLfield(type = "int", name="people")
	private int participants;
	
	//empty constructor
	public Event(){}
	
	//not empty constructor
	public Event(String name, String cityName, int staff, int participants) {
		this.name = name;
		this.cityName = cityName;
		this.staff = staff;
		this.participants = participants;
	}
	
	//methods to get the value of the fields
	
	public int getStaff(){
		return this.staff;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getParticipants(){
		return this.participants;
	}
	
	public String getCity(){
		return this.cityName;
	}
	
	
}