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
					//get list of strings, one for each line
					List<String> list = x.getValue();
					
					//for each string/line ->n° of line = i
					for(int i=0;i<list.size();i++){
						String curr_s = list.get(i).toLowerCase().replaceAll("[^a-z0-9]", " ");
						String[] words = curr_s.split(" ");
						
						//for each word in the line
						for(int j=1; j<words.length; j++){
							if(words[j].length()>3){
								String key= words[j]+", "+ x.getKey();
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
		
		elements.forEach(
				x -> {
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
			Writer.write(this.file_write, output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}


	
	

	
	
}
