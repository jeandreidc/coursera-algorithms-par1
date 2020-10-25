import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> _firstNode;
    private Node<Item> _lastNode;
    private int _size = 0;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return _firstNode == null && _lastNode == null;
    }

    // return the number of items on the deque
    public int size() {
        return _size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        addNode(item, false);
    }

    // add the item to the back
    public void addLast(Item item) {
        addNode(item, true);
    }

    private void addNode(Item item, boolean isReverse) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> newNode = new Node<Item>(item);
        _size++;

        if (_firstNode == null || _lastNode == null) {
            _firstNode = newNode;
            _lastNode = newNode;
            return;
        }

        if (isReverse) {
            _lastNode.Right = newNode;
            newNode.Left = _lastNode;
            _lastNode = newNode;
        } else {
            _firstNode.Left = newNode;
            newNode.Right = _firstNode;
            _firstNode = newNode;
        }
    }

    private Item removeNode(boolean isReverse) {
        if (!isReverse && _firstNode == null || isReverse && _lastNode == null) {
            throw new NoSuchElementException();
        }

        _size--;

        Item item = isReverse ? _lastNode.Item : _firstNode.Item;

        if (isReverse) {
            Node<Item> left = _lastNode.Left;
            _lastNode.Left = null;
            _lastNode = left;

            if (_lastNode != null)
                _lastNode.Right = null;
            else {
                _firstNode = _lastNode;
            }

        } else {
            Node<Item> right = _firstNode.Right;
            _firstNode.Right = null;
            _firstNode = right;

            if (_firstNode != null)
                _firstNode.Left = null;
            else {
                _lastNode = _firstNode;
            }
        }
        return item;

    }

    // remove and return the item from the front
    public Item removeFirst() {
        return removeNode(false);
    }

    // remove and return the item from the back
    public Item removeLast() {
        return removeNode(true);
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new NodeListIterator();
    }


    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> intDeque = new Deque<Integer>();

        StdOut.println(intDeque.isEmpty());

        intDeque.addFirst(1);
        intDeque.addLast(2);
        intDeque.removeFirst();
        intDeque.removeFirst();

        StdOut.println(intDeque.size());
    }

    private class NodeListIterator implements Iterator<Item> {
        private Node<Item> runner = _firstNode;

        public Item next() {
            if (runner == null) {
                throw new NoSuchElementException();
            }

            Item item = runner.Item;
            runner = runner.Right;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return runner != null;
        }
    }

    private class Node<T> {
        public T Item;
        public Node<T> Left;
        public Node<T> Right;

        public Node(T item) {
            Item = item;
        }
    }
}
