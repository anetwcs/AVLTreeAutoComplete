package tests.gitlab.ckpt1;

import cse332.datastructures.containers.Item;
import cse332.types.AlphabeticString;
import datastructures.dictionaries.MoveToFrontList;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DebugTest {

	@SuppressWarnings("unchecked")
	@Test(timeout = 1000000)
	public void checkMoveToFrontListBasic() {
		Integer ret;

		MoveToFrontList<Integer, Integer> list = new MoveToFrontList<Integer, Integer>();

		ret = list.insert(6, 100);
		System.out.println(String.format("ret : %d", ret));

		ret = list.insert(5, 200);
		System.out.println(String.format("ret : %d", ret));

		ret = list.insert(10, 110);
		System.out.println(String.format("ret : %d", ret));

		ret = list.insert(14, 300);
		System.out.println(String.format("ret : %d", ret));
/*
		ret = list.insert(6, 400);
		System.out.println(String.format("ret : %d", ret));

		ret = list.insert(100, 400);
		System.out.println(String.format("ret : %d", ret));
*/
		System.out.println(String.format("%s", list.toString()));
	}


	@SuppressWarnings("unchecked")
	@Test(timeout = 1000000)
	public void checkMoveToFrontListBString() {

		AlphabeticString ret;
		MoveToFrontList<AlphabeticString, AlphabeticString> list = new MoveToFrontList<AlphabeticString, AlphabeticString>();

		ret = list.insert(a("abc"), a("100"));
		System.out.println(String.format("ret : %s", ret));

		ret = list.insert(a("bcd"), a("200"));
		System.out.println(String.format("ret : %s", ret));

		ret = list.insert(a("abc"), a("300"));
		System.out.println(String.format("ret : %s", ret));

		System.out.println(String.format("%s", list.toString()));
	}


	@SuppressWarnings("unchecked")
	@Test(timeout = 1000000)
	public void checkBStringCompare() {

		AlphabeticString s1, s2, s3, s4, s5, s6, s7;

		s1 = a("hello");
		s2 = a("hello");
		s3 = a("meklo");
		s4 = a("hemlo");
		s5 = a("flag");
		s6 = a("hell");
		s7 = a("helloa");

		// Test case reference : https://www.javatpoint.com/java-string-compareto
		System.out.println(s1.compareTo(s2)); //0 because both are equal
		System.out.println(s1.compareTo(s3)); //-5 because "h" is 5 times lower than "m"
		System.out.println(s1.compareTo(s4)); //-1 because "l" is 1 times lower than "m"
		System.out.println(s1.compareTo(s5)); //2 because "h" is 2 times greater than "f"
		System.out.println(s1.compareTo(s6)); //1
		System.out.println(s1.compareTo(s7)); //-1

	}



	@SuppressWarnings("unchecked")
	@Test(timeout = 1000000)
	public void checkBStringEqual() {

		AlphabeticString s1, s2, s3, s4, s5;

		s1 = a("javatpoint");
		s2 = a("javatpoint");
		s3 = a("JAVATPOINT");
		s4 = a("python");

		// Test case reference : https://www.javatpoint.com/java-string-equals
		System.out.println(s1.equals(s2)); //true because content and case is same
		System.out.println(s1.equals(s3)); //false because case is not same
		System.out.println(s1.equals(s4)); //false because content is not same

	}



	private static AlphabeticString a(String s) {
		return new AlphabeticString(s);
	}

}
