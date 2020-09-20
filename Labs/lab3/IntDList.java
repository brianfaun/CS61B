/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        // FIXME: Implement this method and return correct value

        if (this._front == null){
            return 0;
        } else if (this._front._next == null){
            return 1;
        } else {
            int count = 1;
            DNode holder = this._front;
            while (holder._next != null) {
                //System.out.println("i run");
                holder = holder._next;
                count += 1;
            }
            return count;
        }

        /*
        DNode cur = _front;
        int size = 0;

        //Iteratively traverses the list.
        while(cur != null){
            cur = cur._prev;
            size++;
        }

        return size;

         */
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        // FIXME: Implement this method and return correct value
        if (i == 0){
            return _front._val;
        } else if (i > 0){
            DNode holder = this._front;
            while (i != 0){
                i -= 1;
                holder = holder._next;
            }
            return holder._val;
        } else {
            DNode holder = _back;
            while (i != -1){
                i += 1;
                holder = holder._prev;
            }
            return holder._val;
        }
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        // FIXME: Implement this method
        if (_front == null){
            DNode insert = new DNode(d);
            _front = insert;
            _back = insert;
        } else {
            DNode insert = new DNode(d);
            insert._next = _front;
            _front._prev = insert;
            _front = insert;
        }
    }
    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        // FIXME: Implement this method
        /*
        DNode newBack = new DNode(d);
        DNode oldBack = _back;
        oldBack = newBack._prev;
        _back = newBack;
        if (_front == null){
            _front = newBack;
        }
         */
        if (this._front == null) {
            this._front = new DNode(d);
            this._back = this._front;
        } else {
            DNode oldBack = this._back;
            DNode newBack = new DNode (oldBack, d, null);
            this._back._next = newBack;
            this._back = newBack;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        // FIXME: Implement this method
        DNode inserter = new DNode(d);
        DNode a;
        DNode b;
        if(index == 0 || index == -(size()+1)) {
            insertFront(d);
        } else if(index == size() || index == -1) {
            insertBack(d);
        }else {
            a = _front;
            b = _front._next;
            if(0 > index){
                index += size() + 1;
            }
            for(int j = 0; j < index-1; j++) {
                a = a._next;
                b = b._next;
            }

            b._prev = inserter;
            a._next = inserter;

            inserter._prev = a;
            inserter._next = b;
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        // FIXME: Implement this method and return correct value

        int dfront = _front._val;
        if (_front == _back){
            _front = _back = null;
            return dfront;
        }
        DNode pointer = this._front;
        pointer = pointer._next;
        pointer._prev = null;
        this._front = pointer;
        return dfront;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        // FIXME: Implement this method and return correct value
        int dback = _back._val;
        if (_front == _back){
            _front = _back = null;
            return dback;
        }
        DNode pointer = this._back;
        pointer = pointer._prev;
        pointer._next = null;
        this._back = pointer;
        return dback;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        // FIXME: Implement this method and return correct value
        return 0;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        // FIXME: Implement this method to return correct value
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val +"]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
