// Edited by An Barnes

package datastructures.dictionaries;

import cse332.datastructures.trees.BinarySearchTree;

/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 * <p>
 * 1. Create a subclass of BSTNode, perhaps named AVLNode.
 * 2. Override the insert method such that it creates AVLNode instances
 * instead of BSTNode instances.
 * 3. Do NOT "replace" the children array in BSTNode with a new
 * children array or left and right fields in AVLNode.  This will
 * instead mask the super-class fields (i.e., the resulting node
 * would actually have multiple copies of the node fields, with
 * code accessing one pair or the other depending on the type of
 * the references used to access the instance).  Such masking will
 * lead to highly perplexing and erroneous behavior. Instead,
 * continue using the existing BSTNode children array.
 * 4. If this class has redundant methods, your score will be heavily
 * penalized.
 * 5. Cast children array to AVLNode whenever necessary in your
 * AVLTree. This will result a lot of casts, so we recommend you make
 * private methods that encapsulate those casts.
 * 6. Do NOT override the toString method. It is used for grading.
 */

public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    //protected  AVLNode root;

    public AVLTree() {
        super();
        this.root = null;
    }

    /**
     * Inner class represents a node in the AVL tree.
     */

    public class AVLNode extends BSTNode {
        public int height;

        public AVLNode(K key, V value, int height) {
            super(key, value);
            this.height = height;
        }
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }

        return insertHelper(key, value, null, (AVLNode) this.root);
    }

    private V insertHelper(K key, V value, AVLNode prev, AVLNode current) {
        V preValue = null;
        if (current == null) {
            current = new AVLNode(key, value, 0);
            size++;
            if (prev == null) { //add first node to empty tree
                this.root = current;
            } else {
                prev.children[childSelect(key, prev)] = current;
            }
            return null;
        } else {
            int child = childSelect(key, current);
            if (child == -1) { // already found a node with Key "key"
                preValue = current.value;
                current.value = value;
                return preValue;
            }
            preValue = insertHelper(key, value, current, (AVLNode) current.children[child]);
        }

        // backtracking part
        updateHeight(current);
        if (!isBalanced(current)) {
            int rotateCase = getCase(current, key);
            if (rotateCase == 1) {
                current = rotateRight(current);
            } else if (rotateCase == 4) {
                current = rotateLeft(current);
            } else if (rotateCase == 2) {
                current.children[LEFT] = rotateLeft((AVLNode) current.children[LEFT]);
                current = rotateRight(current);
            } else {
                current.children[RIGHT] = rotateRight((AVLNode) current.children[RIGHT]);
                current = rotateLeft(current);
            }

            if (prev != null) {
                prev.children[childSelect(key, prev)] = current;
            } else {
                this.root = current;
            }
        }
        return preValue;
    }

    private int childSelect(K key, AVLNode current) {
        int direction = Integer.signum(key.compareTo(current.key));
        if (direction == -1) {
            return LEFT;
        } else if (direction == 0) {
            return -1;
        }
        return RIGHT;
    }

    private void updateHeight(AVLNode current) {
        int leftHeight = getHeight((AVLNode) current.children[LEFT]);
        int rightHeight = getHeight((AVLNode) current.children[RIGHT]);
        current.height = Math.max(leftHeight, rightHeight) + 1;
    }

    private int getHeight(AVLNode current) {
        if (current == null) {
            return -1;
        }
        return current.height;

    }

    private boolean isBalanced(AVLNode current) {
        int leftHeight = getHeight((AVLNode) current.children[LEFT]);
        int rightHeight = getHeight((AVLNode) current.children[RIGHT]);
        return Math.abs(leftHeight - rightHeight) < 2;
    }

    private int getCase(AVLNode current, K insertedKey) {
        if (childSelect(insertedKey, current) == LEFT && childSelect(insertedKey, (AVLNode) current.children[LEFT]) == LEFT) {
            return 1;
        } else if (childSelect(insertedKey, current) == LEFT && childSelect(insertedKey, (AVLNode) current.children[LEFT]) == RIGHT) {
            return 2;
        } else if (childSelect(insertedKey, current) == RIGHT && childSelect(insertedKey, (AVLNode) current.children[RIGHT]) == LEFT) {
            return 3;
        } else {
            return 4;
        }
    }

    private AVLNode rotateRight(AVLNode current) {
        AVLNode temp = (AVLNode) current.children[LEFT];
        current.children[LEFT] = temp.children[RIGHT];
        temp.children[RIGHT] = current;
        updateHeight(current);
        updateHeight(temp);
        return temp;
    }

    private AVLNode rotateLeft(AVLNode current) {
        AVLNode temp = (AVLNode) current.children[RIGHT];
        current.children[RIGHT] = temp.children[LEFT];
        temp.children[LEFT] = current;
        updateHeight(current);
        updateHeight(temp);
        return temp;
    }

}
