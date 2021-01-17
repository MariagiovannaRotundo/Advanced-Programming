package esercizio2;

//class to test the serialization

@XMLable
public class Student {
	
	//fields
	
	@XMLfield(type = "String")
	private String firstname;
	
	@XMLfield(type = "String", name="surname")
	private String lastName;
	
	@XMLfield(type = "int")
	private int age;
	
	@XMLfield(type = "int", name="missingexams")
	private int exams;
	
	//empty constructor
	public Student(){}
	
	//not empty constructor
	public Student(String firstname, String lastName, int age, int exams) {
		this.firstname = firstname;
		this.lastName = lastName;
		this.age = age;
		this.exams = exams;
	}
	
	//methods to get the value of the fields
	
	public String getFirstname(){
		return this.firstname;
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public int getAge(){
		return this.age;
	}
	
	public int getExams(){
		return this.exams;
	}
	
}