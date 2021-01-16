package myMapReduce;

public class Main {

	public static void main(String[] args) {
		
		//to Count words into files
		CountingWords c= new CountingWords("output_counting");
		c.start();
		
		//to test invertedIndex execution
		//InvertedIndex i= new InvertedIndex("output_inverted");
		//i.start();

	}

}
