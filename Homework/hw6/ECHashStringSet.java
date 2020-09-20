import java.util.*;

/** A set of String values.
 *  @author Brian Faun
 */
class ECHashStringSet implements StringSet {
    private double _max = 5;
    private LinkedList<String>[] hash;
    private int length;

    public ECHashStringSet(){
        hash = new LinkedList[5];
        for (int i = 0; i < 5; i++) {
            hash[i] = new LinkedList<String>();
        }
        length = 0;
    }

    @Override
    public void put(String s) {
        if (s == null) {
            return;
        }
        if (_max <= stack()) {
            List lst = asList();
            hash = new LinkedList[2 * hash.length];
            for (int i = 0; i < hash.length; i++) {
                hash[i] = new LinkedList<String>();
            }
            for (Object l : lst) {
                hash[(l.hashCode() & 0x7fffffff) % hash.length].add((String) l);
            }
        }
        hash[(s.hashCode() & 0x7fffffff) % hash.length].add(s);
        length ++;
    }

    @Override
    public boolean contains(String s) {
        int index = (s.hashCode() & 0x7fffffff) % hash.length;
        return hash[index].contains(s);
    }

    @Override
    public List<String> asList() {
        LinkedList<String> lst = new LinkedList<String>();
        for(LinkedList<String> l : hash) {
            lst.addAll(l);
        }
        return lst;
    }

    private double stack() {
        return (double) length / (double) hash.length;
    }
}
