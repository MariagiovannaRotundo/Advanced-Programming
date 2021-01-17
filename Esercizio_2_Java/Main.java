package esercizio2;

public class Main {

	public static void main(String[] args) {
		
		//Objects to serialize
		
		Event e1 = new Event("talkJava", "Pisa", 20, 200);
		Event e2 = new Event("talkHaskell", "Campobasso", 10, 150);
		Event e3 = new Event("workshop", "Roma", 150, 2000);
		Event e4 = new Event("talk101", "Pisa", 40, 400);
		Event e5 = new Event("Python", "Macerata", 7, 120);
		Event e6 = new Event("C", "Padova", 100, 1300);
		
		Event[] array1 = new Event[] {e1, e2, e3, e4, e5, e6};
		
		XMLSerializer.serialize(array1, "Eventclass");
	
		Student s1 = new Student("Mario", "Rossi", 22, 10);
		Student s2 = new Student("Marco", "Verdi", 23, 5);
		Student s3 = new Student("Bobby", "Bianchi", 28, 2);
		Student s4 = new Student("Nick", "Miller", 19, 1);
		Student s5 = new Student("Winston", "Bishop", 31, 16);
		
		Student[] array2 = new Student[] {s1, s2, s3, s4, s5};
		
		XMLSerializer.serialize(array2, "Studentclass");
	}

}
