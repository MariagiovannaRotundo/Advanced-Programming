package myMapReduce;

public class Main {

	public static void main(String[] args) {
		
		CountingWords c= new CountingWords("output_counting");
		c.start();
		
		//InvertedIndex i= new InvertedIndex("output_inverted");
		//i.start();

	}

}
