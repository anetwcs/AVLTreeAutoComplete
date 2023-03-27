//Edited by An Nguyen

package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.PriorityWorkList;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeap<E> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private Comparator<E> comparator;
    private int size;

    public MinFourHeap(Comparator<E> c) {
        this.comparator = c;
        this.size = 0;
        this.data = (E[]) new Object[10];

    }

    @Override
    public boolean hasWork() {
        return size > 0;
    }

    @Override
    public void add(E work) {
        if (work == null) {
            throw new IllegalArgumentException();
        }
        /**
         * Double the size of the data array if size reached max
         */
        else {
            if (size == data.length) {
                E[] temp = (E[]) new Object[size * 2];
                for (int i = 0; i < data.length; i++) {
                    temp[i] = data[i];
                }
                data = temp;
            }
            /**
             * Create new node by increasing size
             * Find the index of the node that needs to be added by calling percolateUp
             */
            size++;
            int index = percolateUp(size - 1, work);
            data[index] = work;
        }

    }

    @Override
    public E peek() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        } else {
            E nextElement = data[0];
            return nextElement;
        }
    }

    @Override
    public E next() {
        if (!hasWork()) {
            throw new NoSuchElementException();
        } else {
            E nextValue = data[0];
            int index = percolateDown(0, data[size - 1]);
            data[index] = data[size - 1];
            size--;
            return nextValue;
        }
    }

    /**
     * Return the index of the node after percolating up
     *
     * @param nodePosition index of the node that needs to percolate up
     * @param value        value of the node that needs to percolate up
     * @return the index of the node after percolating up
     */
    private int percolateUp(int nodePosition, E value) {
        while (nodePosition > 0 && comparator.compare(value, data[(nodePosition - 1) / 4]) < 0) {
            data[nodePosition] = data[(nodePosition - 1) / 4];
            nodePosition = (nodePosition - 1) / 4;
        }
        return nodePosition;
    }

    private int percolateDown(int nodePosition, E value) {
        while (nodePosition * 4 + 1 < size) {

            //Find the smallest child
            int firstChild = nodePosition * 4 + 1;
            int smallerChild = firstChild;
            for (int i = 1; i < 4; i++) {
                int nextChild = firstChild + i;

                // Check if the nextChild is the last leaf
                if (nextChild > size - 1) {
                    break;
                }
                if (comparator.compare(data[smallerChild], data[nextChild]) > 0) {
                    smallerChild = nextChild;
                }
            }

            //Compare the value of the smallest child with the parent (the percolating-down node)
            if (comparator.compare(data[smallerChild], value) < 0) {
                data[nodePosition] = data[smallerChild];
                nodePosition = smallerChild;
            } else {
                break;
            }
        }
        return nodePosition;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
    }
}
