package esercizio2;

//class to test the serialization

@XMLable
public class Event {
	
	//fields
	
	@XMLfield(type = "String")
	private String name;
	
	@XMLfield(type = "String", name="city")
	private String cityName;
	
	//field not serializable
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