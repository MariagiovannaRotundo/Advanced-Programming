package myMapReduce;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;


public class CountingWords extends MapReduce<String, String, List<String>, Integer, Integer>{

	private Path path;
	File file_write;
	
	
	//costructor. ask the user to insert the path of the directory where
	//files are
	public CountingWords(String namefile){
		Scanner s = new Scanner(System.in);
	    String string_path= s.nextLine();
	    s.close();
		this.path = Paths.get(string_path);
		this.file_write = new File(namefile+".csv");
		
	}

	
	@Override
	protected Stream<Pair<String,List<String>>> read(){
		try {
			return new Reader(this.path).read();
		} catch (IOException e) {
			return Stream.empty();
		}
	}


	@Override
	protected Stream<Pair<String, Integer>> map(Stream<Pair<String, List<String>>> elements) {
		
		List<Pair<String,Integer>> newList = new ArrayList<>();
		
		elements.forEach(
				//for each file
				x -> {
					//get list of strings of the file
					List<String> list = x.getValue();
					
					//for each string/line isolate the words
					for(String s : list){
						String curr_s = s.toLowerCase().replaceAll("[^a-z0-9]", " ");
						String[] words = curr_s.split(" ");
						//sort the words
						Arrays.sort(words);
						//if there are words
						if(words.length>0){
				
							String currword = words[0];
							int occ=1;
							
							//for each word count the occurrences and
							//if its length is greater of 3 add to list 
							//for the stream.
							for(int i=1; i<words.length; i++){
								//an occurrence is found
								if(words[i].compareTo(currword)==0){
									occ++;
								}
								else{//another word is found
									if(currword.length()>3){
										newList.add(new Pair<>(currword, occ));
									}
									currword=words[i];
									occ=1;
								}
							}
							if(currword.length()>3){
								newList.add(new Pair<>(currword, occ));
							}
						}
		            }
				}
		);
		
		return newList.stream();
	}




	@Override
	protected int compare(String s1, String s2) {
		return s1.compareTo(s2);
	}




	@Override
	protected Stream<Pair<String, Integer>> reduce(Stream<Pair<String, List<Integer>>> elements) {
		
		List<Pair<String,Integer>> newList = new ArrayList<>();
		//for each element (w, list number occurrences)
		elements.forEach(
			x -> {
					//sum the occurrences in the list of the word
					int sum=0;
					List<Integer> list = x.getValue();
					for(Integer element : list){
		                sum += element;
		            }
					//add the pair (word, number occurrence)
					newList.add(new Pair<>(x.getKey(), sum));
				}
		);

		return newList.stream();
		
	}




	@Override
	protected void write(Stream<Pair<String, Integer>> output) {
		try {
			//write the obtained stream in the indicated file
			Writer.write(this.file_write, output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
