import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] _items;
    private int _nextAvailableIdx = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        _items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return _items.length == 0 || _items[0] == null;
    }

    // return the number of items on the randomized queue
    public int size() {
        return _nextAvailableIdx;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (_nextAvailableIdx == _items.length) {
            resizeArray(_items.length * 2);
        }
        _items[_nextAvailableIdx++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int idx = StdRandom.uniform(_nextAvailableIdx);
        Item item = _items[idx];

        Item oldItem = _items[--_nextAvailableIdx];
        _items[idx] = oldItem;

        _items[_nextAvailableIdx] = null;

        if (size() == _items.length / 4) {
            resizeArray(_items.length / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return _items[StdRandom.uniform(_nextAvailableIdx)];
    }

    private void resizeArray(int length) {
        if (length < 1) length = 1;

        Item[] newArray = (Item[]) new Object[length];

        for (int i = 0; i < _nextAvailableIdx; i++) {
            newArray[i] = _items[i];
        }
        _items = newArray;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        StdOut.println(rq.isEmpty());
        rq.enqueue("1");
        rq.enqueue("2");
        rq.enqueue("3");
        rq.enqueue("4");
        rq.enqueue("5");
        rq.enqueue("6");
        rq.enqueue("7");
        rq.enqueue("8");
        rq.enqueue("9");
        rq.enqueue("10");

        rq.dequeue();
        rq.dequeue();
        rq.dequeue();

        for (String s : rq) {
            StdOut.println(s);
        }

        StdOut.println(rq.sample());
        StdOut.println(rq.isEmpty());
        StdOut.println(rq.size());
    }


    private class RandomIterator implements Iterator<Item> {
        int _currentDisplayCount = 0;
        int[] _indeces;

        public Item next() {
            if (_indeces == null) {
                _indeces = StdRandom.permutation(_nextAvailableIdx);
            }

            if (_indeces.length == 0 || _currentDisplayCount >= _nextAvailableIdx) {
                throw new NoSuchElementException();
            }
            return _items[_indeces[_currentDisplayCount++]];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return _currentDisplayCount < _nextAvailableIdx;
        }
    }

}
