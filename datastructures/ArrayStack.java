package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {
    private int size;
    private E[] eArray;

    public ArrayStack() {
        size = 0;
        eArray = (E[]) new Object[10];
    }

    @Override
    public void add(E work) {
        /**
         * Expand the size of eArray once it's full by creating a new array of type E
         */
        if (size + 1 > eArray.length) {
            E[] newEArray = (E[]) new Object[eArray.length * 2];

            /**
             * Parse date from the old to the new eArray
             */
            for (int i = 0; i < eArray.length; i++) {
                newEArray[i] = eArray[i];
            }
            eArray = newEArray;
        }
        eArray[size] = work;
        size++;

    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        } else {
            return eArray[size - 1];
        }
    }

    @Override
    public E next() {
        E result = peek();
        size--;
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        eArray = (E[]) new Object[10];
    }
}
