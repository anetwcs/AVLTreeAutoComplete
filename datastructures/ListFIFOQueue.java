package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {

    private int size;
    private Node<E> front;
    private Node<E> back;

    public class Node<E> {
        private Node<E> next;
        private E data;


        public Node(E data) {
            this(data, null);
        }

        public Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    public ListFIFOQueue() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }


    @Override
    public void add(E work) {
        if (!hasWork()) {
            front = new Node<>(work);
            back = front;
        } else {
            back.next = new Node<>(work);
            back = back.next;
        }
        size++;

    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        } else {
            return front.data;
        }
    }

    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        } else {
            E result = peek();
            size--;
            front = front.next;
            return result;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        front = null;
        back = null;
        size = 0;
    }
}
