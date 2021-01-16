package myMapReduce;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.stream.Stream;

public class Writer {
    public static void write (File dst, Stream<Pair<String, Integer>> res) throws FileNotFoundException {
        PrintStream ps = new PrintStream(dst);
        res.sorted(Comparator.comparing(Pair::getKey))
            .forEach(p -> ps.println(p.getKey() + ", " + p.getValue()));
        ps.close();
    }
    
    //method to sort keys and then values
    public static void writeInverted (File dst, Stream<Pair<String, Integer>> res) throws FileNotFoundException {
        PrintStream ps = new PrintStream(dst);
        res.sorted(Comparator.comparing(Pair<String,Integer>::getKey).thenComparing(Comparator.comparing(Pair::getValue)))
            .forEach(p -> ps.println(p.getKey() + ", " + p.getValue()));
        ps.close();
    }
}
