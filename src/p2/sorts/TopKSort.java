package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import datastructures.worklists.MinFourHeap;

import java.util.Comparator;

public class TopKSort {
    public static <E extends Comparable<E>> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {
        MinFourHeap<E> heap = new MinFourHeap<>(comparator);

        for (int i = 0; i < array.length; i++) {
            heap.add(array[i]);
            if (heap.size() > k) {
                heap.next();
            }
        }

        for (int i = 0; i < k && i < array.length; i++) {
            array[i] = heap.next();
        }

        for (int i = k; i < array.length; i++) {
            array[i] = null;
        }
    }
}
