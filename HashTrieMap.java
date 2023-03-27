/*
 *  HashTrieMap
 *
 *                                                        Jun 26, 2021
 *                                                 Edited by An and Young
 *
 *  Pinned Notes :
 *    None.
 *
 */

package datastructures.dictionaries;

import java.lang.reflect.GenericArrayType;
import java.util.*;
import java.util.Map.Entry;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.types.BString;
import cse332.interfaces.trie.TrieMap;

/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {

    /**********************************************************************
    /**********************************************************************
    // HashTrieNode
    // HashTrieNode Sub Class
    /**********************************************************************
    /**********************************************************************/
    public class HashTrieNode extends TrieNode<ChainingHashTable<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new ChainingHashTable<>(MoveToFrontList::new);
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            Iterator<Item<A, HashTrieNode>> hashTableIterator = pointers.iterator();

            return new Iterator<Entry<A, HashTrieNode>>() {
                @Override
                public boolean hasNext() {
                    return hashTableIterator.hasNext();
                }

                @Override
                public Entry<A, HashTrieNode> next() {
                    Item<A, HashTrieNode> returnItem = hashTableIterator.next();
                    return(new AbstractMap.SimpleEntry<>(returnItem.key, returnItem.value));
                }
            };
        }
    }


    /**********************************************************************
    // DeleteMode
    // Const enums
    /**********************************************************************/
    public enum DeleteMode {
        NONE,                   // Nothing to do
        CLEAN,                  // Only clean redundant pointers.
        LAZY,                   // Conduct lazy deletion. (Only delete the value)
        FULL                    // Conduct full deletion. (Delete the value and pointer)
    }


    /**********************************************************************
    // HashTrieMap
    // Constructor
    /**********************************************************************/
    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = null;
    }


    /**********************************************************************
    // castNode
    // Cast node without a warning message.
    // This method was wrapped to remove cast warning messages.
    /**********************************************************************/
    @SuppressWarnings("unchecked")
    private HashTrieMap<A, K, V>.HashTrieNode castNode(TrieNode<?, ?> node) {
        return((HashTrieMap<A, K, V>.HashTrieNode)node);
    }


    /**********************************************************************
    // getNode
    // Returns the target node that has moved by the key from the baseNode.
    // If makeIfNotExist is true, make a new node if the target node or branch node doesn't exist.
    /**********************************************************************/
    private HashTrieNode getNode(HashTrieNode baseNode, Iterator<A> keyIterator, boolean makeIfNotExist) {
        // If this is the last character of the key, return this node.
        if(!keyIterator.hasNext()) return(baseNode);

        // If can occur if the root node is null
        if(baseNode == null) return(null);

        // Check if there is a node for the current character of the key
        A character = keyIterator.next();
        HashTrieNode targetNode = baseNode.pointers.find(character);
        if(targetNode == null) {
            // If makeIfNotExist is true, make a new node and continue. Otherwise, just return null.
            if(makeIfNotExist) {
                targetNode = new HashTrieNode();
                baseNode.pointers.insert(character, targetNode);
            } else {
                return(null);
            }
        }

        // Find the next character
        return(getNode(targetNode, keyIterator, makeIfNotExist));
    }


    /**********************************************************************
    // deleteNode
    // Delete the target node that has moved by the key from the baseNode.
    // If a node has children, it will only delete the value. (Lazy deletion)
    // Otherwise, it will remove branch nodes as well.
    /**********************************************************************/
    private DeleteMode deleteNode(HashTrieNode baseNode, Iterator<A> keyIterator) {

        if(baseNode == null) return(DeleteMode.NONE);

        // If there is no more requested key (which means it is on the target node), calculate the deletion mode.
        if(!keyIterator.hasNext()) {
            // If the node has children, delete only the value. Otherwise, delete the node as well.
            if(baseNode.pointers.size() > 0) return(DeleteMode.LAZY);
            else return(DeleteMode.FULL);
        }

        // First go to the end of the last character of the key
        A character = keyIterator.next();
        HashTrieNode targetNode = baseNode.pointers.find(character);
        DeleteMode deleteMode = deleteNode(targetNode, keyIterator);

        // Debug message
        /*
        System.out.println(String.format("deleteMode  : %s", deleteMode));
        System.out.println(String.format("character   : %s", character));
        System.out.println(String.format("baseNodes   : %s", baseNode.pointers.keySet()));
        System.out.println(String.format("targetNodes : %s", targetNode != null ? targetNode.pointers.keySet() : null));
        */

        switch(deleteMode) {
            case FULL: {
                baseNode.pointers.remove(character);
                -- size;
                return(DeleteMode.CLEAN);
            }

            case LAZY: {
                if(targetNode.value != null) {
                    targetNode.value = null;
                    -- size;
                }
                return(DeleteMode.NONE);
            }

            case CLEAN: {
                if(targetNode != null && targetNode.value != null) {
                    return(DeleteMode.NONE);
                }

                // If this node doesn't have children longer, delete this node.
                if(targetNode == null || targetNode.pointers.size() == 0) {
                    baseNode.pointers.remove(character);
                }
                return(DeleteMode.CLEAN);
            }

            case NONE: {
                // Nothing to do
                return(DeleteMode.NONE);
            }
        }

        // Non reachable code.
        return(DeleteMode.NONE);

    }



    /**********************************************************************
    // insert
    // Insert a new node
    /**********************************************************************/
    @Override
    public V insert(K key, V value) {
        if(key == null || value == null) throw new IllegalArgumentException();

        if(castNode(root) == null) {
            this.root = new HashTrieNode();
        }

        // Get the target node
        HashTrieNode node = getNode(castNode(root), key.iterator(), true);
        if(node == null) throw new IllegalArgumentException(); // Actually, impossible.

        // We need to return the previous value according to the instruction of Dictionary.java:32
        V previousValue = node.value;

        // Assign the new value
        node.value = value;

        // If this is the new assignment, increase the size.
        if(previousValue == null) ++ size;

        return(previousValue);
    }


    /**********************************************************************
    // find
    // Find a node
    /**********************************************************************/
    @Override
    public V find(K key) {
        if(key == null) throw new IllegalArgumentException();

        // If there is no node, just return null
        HashTrieNode node = getNode(castNode(root), key.iterator(), false);
        if(node == null) return(null);

        return(node.value);
    }


    /**********************************************************************
    // findPrefix
    // Return the result whether the trie has a such key.
    /**********************************************************************/
    @Override
    public boolean findPrefix(K key) {
        if(key == null) throw new IllegalArgumentException();

        if(castNode(root) == null) return(false);

        // Return whether there is a node for key
        HashTrieNode node = getNode(castNode(root), key.iterator(), false);
        return(node != null);
    }


    /**********************************************************************
    // delete
    // Delete a node
    /**********************************************************************/
    @Override
    public void delete(K key) {
        if(key == null) throw new IllegalArgumentException();

        // Return whether there is a node for key
        DeleteMode deleteMode = deleteNode(castNode(root), key.iterator());
        // System.out.println(String.format("deleteMode  : %s", deleteMode));

        // Process the root node
        switch(deleteMode) {
            case FULL: {
                // It means deleting root node
                clear();
                break;
            }

            case LAZY: {
                // It means deleting the "" key.
                if(castNode(root).value != null) {
                    castNode(root).value = null;
                    -- size;
                }
                break;
            }

            case CLEAN: {
                if(size() <= 0) clear();
                break;
            }
        }

    }


    /**********************************************************************
    // clear
    // Clear memory in the trie
    /**********************************************************************/
    @Override
    public void clear() {
        castNode(root).pointers.clear();
        root = null;
        size = 0;
    }

}
