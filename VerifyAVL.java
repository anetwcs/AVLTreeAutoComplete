// Edited by An Barnes

package verifyavl;

public class VerifyAVL {
    public static boolean verifyAVL(AVLNode root) {
        return hasCorrectHeight(root) && isBalanced(root) && isBST(root) ;
    }

    private static int findHeight(AVLNode root) {
        //If the node is null then stop adding height
        if (root == null){
            return 0;
        }
        //If the node childrens are null (node is leaf) then stop adding height
        else if (root.left == null && root.right == null) {
            return 0;
        }
        return 1 + Math.max(findHeight(root.left), findHeight(root.right));
    }

    //Check if the provided height is the same as real height
    private static boolean hasCorrectHeight(AVLNode root){
        return (root.height == findHeight(root));
    }


    private static boolean isBST(AVLNode root){
       return isBSTHelper(root, Integer.MIN_VALUE, Integer.MAX_VALUE );
    }

    //Check if the provided tree is a BST
    private static boolean isBSTHelper(AVLNode root, int min, int max) {

        if (root == null) {
            return true;
        }

        if (root.key < min || root.key > max) {
            return false;
        }

        return isBSTHelper(root.left, min, root.key - 1) && isBSTHelper(root.right, root.key + 1, max);
    }

    //Check for AVLTree's balance condition
    private static boolean isBalanced(AVLNode root){

        if (root == null) {
            return true;
        }

        int leftSubTreeHeight = findHeight(root.left);
        int rightSubTreeHeight = findHeight(root.right);

        if (Math.abs(leftSubTreeHeight - rightSubTreeHeight) <= 1
                && isBalanced(root.left) && isBalanced(root.right)){
            return true;
        }

        return false;
    }


}