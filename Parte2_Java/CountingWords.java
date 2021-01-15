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
	
	public CountingWords(String namefile){
		Scanner s = new Scanner(System.in);
	    String string_path= s.nextLine();
	    s.close();
		this.path = Paths.get(string_path);
		this.file_write = new File(namefile+".csv");
		
	}

	
	@Override
	public Stream<Pair<String,List<String>>> read(){
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
					//get list of strings
					List<String> list = x.getValue();
					
					//for each string/line
					for(String s : list){
						String curr_s = s.toLowerCase().replaceAll("[^a-z0-9]", " ");
						String[] words = curr_s.split(" ");
						
						Arrays.sort(words);
						
						if(words.length>0){
				
							String currword = words[0];
							int occ=1;
							
							//for each word
							for(int i=1; i<words.length; i++){
								
								if(words[i].compareTo(currword)==0){
									occ++;
								}
								else{
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
		
		elements.forEach(
			x -> {
					int sum=0;
					List<Integer> list = x.getValue();
					for(Integer element : list){
		                sum += element;
		            }
					
					newList.add(new Pair<>(x.getKey(), sum));
				}
		);
		return newList.stream();
		
	}




	@Override
	protected void write(Stream<Pair<String, Integer>> output) {
		try {
			Writer.write(this.file_write, output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
