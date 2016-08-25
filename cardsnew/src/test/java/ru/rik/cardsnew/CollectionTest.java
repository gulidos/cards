package ru.rik.cardsnew;

import java.util.Map.Entry;
import java.util.TreeMap;


public class CollectionTest {

	public CollectionTest() {
	}
	
	
	public static void main(String[] args) {		
		Dog d1 = new Dog("red30", "0000000000.0000000");
		Dog d2 = new Dog("red30", "1471693329.6137674");
		Dog d3 = new Dog("red30", "9999999999.9999999");
		Dog d4 = new Dog("red30", "6245234523.4362364");
		Dog d5 = new Dog("red30", "2353452453.4362364");
		Dog d6 = new Dog("red30", "a267654523.4362364");
		Dog d7 = new Dog("red30", "A432346797.4362364");
		Dog d8 = new Dog("red30", "0467224654.4362364");
		
		TreeMap<String, Dog> treeMap = new TreeMap<>();
		treeMap.put(d1.getKey(), d1);
		treeMap.put(d2.getKey(), d2);
		treeMap.put(d3.getKey(), d3);
		treeMap.put(d4.getKey(), d4);
		treeMap.put(d5.getKey(), d5);
		treeMap.put(d6.getKey(), d6);
		treeMap.put(d7.getKey(), d7);
		treeMap.put(d8.getKey(), d8);
		
//		Comparator.
//		.subMap("red30_9", true, "red30_a", true)
		for (Entry<String, Dog> entry : treeMap.entrySet()) {
			System.out.println(entry.getKey() + " - " + entry.getValue());
		}
		
		
		
//		treeMap.clear();
//		
//		treeMap.put(d1.name, d1);
//		treeMap.put(d2.name, d2);
//		treeMap.put(d3.name, d3);
//		treeMap.put(d4.name, d4);
		
		
//		.subMap("dog3", true, d2, true)
//		for (Entry<String, Dog> entry : treeMap.entrySet()) {
//			System.out.println(entry.getKey() + " - " + entry.getValue());
//		}
	}

	
}

class Dog {
	String id;
	String name;
 
	Dog(String id, String n) {
		this.id = id;
		this.name = n;
	}
 
	String getKey() {
		return id + "_" + name;
	}
	
 
	@Override
	public String toString() {
		return "Dog [id=" + id + ", name=" + name + "]";
	}


	
}
 