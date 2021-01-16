package myMapReduce;

public class MainCouting {

	public static void main(String[] args) {
		
		//to Count words into files
		CountingWords c= new CountingWords("output_counting");
		c.start();
		
	}

}
