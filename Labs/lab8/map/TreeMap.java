package map;

public class TreeMap<K extends Comparable<K>, V> implements SimpleMap<K, V> {
    @Override
    public void put(K key, V value) {
        _root = putHelper(_root, key, value);
    }

    @Override
    public V get(K key) {
        return getHelper(_root, key);
    }

    @Override
    public void clear() {
        _root = null;
    }

    /**
     * Returns the node with the new key-value pair added (either to node
     * itself, or a descendant of node).
     *
     * If node is null then a new Node without any descendants is created.
     * Otherwise the function is recursively called on either the left and
     * right descendant based on the rules of BSTs.
     */
    private TreeMapNode putHelper(TreeMapNode node, K key, V value) {
        if (node == null) {
            node = new TreeMapNode(key, value, null, null);
            return node;
        } else if (node._key.compareTo(key) < 0) {
            node._right  = putHelper(node._right, key, value);
        } else if (node._key.compareTo(key) > 0) {
            node._left = putHelper(node._left, key, value);
        } else if (node._key.compareTo(key) == 0) {
            node._value = value;
        }
        return node;
    }

    /**
     * Returns the value associated with key from either node or a descendant
     * of node. If there is no key-value mapping associated with key node or
     * any of its descendants then the function returns null.
     *
     * If node's key does not equal key, then the function is recursively
     * called on either the right or left descendant based on the rules of BSTs.
     */
    private V getHelper(TreeMapNode node, K key) {
        if (node._key.compareTo(key) == 0) {
            return node._value;
        } else if (node._key.compareTo(key) > 0 && node._left != null) {
            return getHelper(node._left, key);
        } else if (node._key.compareTo(key) < 0 && node._right != null) {
            return getHelper(node._right, key);
        } else {
            return null;
        }
    }

    private TreeMapNode _root;

    private class TreeMapNode {

        private TreeMapNode(K key, V value, TreeMapNode left, TreeMapNode right) {
            _key = key;
            _value = value;
            _left = left;
            _right = right;
        }

        public String toString() {
            return "(" + _key.toString() + " -> " + _value.toString() + ")";
        }

        /** Left child of this. */
        private TreeMapNode _left;

        /** Right child of this. */
        private TreeMapNode _right;

        /** Key in the key-value pair represented by this. */
        private K _key;

        /** Value in the key-value pair represented by this. */
        private V _value;
    }
}
