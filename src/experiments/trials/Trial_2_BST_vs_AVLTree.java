package experiments.trials;

import cse332.types.AlphabeticString;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.MoveToFrontList;
import cse332.datastructures.trees.BinarySearchTree;

import org.junit.Test;
import java.util.Random;


public class Trial_2_BST_vs_AVLTree {

	long startTime;


	@SuppressWarnings("unchecked")
	@Test(timeout = 1000000)
	public void testTrial2() {

		BinarySearchTree<Integer, Integer> treeA = new BinarySearchTree<>();
		AVLTree<Integer, Integer> treeB = new AVLTree<>();

		int N = 1000;

		System.out.println("Tree A: ");

		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			treeA.insert(idx, 0);
		}
		endTimer("insert");

		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			treeA.find(idx);
		}
		endTimer("find");


		System.out.println("Tree B: ");

		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			treeB.insert(idx, 0);
		}
		endTimer("insert");

		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			treeB.find(idx);
		}
		endTimer("find");

	}


	private void startTimer() { startTime = System.currentTimeMillis(); }
	private void endTimer(String caption) { System.out.printf("Time %s: %dms%n", caption, System.currentTimeMillis() - startTime); }

	private static AlphabeticString a(String s) {
		return new AlphabeticString(s);
	}

}
