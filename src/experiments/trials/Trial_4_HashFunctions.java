package experiments.trials;

import cse332.datastructures.trees.BinarySearchTree;
import cse332.types.AlphabeticString;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;
import datastructures.worklists.CircularArrayFIFOQueue;
import org.junit.Test;

import java.util.Random;


public class Trial_4_HashFunctions {

	long startTime;


	@SuppressWarnings("unchecked")
	@Test(timeout = 1000000)
	public void testTrial4() {

		ChainingHashTable<AlphabeticString, Integer> hastTable = new ChainingHashTable<>(MoveToFrontList::new);

		int N = 1000;
		AlphabeticString [] dataSet = new AlphabeticString[N];

		for(int idx = 0; idx < N; ++ idx) {
			int length = 1 + (idx % 100);
			StringBuilder str = new StringBuilder();

			// Make expectable random strings.
			for(int len = 0; len < length; ++ len) {
				char ch = (char)(48 + ( ((idx + 1) * (len + 1))  % (127 - 48) ));
				str.append(ch);
			}

			dataSet[idx] = a(str.toString());
			System.out.printf("Str #%d: %s\n", idx, str);
		}

		System.out.printf("N: %d\n", N);
		System.out.printf("HASH_RANGE: %d\n", CircularArrayFIFOQueue.HASH_RANGE);

		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			hastTable.insert(dataSet[idx], 0);
		}
		endTimer("Insert");


		startTimer();
		for(int idx = 0; idx < N; ++ idx) {
			hastTable.find(dataSet[idx]);
		}
		endTimer("Search");

	}


	private void startTimer() { startTime = System.currentTimeMillis(); }
	private void endTimer(String caption) { System.out.printf("Time %s: %dms%n", caption, System.currentTimeMillis() - startTime); }

	private static AlphabeticString a(String s) {
		return new AlphabeticString(s);
	}

}
