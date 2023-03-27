package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import cse332.sorts.InsertionSort;

import java.util.Arrays;
import java.util.Comparator;

public class QuickSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        quickSortHelper(0, array.length, array, comparator);
    }


    private static <E> void quickSortHelper(int low, int high, E[] array, Comparator<E> comparator) {
        if (low >= high) {
            return;
        }

        int pivotIndex = findPivotIndex(array, comparator, low, high);
        E pivot = array[pivotIndex];

        swap(array, low, pivotIndex);

        int i = low + 1;
        int j = high - 1;
        while (i <= j) {
            if (comparator.compare(array[j], pivot) > 0) {
                j--;
            } else if ((comparator.compare(array[i], pivot) <= 0)) {
                i++;
            } else {
                swap(array, i, j);
            }
        }

        swap(array, low, j);

        quickSortHelper(low, j, array, comparator);
        quickSortHelper(j + 1, high, array, comparator);
    }


    private static <E> void swap(E[] array, int index1, int index2) {
        E temp = array[index2];
        array[index2] = array[index1];
        array[index1] = temp;
    }

    /**
     * This method finds the pivot by three-way medium and return the pivot's index
     *
     * @param array
     * @param comparator
     * @param <E>
     * @return the index of the pivot value
     */
    private static <E> int findPivotIndex(E[] array, Comparator<E> comparator, int low, int high) {
        int n = high - low;

        //Find the three-way medium
        E[] threeWayArray = (E[]) new Object[3];   //??? Should I initialize the size?
        threeWayArray[0] = array[low];
        threeWayArray[1] = array[(low + high - 1) / 2];
        threeWayArray[2] = array[high - 1];

        InsertionSort.sort(threeWayArray, comparator);

        E pivot = threeWayArray[1];

        if (pivot == array[high - 1]) {
            return high - 1;
        } else if (pivot == array[(low + high - 1) / 2]) {
            return (low + high - 1) / 2;
        } else {
            return low;
        }
    }
}
