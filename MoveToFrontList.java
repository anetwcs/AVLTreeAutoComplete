// Edited by An Barnes

package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 1. The list is typically not sorted.
 * 2. Add new items to the front of the list.
 * 3. Whenever find is called on an item, move it to the front of the 
 *    list. This means you remove the node from its current position 
 *    and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 *    elements to the front.  The iterator should return elements in
 *    the order they are stored in the list, starting with the first
 *    element in the list. When implementing your iterator, you should 
 *    NOT copy every item to another dictionary/list and return that 
 *    dictionary/list's iterator. 
 */

public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {

    private Node<K, V>      firstNode;          // The pointer of the first node


    /**********************************************************************
    // insert
    // Insert a new value
    /**********************************************************************/
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
     *            if there was no mapping for <tt>key</tt>.
     * @throws IllegalArgumentException
     *            if either key or value is null.
     */
    @Override
    public V insert(K key, V value) {

        // Check key and value validity
        if(key == null || value == null) throw new IllegalArgumentException();

        Node<K, V> newNode = pullNode(key);
        V          previousValue = null;

        // If previous node exists, use it instead and save previous value.
        // Otherwise, make a new node in the front
        if(newNode != null) {
            previousValue = newNode.value;
        } else {
            newNode = new Node(key, value);

            // It needs to be placed at the first node
            firstNode = insertNode(newNode, firstNode);
        }

        // For debugging
        // System.out.println(String.format("%s : %s : %s : %s : %s", newNode.toString(), newNode.key, newNode.value, newNode.previousNode != null ? newNode.previousNode.toString() : "NULL", newNode.nextNode != null ? newNode.nextNode.toString() : "NULL"));

        newNode.value = value;
        return(previousValue);
    }


    /**********************************************************************
    // find
    // Find a value in the map and place it into the front node
    /**********************************************************************/
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

        // Check key validity
        if(key == null) throw new IllegalArgumentException();

        Node<K, V> node = pullNode(key);
        return(node != null ? node.value : null);

    }


    /**********************************************************************
    // iterator
    // Create a new iterator for list and return it
    /**********************************************************************/
    @Override
    public Iterator<Item<K, V>> iterator() {
        return new NodeIterator();
    }


    /**********************************************************************
    // insertNode
    // Insert a new node right before the targetNode and reconfigure link information
    // Return the inserted node
    /**********************************************************************/
    private Node<K, V> insertNode(Node<K, V> node, Node<K, V> targetNode) {

        if(node == null) throw new IllegalArgumentException();

        if(targetNode != null) {
            Node<K, V> previousNode = targetNode.previousNode;
            node.previousNode = previousNode;
            if(previousNode != null) previousNode.nextNode = node;

            targetNode.previousNode = node;
            node.nextNode = targetNode;
        }

        ++ size;
        return(node);

    }


    /**********************************************************************
    // removeNode
    // Remove a node and reconfigure link information
    /**********************************************************************/
    private void removeNode(Node<K, V> node) {

        if(node == null) throw new IllegalArgumentException();

        Node<K, V> previousNode = node.previousNode;
        Node<K, V> nextNode     = node.nextNode;

        // Link between previous and next node.
        if(previousNode != null) previousNode.nextNode = nextNode;
        if(nextNode != null)     nextNode.previousNode = previousNode;

        // If the node was the firstNode, update the firstNode information to next node
        if(firstNode == node) firstNode = nextNode;

        // Break previous and next node information
        node.previousNode = null;
        node.nextNode     = null;

        -- size;

    }


    /**********************************************************************
    // pullNode
    // Find a node with a key and place it into the front node
    /**********************************************************************/
    private Node<K, V> pullNode(K key) {

        Node<K, V> currentNode = firstNode;

        while(currentNode != null) {
            // For debuging
            // System.out.println(String.format(">> %s : %s : %s : %s : %s", currentNode.toString(), currentNode.key, currentNode.value, currentNode.previousNode != null ? currentNode.previousNode.toString() : "NULL", currentNode.nextNode != null ? currentNode.nextNode.toString() : "NULL"));

            if(currentNode.key.equals(key)) {
                removeNode(currentNode);
                firstNode = insertNode(currentNode, firstNode);
                return(currentNode);
            }

            currentNode = currentNode.nextNode;
        }

        return(null);

    }



    /**********************************************************************
    /**********************************************************************
    // Node
    // Sub class for a node structure
    /**********************************************************************
    /**********************************************************************/
    public static class Node<K, V> {
        final K     key;
        V           value;
        Node<K, V>  previousNode = null, nextNode = null;

        /**********************************************************************
        // Node
        // Constructor
        /**********************************************************************/
        public Node(K key, V value) {
            this.key   = key;
            this.value = value;
        }
    }



    /**********************************************************************
    /**********************************************************************
    // NodeIterator
    // Sub class for a node iterator
    /**********************************************************************
    /**********************************************************************/
    private class NodeIterator implements Iterator<Item<K, V>> {
        Node<K, V> currentNode = firstNode;

        /**********************************************************************
        // hasNext
        // Return whether there is a next node or not
        /**********************************************************************/
        @Override
        public boolean hasNext() {
            return(currentNode != null);
        }

        /**********************************************************************
        // next
        // Move the offset to the next node
        /**********************************************************************/
        @Override
        public Item<K, V> next() {
            if(!hasNext()) throw new NoSuchElementException();

            // Get an item at the current offset, and then move next
            Item<K, V> item = new Item<>((K)currentNode.key,(V)currentNode.value);
            currentNode = currentNode.nextNode;
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
    }

}
