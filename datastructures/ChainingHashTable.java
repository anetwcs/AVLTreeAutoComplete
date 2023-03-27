package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.NoSuchElementException;

/**
 * 1. You must implement a generic chaining hashtable. You may not
 *    restrict the size of the input domain (i.e., it must accept 
 *    any key) or the number of inputs (i.e., it must grow as necessary).
 * 3. Your HashTable should rehash as appropriate (use load factor as
 *    shown in class!). 
 * 5. HashTable should be able to resize its capacity to prime numbers for more
 *    than 200,000 elements. After more than 200,000 elements, it should
 *    continue to resize using some other mechanism.
 * 6. We suggest you hard code some prime numbers. You can use this
 *    list: http://primes.utm.edu/lists/small/100000.txt 
 *    NOTE: Do NOT copy the whole list!
 * 7. When implementing your iterator, you should NOT copy every item to another
 *    dictionary/list and return that dictionary/list's iterator. 

 */

public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {

    private final static int[]                  TABLE_SIZE = { 11, 23, 43, 83, 163, 313, 643, 1223, 2423, 4813, 9533, 19333, 39953, 79903, 149993 };
    private final static double                 TABLE_LOAD_FACTOR = 1.0;
    private final static double                 TABLE_MULTIPLE_FACTOR = 2.0;

    private int                                 tableSize;
    private int                                 tableSizeIndex;
    private Dictionary<K, V>[]                  tableChains;

    private final Supplier<Dictionary<K, V>>    newChain;


    /**********************************************************************
    // CircularArrayFIFOQueue
    // Constructor
    /**********************************************************************/
    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;

        tableSizeIndex = 0;
        tableSize   = TABLE_SIZE[tableSizeIndex];
        tableChains = new Dictionary[tableSize];
    }


    /**********************************************************************
    // insert
    // Insert a new value
    /**********************************************************************
    /**
     * Associates the specified value with the specified key in this map. If the
     * map previously contained a mapping for the key, the old value is
     * replaced.
     *
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
     *         if there was no mapping for <tt>key</tt>.
     * @throws IllegalArgumentException
     *             if either key or value is null.
     */
    @Override
    public V insert(K key, V value) {
        if(key == null || value == null) throw new IllegalArgumentException();

        // Check lambda.
        double lambda = size / (double)tableSize;
        if(lambda > TABLE_LOAD_FACTOR) {
            // Need to increase table size.
            increaseTableSize();
        }

        int tableIndex = getTableIndex(key);
        // System.out.println(String.format("tableIndex   : %d", tableIndex));

        Dictionary<K, V> chain = getTableChain(tableIndex);
        if(chain == null) throw new IllegalArgumentException();

        int oldSize = chain.size();
        V  oldValue = chain.insert(key, value);

        if(chain.size() > oldSize) ++ size;
        return(oldValue);
    }


    /**********************************************************************
    // remove
    // Delete a key and value. (=delete)
    /**********************************************************************/
    public void remove(K key) {
        delete(key);
    }


    /**********************************************************************
    // find
    // Find a value in the map and place it into the front node
    /**********************************************************************
    /**
     * Returns the value to which the specified key is mapped, or {@code null}
     * if this map contains no mapping for the key.
     *
     * @param key
     *            the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null}
     *         if this map contains no mapping for the key
     * @throws IllegalArgumentException
     *             if key is null.
     */
    @Override
    public V find(K key) {
        if(key == null) throw new IllegalArgumentException();

        int tableIndex = getTableIndex(key);
        if(tableIndex < 0) return(null);

        Dictionary<K, V> chain = getTableChain(tableIndex);
        if(chain == null) return(null);

        return(chain.find(key));
    }


    /**********************************************************************
    // containsKey
    // Check if the key is existed
    /**********************************************************************/
    public boolean containsKey(K key) {
        return(find(key) != null);
    }


    /**********************************************************************
    // getTableSize
    // Return the current table size
    /**********************************************************************/
    public int getTableSize() {
        return(tableSize);
    }


    /**********************************************************************
    // iterator
    // Create a new iterator for list and return it
    /**********************************************************************
    /**
     * An iterator over the keys of the map
     */
    @Override
    public Iterator<Item<K, V>> iterator() {
        return new ItemIterator();
    }


    /**********************************************************************
    // increaseTableSize
    // Increase table size
    /**********************************************************************/
    private void increaseTableSize() {
        int                 oldTableSize = tableSize;
        Dictionary<K, V>[]  oldTableChains = tableChains;


        if(++ tableSizeIndex < TABLE_SIZE.length) {
            // Use a const value
            tableSize = TABLE_SIZE[tableSizeIndex];
        } else {
            // Use a calculated value
            tableSize *= (double)TABLE_MULTIPLE_FACTOR;
        }

        // Make the new tableChains
        tableChains = new Dictionary[tableSize];
        size = 0;

        // Copy old value
        for(int chainIndex = 0; chainIndex < oldTableSize; ++ chainIndex) {
            if(oldTableChains[chainIndex] == null) continue;

            for(Item<K, V> item : oldTableChains[chainIndex]) {
                insert(item.key, item.value);
            }
        }
    }


    /**********************************************************************
    // getTableIndex
    // Return a proper table index based on hash code
    /**********************************************************************/
    private int getTableIndex(K key) {
        if(tableSize <= 0) return(-1);
        return(Math.abs(key.hashCode()) % tableSize);
    }


    /**********************************************************************
    // getTableChain
    // Return a table chain for the index
    /**********************************************************************/
    private Dictionary<K, V> getTableChain(int tableIndex) {
        if(tableIndex < 0 || tableIndex >= tableSize) return(null);

        Dictionary<K, V> chain = tableChains[tableIndex];
        if(chain == null) {
            tableChains[tableIndex] = newChain.get();
            return(tableChains[tableIndex]);
        }

        return(chain);
    }



    /**********************************************************************
    /**********************************************************************
    // ItemIterator
    // Sub class for a item iterator
    /**********************************************************************
    /**********************************************************************/
    private class ItemIterator implements Iterator<Item<K, V>> {
        private int                     currentTableIndex = -1;
        private Iterator<Item<K, V>>    currentChainIterator = null;
        private Item<K, V>              currentChainItem = moveToNextChainItem();

        /**********************************************************************
        // hasNext
        // Return whether there is a next node or not
        /**********************************************************************/
        @Override
        public boolean hasNext() {
            return(currentChainItem != null);
        }

        /**********************************************************************
        // next
        // Move the offset to the next node
        /**********************************************************************/
        @Override
        public Item<K, V> next() {
            if(!hasNext()) throw new NoSuchElementException();

            Item<K, V> item = currentChainItem;
            currentChainItem = moveToNextChainItem();
            return(item);
        }


        /**********************************************************************
        // remove
        // Remove node at the iterator, but it doesn't support for DeletelessDictionary
        /**********************************************************************/
        @Override
        public void remove() {
            throw new UnsupportedOperationException("DeletelessDictionary does not support iterator deletion");
        }


        /**********************************************************************
        // moveToNextChainIterator
        // Move to next chain iterator. Return null if not exists.
        /**********************************************************************/
        private Iterator<Item<K, V>> moveToNextChainIterator() {
            while(++ currentTableIndex < tableSize) {
                if(tableChains[currentTableIndex] != null) {
                    return(tableChains[currentTableIndex].iterator());
                }
            }

            return(null);
        }


        /**********************************************************************
        // moveToNextChainItem
        // Move to next item. Return null if not exists.
        /**********************************************************************/
        private Item<K, V> moveToNextChainItem() {
            while(true) {
                // Move to the next chain iterator if it is null.
                if(currentChainIterator == null || !currentChainIterator.hasNext()) {
                    currentChainIterator = moveToNextChainIterator();

                    if(currentChainIterator == null) return(null);
                    continue;
                }

                return(currentChainIterator.next());
            }
        }
    }

}
