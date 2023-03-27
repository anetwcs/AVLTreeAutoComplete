package experiments.trials;

import cse332.datastructures.trees.BinarySearchTree;
import cse332.types.AlphabeticString;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;
import org.junit.Test;

import java.util.Random;


public class Trial_3_ChainingHashTable {

	long startTime;


	@SuppressWarnings("unchecked")
	@Test(timeout = 1000000)
	public void testTrial3() {

		Random rd = new Random(); // creating Random object

		ChainingHashTable<String, Integer> hastTable1 = new ChainingHashTable<>(MoveToFrontList::new);
		ChainingHashTable<String, Integer> hastTable3 = new ChainingHashTable<>(BinarySearchTree::new);
		ChainingHashTable<String, Integer> hastTable2 = new ChainingHashTable<>(AVLTree::new);

		int N = 3000000;
		int [] dataSet = new int[N];

		for(int idx = 0; idx < N; ++ idx) {
			dataSet[idx] = idx;
		}

		// Mix integers
		for(int idx = 0; idx < N; ++ idx) {
			int idxNew = rd.nextInt(N);

			int temp = dataSet[idx];
			dataSet[idx] = dataSet[idxNew];
			dataSet[idxNew] = temp;
		}

		System.out.printf("N: %d\n", N);
		System.out.println("Insert: ");

		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			hastTable1.insert(Integer.toString(dataSet[idx]), idx);
		}
		endTimer("MoveToFrontList");

		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			hastTable2.insert(Integer.toString(dataSet[idx]), idx);
		}
		endTimer("BinarySearchTree");

		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			hastTable3.insert(Integer.toString(dataSet[idx]), idx);
		}
		endTimer("AVLTree");


		// Randomize integers
		for(int idx = 0; idx < N; ++ idx) {
			dataSet[idx] = rd.nextInt(N);
		}

		System.out.println("Find: ");

		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			hastTable1.find(Integer.toString(dataSet[idx]));
		}
		endTimer("MoveToFrontList");

		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			hastTable2.find(Integer.toString(dataSet[idx]));
		}
		endTimer("BinarySearchTree");

		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			hastTable3.find(Integer.toString(dataSet[idx]));
		}
		endTimer("AVLTree");

	}


	private void startTimer() { startTime = System.currentTimeMillis(); }
	private void endTimer(String caption) { System.out.printf("Time %s: %dms%n", caption, System.currentTimeMillis() - startTime); }

	private static AlphabeticString a(String s) {
		return new AlphabeticString(s);
	}

}
