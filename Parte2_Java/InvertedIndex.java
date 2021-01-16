package myMapReduce;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;


public class InvertedIndex extends MapReduce<String, String, List<String>, Integer, Integer>{

	private Path path;
	File file_write;
	
	//costructor. ask the user to insert the path of the directory where
	//files are
	public InvertedIndex(String namefile){
		Scanner s = new Scanner(System.in);
	    String string_path= s.nextLine();
	    s.close();
		this.path = Paths.get(string_path);
		this.file_write = new File(namefile+".csv");
		
	}

	
	@Override
	protected Stream<Pair<String,List<String>>> read() {
		try {
			return new Reader(this.path).read();
		} catch (IOException e) {
			return Stream.empty();
		}
	}

	
	@Override 
	protected Stream<Pair<String,Integer>> map(Stream<Pair<String, List<String>>> elements) {
		
		List<Pair<String,Integer>> newList = new ArrayList<>();
		
		elements.forEach(
				//for each file
				x -> {
					//get the list of strings, one for each line of the file
					List<String> list = x.getValue();
					
					//for each string/line isolate the words
					for(int i=0;i<list.size();i++){
						String curr_s = list.get(i).toLowerCase().replaceAll("[^a-z0-9]", " ");
						String[] words = curr_s.split(" ");
						
						//for each word in the line
						for(int j=1; j<words.length; j++){
							if(words[j].length()>3){
								//word + name of file where the word is
								String key= words[j]+", "+ x.getKey();
								//add the Pair with information: word, file
								//an number of line
								newList.add(new Pair<>(key,i));
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
	protected Stream<Pair<String,Integer>> reduce(Stream<Pair<String, List<Integer>>> elements) {
		
		List<Pair<String,Integer>> newList = new ArrayList<>();
		
		//for each string word + file
		elements.forEach(
				x -> {
					//iteration on the list with lines where the word is
					x.getValue().forEach(
						y -> newList.add(new Pair<String,Integer>(x.getKey(), y))
					);
				}
		);
		
		return newList.stream();
	}

	
	@Override
	protected void write(Stream<Pair<String,Integer>> output) {
		try {
			//write the obtained stream on the file indicated by file_write
			Writer.write(this.file_write, output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
