package pe.neat;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class TreeMapTests
{
    public static void main(String[] args)
    {
        TreeSet<Node>          set   = new TreeSet<>((o1, o2) -> 0);
        TreeMap<Integer, Node> map   = new TreeMap<>(Integer::compare);
        HashMap<Integer, Node> nodes = new HashMap<>();
        // map.put()
        // map.values()
    }
}
