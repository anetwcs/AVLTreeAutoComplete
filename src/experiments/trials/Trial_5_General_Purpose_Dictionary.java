package experiments.trials;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.misc.Dictionary;
import cse332.misc.WordReader;
import cse332.types.AlphabeticString;
import cse332.types.NGram;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.HashTrieMap;
import datastructures.dictionaries.MoveToFrontList;
import datastructures.worklists.CircularArrayFIFOQueue;
import org.junit.Test;
import java.lang.instrument.Instrumentation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Trial_5_General_Purpose_Dictionary {

	private static Instrumentation instrumentation;
	long startTime;


	@SuppressWarnings("unchecked")
	@Test(timeout = 1000000)
	public void testTrial5() throws IOException {

		BinarySearchTree<AlphabeticString, Integer> 		storage1 = new BinarySearchTree<>();
		AVLTree<AlphabeticString, Integer> 					storage2 = new AVLTree<>();
		Dictionary<AlphabeticString, Integer> 				storage3 = new ChainingHashTable(MoveToFrontList::new);
		HashTrieMap<Character, AlphabeticString, Integer> 	storage4 = new HashTrieMap<>(AlphabeticString.class);

		// Target dictionary
		Dictionary<AlphabeticString, Integer> storage = storage1;
		List<AlphabeticString> wordList = new ArrayList<AlphabeticString>();

		final int N = 100;
		int wordCount = 0;


		System.out.printf("* %s\n", storage.getClass());

		// Read file
		WordReader reader = new WordReader(new FileReader("alice.txt"));

		while (reader.hasNext()) {
			wordList.add(a(reader.next()));
			++ wordCount;
		}

		reader.close();



		startTimer();

		for(int cnt = 0; cnt < N; ++ cnt) {
			Iterator<AlphabeticString> listIterator = wordList.iterator();

			while (listIterator.hasNext()) {
				AlphabeticString word = listIterator.next();
				Integer presentCount = storage.find(word);

				if (presentCount == null) {
					storage.insert(word, 1);
				} else {
					storage.insert(word, presentCount + 1);
				}
			}
		}

		endTimer("Insert/Add values");



		startTimer();

		for(int cnt = 0; cnt < N; ++ cnt) {
			Iterator<AlphabeticString> listIterator = wordList.iterator();

			while (listIterator.hasNext()) {
				AlphabeticString word = listIterator.next();
				storage.find(word);
			}
		}

		endTimer("Search with inserted order");



		startTimer();

		for(int cnt = 0; cnt < N; ++ cnt) {
			Collections.shuffle(wordList);

			Iterator<AlphabeticString> listIterator = wordList.iterator();

			while (listIterator.hasNext()) {
				AlphabeticString word = listIterator.next();
				storage.find(word);
			}
		}

		endTimer("Search with random order");




		startTimer();

		for(int cnt = 0; cnt < N; ++ cnt) {
			int wordCounts = 0;

			Iterator<Item<AlphabeticString, Integer>> iterator = storage.iterator();
			while (iterator.hasNext()) {
				Item<AlphabeticString, Integer> item = iterator.next();
				++ wordCounts;
				if(cnt == 0) System.out.printf("%s : %d\n", item.key, item.value / N);
			}

			// System.out.printf("Total words in iterator : %d\n", wordCounts);
		}

		endTimer("Iterator");


		System.out.printf("Total words in files : %d\n", wordCount);
		System.out.printf("Total unique words in storage : %d\n", storage.size());

	}


	private void startTimer() { startTime = System.currentTimeMillis(); }
	private void endTimer(String caption) { System.out.printf("Time %s: %dms%n", caption, System.currentTimeMillis() - startTime); }

	private static AlphabeticString a(String s) {
		return new AlphabeticString(s);
	}

}
