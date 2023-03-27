package verifyavl;

public class AVLNode {
    public AVLNode left;
    public AVLNode right;
    public int key; // The data element stored at this node.
    public int height;

    public AVLNode(int key, int height, AVLNode left, AVLNode right) {
        this.key = key;
        this.height = height;
        this.left = left;
        this.right = right;
    }
}
