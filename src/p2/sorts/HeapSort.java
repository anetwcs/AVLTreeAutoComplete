package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import datastructures.worklists.MinFourHeap;

import java.util.Comparator;

public class HeapSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        int n = array.length;

        MinFourHeap<E> heap = new MinFourHeap<>(comparator);
        for (int i = 0; i < n; i++) {
            heap.add(array[i]);
        }
        for (int i = 0; i < n; i++) {
            array[i] = heap.next();
        }
    }
}
