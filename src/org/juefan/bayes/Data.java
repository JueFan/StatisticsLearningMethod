package org.juefan.bayes;

import java.util.ArrayList;

public class Data {
	public ArrayList<Object> x;
	public Object y;
	
	/**读取一行数据转化为标准格式*/
	public Data(String content){
		String[] strings = content.split("\t| |:");
		ArrayList<Object> xList = new ArrayList<Object>();
		for(int i = 1; i < strings.length; i++){
			xList.add(strings[i]);
		}
		this.x = new ArrayList<>();
		this.x = xList;
		this.y = strings[0];
	}
	
	public Data(){
		x  = new ArrayList<>();
		y = 0;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("[ ");
		for(int i = 0; i < x.size() - 1; i++)
			builder.append(x.get(i).toString()).append(",");
		builder.append(x.get(x.size() - 1).toString());
		builder.append(" ]");
		return builder.toString();
	}
}
