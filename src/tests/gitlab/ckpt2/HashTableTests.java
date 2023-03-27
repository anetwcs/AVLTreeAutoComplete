package tests.gitlab.ckpt2;

import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.Dictionary;
import cse332.types.AlphabeticString;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class HashTableTests {

	private void incCount(Dictionary<String, Integer> list, String key) {
		Integer find = list.find(key);
		if (find == null)
			list.insert(key, 1);
		else
			list.insert(key, 1 + find);
	}


	@Test(timeout = 3000)
	public void testBasicHashTable() {
		ChainingHashTable<String, Integer> list = new ChainingHashTable<>(MoveToFrontList::new);

		list.insert("a", 1);
		list.insert("aa", 101);
		list.insert("b", 2);
		list.insert("bb", 202);
		list.insert("c", 3);
		list.insert("d", 4);
		list.insert("e", 5);
		list.insert("f", 6);
		list.insert("g", 7);
		list.insert("h", 8);
		list.insert("i", 9);
		list.insert("j", 10);
		list.insert("k", 11);
		list.insert("l", 12);
		list.insert("m", 13);
		list.insert("n", 14);
		list.insert("o", 15);
		list.insert("p", 16);
		list.insert("q", 17);
		list.insert("r", 18);
		list.insert("s", 19);
		list.insert("t", 20);
		list.insert("u", 21);
		list.insert("v", 22);
		list.insert("w", 23);
		list.insert("x", 24);
		list.insert("y", 25);
		list.insert("z", 26);
/*
		for(int index = 0; index < 1000000; ++ index) {
			list.insert(Integer.toString(index), index);
		}
*/

		System.out.println(String.format("getTableSize  : %d", list.getTableSize()));
		System.out.println(String.format("getSize  : %d", list.size()));
		System.out.println(String.format("aa : %d, bb : %d", list.find("aa"), list.find("bb")));
		System.out.println(String.format("%s", list.toString()));
	}


	@Test(timeout = 3000)
	public void testBasicHashTable2() {
		ChainingHashTable<AlphabeticString, String> list = new ChainingHashTable<>(MoveToFrontList::new);

		list.insert(a("a"), "1");
		list.insert(a("b"), "2");
		list.insert(a("aa"), "101");
		list.insert(a("bb"), "201");
		list.insert(a("c"), "3");
		list.insert(a("d"), "4");
		list.insert(a("e"), "5");
		list.insert(a("fAB"), "6");
		list.insert(a("Hello World"), "6");

		for(int index = 0; index < 1000000; ++ index) {
			list.insert(a(Integer.toString(index)), Integer.toString(index));
		}

		System.out.println(String.format("getTableSize  : %d", list.getTableSize()));
		System.out.println(String.format("aa : %s, bb : %s", list.find(a("aa")), list.find(a("bb"))));
		System.out.println(String.format("getSize  : %d", list.size()));
		//System.out.println(String.format("%s", list.toString()));
	}


	@Test(timeout = 3000)
	public void testHugeHashTable() {
		ChainingHashTable<String, Integer> list = new ChainingHashTable<>(MoveToFrontList::new);

		int n = 1000;

		// Add them
		for (int i = 0; i < 5 * n; i++) {
			int k = (i % n) * 37 % n;
			String str = String.format("%05d", k);
			for (int j = 0; j < k + 1; j ++)
				incCount(list, str);
		}

		// Delete them all
		int totalCount = 0;
		for (Item<String, Integer> dc : list) {
			assertEquals((Integer.parseInt(dc.key) + 1) * 5, (int) dc.value);
			totalCount += dc.value;
		}
		assertEquals(totalCount, (n * (n + 1)) / 2 * 5);
		assertEquals(list.size(), n);
		assertNotNull(list.find("00851"));
		assertEquals(4260, (int) list.find("00851"));
	}


	private static AlphabeticString a(String s) {
		return new AlphabeticString(s);
	}

}
