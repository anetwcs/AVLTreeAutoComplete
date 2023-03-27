/*
 *  CircularArrayFIFOQueue
 *
 *                                                        Jun 26, 2021
 *                                                 Edited by Young You
 *
 *  Pinned Notes :
 *    None.
 *
 */

package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

import javax.swing.*;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {

    private int         offset;             // The offset where the first item exists.
    private int         size;               // Actual occupancy size.
    private final E[]   data;


    /**********************************************************************
    // CircularArrayFIFOQueue
    // Constructor
    /**********************************************************************/
    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);

        // Allocate and initialize memory block
        data = allocateArray(capacity);
        clear();
    }


    /**********************************************************************
    // allocateArray
    // Allocate array memory block
    // This method was wrapped to remove cast warning messages.
    /**********************************************************************/
    @SuppressWarnings("unchecked")
    private E[] allocateArray(int capacity) {
        return((E[]) new Comparable[capacity]);
    }


    /**********************************************************************
    // getIthOffset
    // Return ith offset position
    /**********************************************************************/
    private int getIthOffset(int i) {
        return((offset + i) % capacity());
    }


    /**********************************************************************
    // add
    // Add an element into array
    /**********************************************************************/
    @Override
    public void add(E work) {
        if(isFull()) throw new IllegalStateException();

        data[getIthOffset(size)] = work;
        ++ size;
    }


    /**********************************************************************
    // peek
    // View the first element in the data array
    /**********************************************************************/
    @Override
    public E peek() {
        if(!hasWork()) throw new NoSuchElementException();
        return(data[offset]);
    }


    /**********************************************************************
    // peek
    // View the ith element in the data array
    /**********************************************************************/
    @Override
    public E peek(int i) {
        if(!hasWork()) throw new NoSuchElementException();
        if(i < 0 || i >= size()) throw new IndexOutOfBoundsException();

        return(data[getIthOffset(i)]);
    }


    /**********************************************************************
    // next
    // Pop the ith element in the data array
    /**********************************************************************/
    @Override
    public E next() {
        E object = peek();

        // Increase offset and decrease size.
        offset = getIthOffset(1);
        -- size;

        return(object);
    }


    /**********************************************************************
    // update
    // Replaces the ith element in the data array
    /**********************************************************************/
    @Override
    public void update(int i, E value) {
        if(!hasWork()) throw new NoSuchElementException();
        if(i < 0 || i >= size()) throw new IndexOutOfBoundsException();

        data[getIthOffset(i)] = value;
    }


    /**********************************************************************
    // size
    // Return the actual occupancy size
    /**********************************************************************/
    @Override
    public int size() {
        return(size);
    }


    /**********************************************************************
    // clear
    // Initialize the array
    /**********************************************************************/
    @Override
    public void clear() {
        offset = 0;
        size   = 0;
    }


    /**********************************************************************
    // compareTo
    // Return which one is alphabetical bigger.
    /**********************************************************************/
    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {

        int otherSize = other.size();
        int length    = Math.min(size, otherSize);

        for(int index = 0; index < length; ++ index) {
            int compare = peek(index).compareTo(other.peek(index));
            if(compare != 0) return(compare);
        }

        if(size > otherSize) return(1);
            else if(size < otherSize) return(-1);
            else return(0);

    }


    /**********************************************************************
    // equals
    // Return whether they are the same string or not.
    /**********************************************************************/
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        } else {
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
            return(compareTo(other) == 0);
        }
    }


    /**********************************************************************
    // hashCode
    // Return hashcode for object
    /**********************************************************************/
    @Override
    public int hashCode() {
        int hashValue = 0;

        for(int index = 0; index < size; ++ index) {
            // hashCode() return ascII code. Then we multiply proper value to avoid collision.
            hashValue = hashValue * 31 + peek(index).hashCode();
            // System.out.println(String.format("Hash: %d %d = %d", index, peek(index).hashCode(), hashValue));
        }

        return(hashValue);
    }


    /**********************************************************************
    // hashCode_Experiment
    // Hashcode function for experiments
    /**********************************************************************/
    static public final int HASH_RANGE = Integer.MAX_VALUE;
    /*
    private int hashCode_Experiment() {
        int hashValue = 0;

        // First, we will make a reasonable basic hash code.
        for(int index = 0; index < size; ++ index) {
            // hashCode() return ascII code. Then we multiply proper value to avoid collision.
            hashValue = hashValue * 31 + peek(index).hashCode();
        }

        // Then, we will forcibly restrict the range of hash between 0 ~ HASH_RANGE-1
        if(hashValue < 0) hashValue *= -1;
        hashValue %= HASH_RANGE;

        return(hashValue);
    }
    */

}
