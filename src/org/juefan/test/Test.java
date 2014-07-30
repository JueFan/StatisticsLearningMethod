package org.juefan.test;

import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		ArrayList<String> tList = new ArrayList<String>();
		for(int i = 0; i < 10; i++)
			tList.add(Integer.toString(i));
		for(int j = 0; j < 3; j++){
			ArrayList<String> tList2 = new ArrayList<String>(tList);
			tList2.remove(j);
			System.out.println(tList2.size());
		}
	}
}
