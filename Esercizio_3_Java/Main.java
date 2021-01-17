package esercizio3;


public class Main {

	public static void main(String[] args) {
		
		String pack="esercizio3";
		
		//deserialiazion using class Student.java
		
		Object [] s = XMLDeserializer.deserialize("Studentclass.txt",pack);
		
		if(s != null){
			System.out.print("Number of elements: "+s.length+"\n\n");
			for(int i=0;i<s.length;i++){
				print((Student)s[i]);
			}
			System.out.print("\n");
		}
		
		//deserialiazion using class Event.java -> Error: not deserializable
		
		Object [] e = XMLDeserializer.deserialize("Eventclass.txt", pack);
		
		if(e != null){
			System.out.print("Number of elements: "+e.length+"\n");
			for(int i=0;i<e.length;i++){
				printEvent((Event)e[i]);
			}
		}
	
	}
	
	public static void print(Student s) {
		System.out.print(s.getFirstname()+ " ");
		System.out.print(s.getLastName()+ " ");
		System.out.print(s.getAge()+ " ");
		System.out.println(s.getExams());
	}
	
	
	public static void printEvent(Event e) {
		System.out.print(e.getName()+ " ");
		System.out.print(e.getCity()+ " ");
		System.out.print(e.getStaff()+ " ");
		System.out.println(e.getParticipants());
	}

}
