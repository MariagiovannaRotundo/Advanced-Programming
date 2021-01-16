package myMapReduce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public abstract class MapReduce<K1,K2,V1,V2,V3>{

    public MapReduce(){}

    //method that establish the execution order
    public final void start(){
        Stream<Pair<K1,V1>> input_stream = read();
        Stream<Pair<K2,V2>> map_stream = map(input_stream);
        Stream<Pair<K2,List<V2>>> group_stream = groupAndSort(map_stream);
        Stream<Pair<K2,V3>> reduce_stream = reduce(group_stream);
        write(reduce_stream);
    }
    
    
    //hotspot methods: abstract
    protected abstract Stream<Pair<K1,V1>> read();
    protected abstract Stream<Pair<K2,V2>> map(Stream<Pair<K1,V1>> elements);
    protected abstract int compare(K2 s1, K2 s2);
    protected abstract Stream<Pair<K2,V3>> reduce(Stream<Pair<K2,List<V2>>> elements);
    protected abstract void write(Stream<Pair<K2,V3>> output);

    
    //method to aggregate the elements with the same key
    private Stream<Pair<K2,List<V2>>> groupAndSort(Stream<Pair<K2,V2>> elements){
    	
    	HashMap<K2, List<V2>> values = new HashMap<>();
    	
    	//for each input element
    	elements.forEach( x ->{
    		//if the key is not in the hashmap yet, add to it
    		if (values.get(x.getKey())==null){
    			values.put(x.getKey(), new ArrayList<>());
    		}
    		//add the value to the list of values of the right key
    		values.get(x.getKey()).add(x.getValue());
    		}
		);
    	
    	return values.entrySet().stream().map(x -> new Pair<>(x.getKey(),x.getValue()));
    }
    
    
    
}