package ru.rik.cardsnew;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class Rules {
private volatile List<String[]> rules;
	
	public Rules() {
	}
	
	public void load(List<String> list) {
		if (list == null)
			return;
		List<String[]> newRules = new ArrayList<>();
		for (String s: list) {
			String [] a = s.split("\\s+");
			if	(a.length == 2) {
				a = Arrays.copyOf(a, 3);  // if the rule hasn't prefix that has to be added
				a[2] = "";
			}	
				
			if (isOk(a))
				rules.add(a);
		}
		rules = newRules;
	}
	
	
	private boolean isOk(String[] a) {
		if (a.length != 3)
			return false;
		for (String s: a) {
			for (char ch: s.toCharArray())
				if (!Character.isDigit(ch)) {
					System.err.println(s + " is not verified");
					return false;
				}	
		}
		return true;
	}
	
	
	private String[] find(String number) {
		if (number == null || number.length() == 0)
			return null;
		String[] res = null;
		int longest = 0;
		for (String[] a: rules) {
			if (number.startsWith(a[0]) && a[0].length() > longest) {
				res = a;
				longest = a[0].length();
			}	
		}
		System.out.println(Arrays.toString(res));
		return res;
	}
	
	
	public String apply(String n) {
		if (n == null || n.length() == 0) return null;
		String[] rule = find(n);
		if (rule == null ) 
			return n;	
		
		int cut = Integer.parseInt(rule[1]);
		if (cut == 0 || n.length() <= cut )
			return n;
		
		return (rule[2] + n.substring(cut));	
	}
	
	
	public static void main(String[] args) {
		List<String> list = Arrays.asList(new String[] {
			"7		 0	   	0",
			"7495775		 4	   	",
			"7495 1	  8  "
		});
		Rules r = new Rules();
		r.load(list);
		
		
	}
}
