package ru.rik.cardsnew;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class SortingTests {

	private static int[] arr = {5,1,3,5,3,4,7,5,8,0,76,533,43 };
	public SortingTests() {	}

	@Test
	public void insertSortingTest() {
		List<Integer> orig = ArrayList<Integer>(arr); 
	}
	
	public static List<Integer> inserSort(List<Integer> origList) {
		List<Integer> result = new LinkedList<>();
		
		orig: for (Integer i: origList) {
			for (int j = 0; j < result.size()-1; j++) {
				if (i < result.get(j)) {
					result.add(i);
					break orig;
				}
			}
		}
		return result;
		
	}
}
