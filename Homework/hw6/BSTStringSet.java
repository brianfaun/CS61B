import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Brian Faun
 */
public class BSTStringSet implements StringSet, SortedStringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        _root = put(s, _root);
    }

    private Node put(String s, Node n) {
        if (n == null) {
            return new Node(s);
        }
        int a = s.compareTo(n.s);
        if (a > 0) {
            n.right = put(s, n.right);
        } else if (a < 0) {
            n.left = put(s, n.left);
        }
        return n;
    }

    @Override
    public boolean contains(String s) {
        return contains(s, _root);
    }

    private boolean contains(String s, Node n) {
        if (n == null) {
            return false;
        }
        int a = s.compareTo(n.s);
        if (a > 0) {
            return contains(s, n.right);
        } else if (a < 0){
            return contains(s, n.left);
        }
        return true;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> lst = new ArrayList<String>();
        for (String s : this) {
            lst.add(s);
        }
        return lst;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    private static class BSTBoundedIterator implements Iterator<String> {
        BSTBoundedIterator(Iterator source, String low, String high) {
            _source = source;
            _low = low;
            _high = high;
        }

        @Override
        public boolean hasNext() {
            nextInbound();
            return !s.equals("");
        }

        public void nextInbound() {
            s = "";
            while (_source.hasNext()) {
                String inbound = (String) _source.next();
                if (inbound.compareTo(_low) >= 0 && inbound.compareTo(_high) <= 0) {
                    s = inbound;
                    break;
                }
            }
        }

        @Override
        public String next() {
            return s;
        }

        private Iterator _source;
        private String s;
        private String _low;
        private String _high;
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        Iterator source = iterator();
        return new BSTBoundedIterator(source, low, high);
    }

    /** Root node of the tree. */
    private Node _root;
}
