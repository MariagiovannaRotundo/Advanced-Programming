package myMapReduce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public abstract class MapReduce<K1,K2,V1,V2,V3>{

    public MapReduce(){}

    public final void start(){
    	
        Stream<Pair<K1,V1>> input_stream = read();
        Stream<Pair<K2,V2>> map_stream = map(input_stream);
        Stream<Pair<K2,List<V2>>> group_stream = groupAndSort(map_stream);
        Stream<Pair<K2,V3>> reduce_stream = reduce(group_stream);
        write(reduce_stream);
		
    }
    
    
    //hotspot methods
    protected abstract Stream<Pair<K1,V1>> read();
    protected abstract Stream<Pair<K2,V2>> map(Stream<Pair<K1,V1>> elements);
    protected abstract int compare(K2 s1, K2 s2);
    protected abstract Stream<Pair<K2,V3>> reduce(Stream<Pair<K2,List<V2>>> elements);
    protected abstract void write(Stream<Pair<K2,V3>> output);

    
    private Stream<Pair<K2,List<V2>>> groupAndSort(Stream<Pair<K2,V2>> elements){
		
    	//aggrega gli eleenti con la stessa chiave in un elemento solo 
    	//con valore associato una lista contenente i valori associati alla 
    	//stessa chiave (nelle varie coppie).  Es: (a,6),(a,1)->(a,[6,1])
    	
    	HashMap<K2, List<V2>> values = new HashMap<>();
    	
    	elements.forEach( x ->{
    		
    		//if the key is not in the hashmap yet, add it
    		if (values.get(x.getKey())==null){
    			values.put(x.getKey(), new ArrayList<>());
    		}
    		
    		values.get(x.getKey()).add(x.getValue());
    		
    		}
		);
    	
    	return values.entrySet().stream().map(x -> new Pair<>(x.getKey(),x.getValue()));
    }
    
    
    
}