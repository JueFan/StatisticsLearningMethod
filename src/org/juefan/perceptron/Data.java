package org.juefan.perceptron;

import java.util.ArrayList;

public class Data {
	public ArrayList<Integer> x;
	public int y;
	
	/**��ȡһ������ת��Ϊ��׼��ʽ*/
	public Data(String content){
		String[] strings = content.split("\t| |:");
		ArrayList<Integer> xList = new ArrayList<Integer>();
		for(int i = 1; i < strings.length; i++){
			xList.add(Integer.parseInt(strings[i]));
		}
		this.x = new ArrayList<>();
		this.x = xList;
		this.y = Integer.parseInt(strings[0]);
	}
}
